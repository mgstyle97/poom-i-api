package io.wisoft.poomi.service.member;

import io.wisoft.poomi.domain.auth.residence.ResidenceCertification;
import io.wisoft.poomi.domain.auth.residence.ResidenceCertificationRepository;
import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.file.UploadFileRepository;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.request.member.ProfileImageUploadRequest;
import io.wisoft.poomi.global.dto.response.member.ChildAndPoomiResponse;
import io.wisoft.poomi.global.dto.response.member.SignupResponse;
import io.wisoft.poomi.global.dto.request.member.SignupRequest;
import io.wisoft.poomi.global.utils.UploadFileUtils;
import io.wisoft.poomi.global.exception.exceptions.DuplicateMemberException;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.authority.AuthorityRepository;
import io.wisoft.poomi.domain.member.child.ChildRepository;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.service.auth.certification.CertificationService;
import io.wisoft.poomi.service.child_care.expert.ChildCareExpertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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
    private final UploadFileRepository uploadFileRepository;
    private final ResidenceCertificationRepository residenceCertificationRepository;

    private final UploadFileUtils uploadFileUtils;
    private final ChildCareExpertService expertService;

    @Transactional
    public SignupResponse signup(final SignupRequest signupRequest, final HttpServletRequest request) {
        Member member = saveMember(signupRequest, request);
        log.info("Generate member: {}", member.getEmail());

        return SignupResponse.of(member);
    }

    @Transactional
    public void saveProfileImage(final ProfileImageUploadRequest profileImageUploadRequest, final Member member) {
        UploadFile profileImage = uploadFileUtils.saveFileAndConvertImage(profileImageUploadRequest.getImageMetaData());
        uploadFileRepository.save(profileImage);
        member.saveProfileImage(profileImage);
    }

    @Transactional(readOnly = true)
    public ChildAndPoomiResponse childAndPoomi(final Member member) {
        List<ChildCareExpert> memberMangedExpertList = expertService.findAllExpert().stream()
                .filter(expert -> expert.getRecruitmentStatus().equals(RecruitmentStatus.CLOSED))
                .filter(expert -> expert.getManager().equals(member))
                .collect(Collectors.toList());
        return ChildAndPoomiResponse.of(member, memberMangedExpertList);
    }

    private Member saveMember(final SignupRequest signupRequest, final HttpServletRequest request) {
        verifyEmailAndNick(signupRequest.getEmail(), signupRequest.getNick());
        log.info("Verify email and nick from request");

        Member member = Member.of(signupRequest, passwordEncoder, authorityRepository.getAnonymousAuthority());
        log.info("Generate member data through request");

        saveAddressInfo(member, signupRequest);

        saveChildInfo(member, signupRequest, request);

        saveResidenceCertification(member, signupRequest.getAddressCertificationFileData(), request);

        return memberRepository.save(member);
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
                .saveAddressTagWithExtraAddress(signupRequest.getExtraAddress().trim());
        log.info("Generate address tag data through request");

        Address address = Address.of(signupRequest, addressTag);
        addressRepository.save(address);
        log.info("Generate address data through request");

        member.updateAddressInfo(address);
        log.info("Set request data to member properties");

    }

    private void saveChildInfo(final Member member, final SignupRequest signupRequest,
                               final HttpServletRequest request) {

        if (signupRequest.getChildren() != null) {
            List<Child> children = signupRequest.getChildren().stream()
                    .map(childAddRequest -> Child.of(childAddRequest, member))
                    .collect(Collectors.toList());
            childRepository.saveAll(children);
            member.setChildren(children);
            log.info("Save member data through member repository");

            saveFamilyCertificationFile(member, signupRequest.getFamilyCertificateFileData(), request);
        }
    }

    private void saveResidenceCertification(final Member member, final String residenceCertificationFileData,
                                            final HttpServletRequest request) {
        UploadFile residenceFile = uploadFileUtils
                .saveFileWithEncryption(residenceCertificationFileData, request);
        uploadFileRepository.save(residenceFile);
        log.info("Upload S3 Residence file id: {}", residenceFile.getId());

        ResidenceCertification residenceCertification = ResidenceCertification.builder()
                .member(member)
                .residenceFile(residenceFile)
                .build();
        residenceCertificationRepository.save(residenceCertification);
        log.info("Save Residence Certification id: {}", residenceCertification.getId());

        member.setResidenceCertification(residenceCertification);
    }

    private void saveFamilyCertificationFile(final Member member, final String familyCertificationFileData,
                                             final HttpServletRequest request) {
        member.setFamilyCertificationFile(
                uploadFileRepository.save(
                        uploadFileUtils.saveFileWithEncryption(familyCertificationFileData, request)
                )
        );
        memberRepository.save(member);
    }

}
