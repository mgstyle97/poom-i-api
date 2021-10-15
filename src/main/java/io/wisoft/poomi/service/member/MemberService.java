package io.wisoft.poomi.service.member;

import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.response.member.SignupResponse;
import io.wisoft.poomi.global.dto.request.member.SignupRequest;
import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import io.wisoft.poomi.global.utils.MultipartFileUtils;
import io.wisoft.poomi.global.exception.exceptions.DuplicateMemberException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    @Transactional
    public SignupResponse signup(final SignupRequest signupRequest, final List<MultipartFile> images) {
        Member member = saveMember(signupRequest);

        log.info("Generate member: {}", member.getEmail());

        MultipartFileUtils.saveImageWithUserEmail(member, images);

        return SignupResponse.of(member);
    }

    @Transactional(readOnly = true)
    public byte[] getMemberProfileImage(final String nick) {
        final Member member = memberRepository.findByNick(nick).orElseThrow(
                () -> new NotFoundEntityDataException("member nick: " + nick + "에 해당하는 회원이 없습니다.")
        );

        return MultipartFileUtils.findFileByteArray(member.getProfileImagePath());
    }

    @Transactional
    public void saveProfileImage(final MultipartFile profileImage, final Member member) {
        MultipartFileUtils.saveProfileImage(profileImage, member);

        memberRepository.save(member);
    }

    private Member saveMember(final SignupRequest signupRequest) {
        verifyEmailAndNick(signupRequest.getEmail(), signupRequest.getNick());
        log.info("Verify email and nick from request");

        Member member = Member.of(signupRequest, passwordEncoder, authorityRepository.getUserAuthority());
        log.info("Generate member data through request");

        saveAddressInfo(member, signupRequest);

        saveChildInfo(member, signupRequest);

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

    private void saveAddressInfo(final Member member, final SignupRequest signupRequest) {

        AddressTag addressTag = addressTagRepository
                .saveAddressTagWithExtraAddress(signupRequest.getExtraAddress());
        log.info("Generate address tag data through request");

        Address address = Address.of(signupRequest, addressTag);
        addressRepository.save(address);
        log.info("Generate address data through request");

        member.updateAddressInfo(address);
        memberRepository.save(member);
        log.info("Set request data to member properties");
    }

    private void saveChildInfo(final Member member, final SignupRequest signupRequest) {

        if (signupRequest.getChildren() != null) {
            List<Child> children = signupRequest.getChildren().stream()
                    .map(childAddRequest -> Child.of(childAddRequest, member))
                    .collect(Collectors.toList());
            childRepository.saveAll(children);
            member.setChildren(children);
            log.info("Save member data through member repository");
        }
    }

}
