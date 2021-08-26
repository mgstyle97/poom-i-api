package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.SigninDto;
import io.wisoft.poomi.bind.dto.CMInfoRegisterDto;
import io.wisoft.poomi.bind.dto.SignupDto;
import io.wisoft.poomi.bind.request.SigninRequest;
import io.wisoft.poomi.bind.request.CMInfoRegisterRequest;
import io.wisoft.poomi.bind.request.SignupRequest;
import io.wisoft.poomi.bind.utils.FileUtils;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.domain.member.address.Address;
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

import javax.servlet.http.HttpServletRequest;
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
        Member member = Member.of(signupRequest, passwordEncoder, authorityRepository);
        log.info("Generate member data through request");

        Address address = new Address();
        address.of(addressRepository, addressTagRepository, signupRequest.getAddress());
        log.info("Generate address data through request");

        member.updateAddressInfo(address);
        memberRepository.save(member);

        member.setChildren(signupRequest.getChildren(), childRepository);

        log.info("Save member data through member repository");

        return member;
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
