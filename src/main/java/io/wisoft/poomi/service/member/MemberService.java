package io.wisoft.poomi.service.member;

import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.response.member.SignupResponse;
import io.wisoft.poomi.global.dto.request.member.SigninRequest;
import io.wisoft.poomi.global.dto.request.member.SignupRequest;
import io.wisoft.poomi.global.utils.FileUtils;
import io.wisoft.poomi.global.exception.exceptions.DuplicateMemberException;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.authority.AuthorityRepository;
import io.wisoft.poomi.domain.member.child.ChildRepository;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final AddressRepository addressRepository;
    private final AddressTagRepository addressTagRepository;
    private final ChildRepository childRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public SignupResponse signup(final SignupRequest signupRequest, final List<MultipartFile> images) {
        Member member = saveMember(signupRequest);

        log.info("Generate member: {}", member.getEmail());

        FileUtils.saveImageWithUserEmail(member.getEmail(), images);

        return SignupResponse.of(member);
    }

    @Transactional(readOnly = true)
    public String signin(SigninRequest signinRequest) {
        Member member = memberRepository.getMemberByEmail(signinRequest.getEmail());
        Authentication authentication = toAuthentication(signinRequest, member.getAuthority());

        String accessToken = jwtTokenProvider.generateToken(authentication);
        log.info("Generate JWT token: {}", accessToken);

        return accessToken;
    }

    private Member saveMember(final SignupRequest signupRequest) {
        verifyEmailAndNick(signupRequest.getEmail(), signupRequest.getNick());
        log.info("Verify email and nick from request");

        Member member = Member.of(signupRequest, passwordEncoder, authorityRepository.getUserAuthority());
        log.info("Generate member data through request");

        AddressTag addressTag = addressTagRepository
                .saveAddressTagWithExtraAddress(signupRequest.getExtraAddress());
        log.info("Generate address tag data through request");

        Address address = Address.of(signupRequest, addressTag);
        addressRepository.save(address);
        log.info("Generate address data through request");

        member.updateAddressInfo(address);
        memberRepository.save(member);
        log.info("Set request data to member properties");

        List<Child> children = signupRequest.getChildren().stream()
                        .map(childAddRequest -> Child.of(childAddRequest, member))
                                .collect(Collectors.toList());
        childRepository.saveAll(children);
        member.setChildren(children);
        log.info("Save member data through member repository");

        return member;
    }

    private void verifyEmailAndNick(final String email, final String nick) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateMemberException("이미 존재하는 이메일입니다.");
        }

        if (memberRepository.existsByNick(nick)) {
            throw new DuplicateMemberException("이미 존재하는 닉네임입니다.");
        }
    }

    private Authentication toAuthentication(final SigninRequest signinRequest,
                                            final String authority) {
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(signinRequest.toAuthentication(authority));

        log.info("Generate authentication: {}", authentication.getPrincipal());
        log.info("Authenticate member: {}", signinRequest.getEmail());

        return authentication;
    }

}
