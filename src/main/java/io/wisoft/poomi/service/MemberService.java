package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.SigninDto;
import io.wisoft.poomi.bind.dto.CMInfoRegisterDto;
import io.wisoft.poomi.bind.dto.SignupDto;
import io.wisoft.poomi.bind.request.SigninRequest;
import io.wisoft.poomi.bind.request.CMInfoRegisterRequest;
import io.wisoft.poomi.bind.request.SignupRequest;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.cmInfo.ChildminderInfo;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final AddressTagRepository addressTagRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final String USER_INFO_DOCUMENT_PATH = "C:/Image/";

    @Transactional
    public SignupDto signup(SignupRequest signupRequest, List<MultipartFile> files) {
        Member member = saveMember(signupRequest);

        log.info("Generate member: {}", member.getEmail());

        saveDocument(member.getEmail(), files);

        return SignupDto.of(member);
    }

    @Transactional(readOnly = true)
    public SigninDto signin(SigninRequest signinRequest) {
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(signinRequest.toAuthentication());
        String accessToken = jwtTokenProvider.generateToken(authentication);

        return SigninDto.of(authentication.getName(), accessToken);
    }

    @Transactional(readOnly = true)
    public CMInfoRegisterDto cmInfoRegist(Authentication authInfo,
                                          CMInfoRegisterRequest cmInfoRegisterRequest) {
        Member member = memberRepository.getMemberByEmail(authInfo.getName());
        member.setChildminderInfo(ChildminderInfo.from(cmInfoRegisterRequest));
        memberRepository.save(member);

        return new CMInfoRegisterDto(member.getChildminderInfo().getId(), member.getEmail(), new Date());
    }

    private void saveDocument(String email, List<MultipartFile> files) {
        int idx = 1;

        try {
            for (MultipartFile file : files) {
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                File dest =
                        new File(
                                USER_INFO_DOCUMENT_PATH + email + "/" + email + "_" + idx + "." + extension
                        );
                if (!dest.exists()) {
                    dest.mkdirs();
                }
                file.transferTo(dest);
                log.info("Save file: {}", email + "_" + idx);
                idx++;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    private Member saveMember(SignupRequest request) {
        Member member = Member.of(request, passwordEncoder);
        Address address = Address.of(addressRepository, addressTagRepository, request.getAddress());

        member.setAddress(address);
        memberRepository.save(member);

        return member;
    }

}
