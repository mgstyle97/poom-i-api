package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.childminder.classes.ChildminderClassLookupDto;
import io.wisoft.poomi.bind.request.childminder.classes.ChildminderClassRegisterRequest;
import io.wisoft.poomi.domain.member.Gender;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.domain.member.authority.AuthorityRepository;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClass;
import io.wisoft.poomi.domain.childminder.classes.ChildminderClassRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChildminderClassServiceTest {

    @Autowired
    private ChildminderClassService childminderClassService;

    @Autowired
    private ChildminderClassRepository childminderClassRepository;

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

        ChildminderClass childminderClass = ChildminderClass.builder()
                .title("테스트")
                .contents("테스트입니다.")
                .capacity(15L)
                .isRecruit(false)
                .writer(member)
                .build();

        ChildminderClass childminderClass2 = ChildminderClass.builder()
                .title("테스트2")
                .contents("테스트2입니다.")
                .capacity(12L)
                .isRecruit(false)
                .writer(member)
                .build();
        childminderClassRepository.saveAll(List.of(childminderClass, childminderClass2));
    }

    @AfterAll
    void cleanup() {
        childminderClassRepository.deleteAll();
        memberRepository.deleteAll();
        addressRepository.deleteAll();
        addressTagRepository.deleteAll();
    }

    @Test
    @DisplayName("테스트: 주소 태그를 통한 조회")
    void select_through_address_tag() {
        List<ChildminderClassLookupDto> result = childminderClassService.findByAddressTag(member.getAddressTag());
        result
                .forEach(childminderClassLookupDto -> System.out.println(childminderClassLookupDto.getTitle()));
    }

    @Test
    @DisplayName("테스트: 클래스 프로그램 저장")
    @Disabled
    void save_class_program() {
        ChildminderClassRegisterRequest childminderClassRegisterRequest = new ChildminderClassRegisterRequest();
        childminderClassRegisterRequest.setTitle("테스트3");
        childminderClassRegisterRequest.setContents("테스트3입니다.");
        childminderClassRegisterRequest.setIsRecruit(false);
        childminderClassRegisterRequest.setCapacity(12L);

//        classProgramService.registerClassProgram(member, classProgramRegisterRequest);

        List<ChildminderClassLookupDto> result = childminderClassService.findByAddressTag(member.getAddressTag());
        result
                .forEach(childminderClassLookupDto -> System.out.println(childminderClassLookupDto.getTitle()));
    }

    @Test
    @DisplayName("테스트: 클래스 프로그램 지원")
    @Transactional
    void apply_class_program() {
        childminderClassService.applyChildminderClass(1L, member);

        ChildminderClass childminderClass = childminderClassRepository.findChildminderClassById(1L);
        Set<ChildminderClass> appliedClasses = member.getChildminderClassProperties().getAppliedClasses();

        ChildminderClass childminderClass1 = appliedClasses.stream()
                .filter(appliedClass -> childminderClass.getTitle().equals(appliedClass.getTitle()))
                .collect(Collectors.toList()).get(0);
        assertAll(
                () -> assertNotNull(childminderClass1),
                () -> assertTrue(childminderClass1.getAppliers().contains(member))
        );
    }

    @Test
    @DisplayName("테스트: 클래스 프로그램 좋아요")
    @Transactional
    void like_class_program() {
        childminderClassService.likeChildminderClass(1L, member);

        ChildminderClass childminderClass = childminderClassRepository.findChildminderClassById(1L);
        Set<ChildminderClass> likedClasses = member.getChildminderClassProperties().getLikedClasses();

        ChildminderClass childminderClass1 = likedClasses.stream()
                        .filter(likedClass -> childminderClass.getTitle().equals(likedClass.getTitle()))
                        .collect(Collectors.toList()).get(0);

        assertAll(
                () -> assertNotNull(childminderClass1),
                () -> assertTrue(childminderClass1.getLikes().contains(member))
        );

    }

}