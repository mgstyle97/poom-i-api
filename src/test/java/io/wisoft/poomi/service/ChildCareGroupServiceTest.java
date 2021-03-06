package io.wisoft.poomi.service;

import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupApplyRequest;
import io.wisoft.poomi.global.dto.response.child_care.group.ChildCareGroupLookupResponse;
import io.wisoft.poomi.global.dto.request.child_care.group.ChildCareGroupRegisterRequest;
import io.wisoft.poomi.domain.member.Gender;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.domain.member.authority.AuthorityRepository;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroupRepository;
import io.wisoft.poomi.service.child_care.group.ChildCareGroupService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChildCareGroupServiceTest {

    @Autowired
    private ChildCareGroupService childCareGroupService;

    @Autowired
    private ChildCareGroupRepository childCareGroupRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AddressTagRepository addressTagRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeAll
    void setup() {
        // given

        AddressTag addressTag = addressTagRepository
                .saveAddressTagWithExtraAddress("city");

        Address address = Address.builder()
                .addressTag(addressTag)
                .postCode("12345")
                .detailAddress("Seoul city")
                .build();
        addressRepository.save(address);

        member = Member.builder()
                .name("test")
                .phoneNumber("01011111111")
                .email("test@gmail.com")
                .password("1234")
                .authorities(Collections.singleton(authorityRepository.getUserAuthority()))
                .address(address)
                .nick("test")
                .gender(Gender.MALE)
                .build();
        memberRepository.save(member);

        ChildCareGroup childCareGroup = ChildCareGroup.builder()
                .recruitmentStatus(RecruitmentStatus.RECRUITING)
                .writer(member)
                .build();

        ChildCareGroup childCareGroup2 = ChildCareGroup.builder()
                .recruitmentStatus(RecruitmentStatus.RECRUITING)
                .writer(member)
                .build();
        childCareGroupRepository.saveAll(List.of(childCareGroup, childCareGroup2));
    }

    @AfterAll
    void cleanup() {
        childCareGroupRepository.deleteAll();
        memberRepository.deleteAll();
        addressRepository.deleteAll();
        addressTagRepository.deleteAll();
    }

    @Test
    @DisplayName("?????????: ?????? ????????? ?????? ??????")
    void select_through_address_tag() {
        List<ChildCareGroupLookupResponse> result = childCareGroupService.findByAddressTag(member.getAddressTag());
        result
                .forEach(childCareGroupLookupResponse -> System.out.println(childCareGroupLookupResponse.getGroupName()));
    }

    @Test
    @DisplayName("?????????: ????????? ???????????? ??????")
    @Disabled
    void save_class_program() {
        ChildCareGroupRegisterRequest childCareGroupRegisterRequest = new ChildCareGroupRegisterRequest();
        childCareGroupRegisterRequest.setName("?????????3");
        childCareGroupRegisterRequest.setRecruitmentStatus(null);

//        classProgramService.registerClassProgram(member, classProgramRegisterRequest);

        List<ChildCareGroupLookupResponse> result = childCareGroupService.findByAddressTag(member.getAddressTag());
        result
                .forEach(childCareGroupLookupResponse -> System.out.println(childCareGroupLookupResponse.getGroupName()));
    }

    @Test
    @DisplayName("?????????: ????????? ???????????? ??????")
    @Transactional
    void apply_class_program() {
        // given
        ChildCareGroupApplyRequest applyRequest = new ChildCareGroupApplyRequest();
        applyRequest.setContents("??? ??????????????????.");

        childCareGroupService.applyChildCareGroup(1L, member, applyRequest);

        ChildCareGroup childCareGroup = childCareGroupRepository.getById(1L);

    }

    @Test
    @DisplayName("?????????: ????????? ???????????? ?????????")
    @Transactional
    void like_class_program() {

        ChildCareGroup childCareGroup = childCareGroupRepository.getById(1L);


    }

}