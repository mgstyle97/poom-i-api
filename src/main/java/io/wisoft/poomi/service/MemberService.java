package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.member.SigninDto;
import io.wisoft.poomi.bind.dto.member.CMInfoRegisterDto;
import io.wisoft.poomi.bind.dto.member.SignupDto;
import io.wisoft.poomi.bind.request.member.SigninRequest;
import io.wisoft.poomi.bind.request.member.CMInfoRegisterRequest;
import io.wisoft.poomi.bind.request.member.SignupRequest;
import io.wisoft.poomi.bind.utils.FileUtils;
import io.wisoft.poomi.common.error.exceptions.DuplicateMemberException;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.authority.AuthorityRepository;
import io.wisoft.poomi.domain.member.child.ChildRepository;
import io.wisoft.poomi.domain.member.cmInfo.ChildminderInfo;
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
    public SignupDto signup(final SignupRequest signupRequest, final List<MultipartFile> images) {
        Member member = saveMember(signupRequest);

        log.info("Generate member: {}", member.getEmail());

        FileUtils.saveImageWithUserEmail(member.getEmail(), images);

        return SignupDto.of(member);
    }

    @Transactional(readOnly = true)
    public SigninDto signin(SigninRequest signinRequest) {
        Member member = memberRepository.getMemberByEmail(signinRequest.getEmail());
        Authentication authentication = toAuthentication(signinRequest, member.getAuthority());

        String accessToken = jwtTokenProvider.generateToken(authentication);
        log.info("Generate JWT token: {}", accessToken);

        return SigninDto.of(authentication.getName(), accessToken);
    }

    @Transactional(readOnly = true)
    public CMInfoRegisterDto cmInfoUpdate(Member member,
                                          final CMInfoRegisterRequest cmInfoRegisterRequest) {
        member.updateChildminderInfo(ChildminderInfo.from(cmInfoRegisterRequest));
        memberRepository.save(member);

        return new CMInfoRegisterDto(member.getChildminderInfo().getId(), member.getEmail());
    }

    private Member saveMember(final SignupRequest signupRequest) {
        verifyEmailAndNick(signupRequest.getEmail(), signupRequest.getNick());
        log.info("Verify email and nick from request");

        Member member = Member.of(signupRequest, passwordEncoder, authorityRepository.getUserAuthority());
        log.info("Generate member data through request");

        AddressTag addressTag = addressTagRepository
                .saveAddressTagWithExtraAddress(signupRequest.getAddress().getExtraAddress());
        log.info("Generate address tag data through request");

        Address address = Address.of(signupRequest.getAddress(), addressTag);
        addressRepository.save(address);
        log.info("Generate address data through request");

        member.updateAddressInfo(address);
        member.setChildren(signupRequest.getChildren());
        log.info("Set request data to member properties");

        memberRepository.save(member);
        childRepository.saveAll(member.getChildren());
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
