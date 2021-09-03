package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.ClassProgramLookupDto;
import io.wisoft.poomi.bind.request.ClassProgramRegisterRequest;
import io.wisoft.poomi.domain.member.Gender;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.domain.member.authority.AuthorityRepository;
import io.wisoft.poomi.domain.program.classes.ClassProgram;
import io.wisoft.poomi.domain.program.classes.ClassProgramRepository;
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
class ClassProgramServiceTest {

    @Autowired
    private ClassProgramService classProgramService;

    @Autowired
    private ClassProgramRepository classProgramRepository;

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

        ClassProgram classProgram = ClassProgram.builder()
                .title("테스트")
                .contents("테스트입니다.")
                .capacity(15L)
                .isRecruit(false)
                .writer(member)
                .build();

        ClassProgram classProgram2 = ClassProgram.builder()
                .title("테스트2")
                .contents("테스트2입니다.")
                .capacity(12L)
                .isRecruit(false)
                .writer(member)
                .build();
        classProgramRepository.saveAll(List.of(classProgram, classProgram2));
    }

    @AfterAll
    void cleanup() {
        classProgramRepository.deleteAll();
        memberRepository.deleteAll();
        addressRepository.deleteAll();
        addressTagRepository.deleteAll();
    }

    @Test
    @DisplayName("테스트: 주소 태그를 통한 조회")
    void select_through_address_tag() {
        List<ClassProgramLookupDto> result = classProgramService.findByAddressTag(member.getAddressTag());
        result
                .forEach(classProgramLookupDto -> System.out.println(classProgramLookupDto.getTitle()));
    }

    @Test
    @DisplayName("테스트: 클래스 프로그램 저장")
    void save_class_program() {
        ClassProgramRegisterRequest classProgramRegisterRequest = new ClassProgramRegisterRequest();
        classProgramRegisterRequest.setTitle("테스트3");
        classProgramRegisterRequest.setContents("테스트3입니다.");
        classProgramRegisterRequest.setIsRecruit(false);
        classProgramRegisterRequest.setCapacity(12L);

        classProgramService.registerClassProgram(member, classProgramRegisterRequest);

        List<ClassProgramLookupDto> result = classProgramService.findByAddressTag(member.getAddressTag());
        result
                .forEach(classProgramLookupDto -> System.out.println(classProgramLookupDto.getTitle()));
    }

    @Test
    @DisplayName("테스트: 클래스 프로그램 지원")
    @Transactional
    void apply_class_program() {
        classProgramService.applyClassProgram(1L, member);

        ClassProgram classProgram = classProgramRepository.findClassProgramById(1L);
        Set<ClassProgram> appliedClasses = member.getClassProgramProperties().getAppliedClasses();

        ClassProgram classProgram1 = appliedClasses.stream()
                .filter(appliedClass -> classProgram.getTitle().equals(appliedClass.getTitle()))
                .collect(Collectors.toList()).get(0);
        assertAll(
                () -> assertNotNull(classProgram1),
                () -> assertTrue(classProgram1.getAppliers().contains(member))
        );
    }

    @Test
    @DisplayName("테스트: 클래스 프로그램 좋아요")
    @Transactional
    void like_class_program() {
        classProgramService.likeClassProgram(1L, member);

        ClassProgram classProgram = classProgramRepository.findClassProgramById(1L);
        Set<ClassProgram> likedClasses = member.getClassProgramProperties().getLikedClasses();

        ClassProgram classProgram1 = likedClasses.stream()
                        .filter(likedClass -> classProgram.getTitle().equals(likedClass.getTitle()))
                        .collect(Collectors.toList()).get(0);

        assertAll(
                () -> assertNotNull(classProgram1),
                () -> assertTrue(classProgram1.getLikes().contains(member))
        );

    }

}