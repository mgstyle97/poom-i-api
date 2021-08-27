package io.wisoft.poomi.domain.program.classes;

import io.wisoft.poomi.domain.member.Gender;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.domain.member.authority.AuthorityRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.auditing.config.AuditingConfiguration;
import org.springframework.data.domain.AuditorAware;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {AuditorAware.class, AuditingConfiguration.class}
))
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.ANY
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClassProgramRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AddressTagRepository addressTagRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ClassProgramRepository classProgramRepository;

    private Member member;

    @BeforeAll
    void setup() {
        // given
        AddressTag addressTag = new AddressTag("city");
        addressTagRepository.save(addressTag);

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
    }

    @Test
    @DisplayName("클래스 프로그램 생성 테스트")
    void creation_class_program() {
        // given
        ClassProgram classProgram = ClassProgram.builder()
                        .title("테스트")
                        .contents("테스트입니다.")
                        .capacity(15L)
                        .isBoard(false)
                        .isRecruit(false)
                        .writer(member)
                        .build();

        classProgramRepository.save(classProgram);
        System.out.println(classProgram.getCreatedAt());

        assertAll(
                () -> assertEquals("테스트", classProgram.getTitle()),
                () -> assertEquals("테스트입니다.", classProgram.getContents()),
                () -> assertEquals(15L, classProgram.getCapacity()),
                () -> assertFalse(classProgram.getIsBoard()),
                () -> assertFalse(classProgram.getIsRecruit()),
                () -> assertNotNull(classProgram.getCreatedAt())
        );

    }

    @Test
    @DisplayName("클래스 프로그램 삭제 테스트")
    void delete_class_program() {
        ClassProgram classProgram = ClassProgram.builder()
                .title("테스트")
                .contents("테스트입니다.")
                .capacity(15L)
                .isBoard(false)
                .isRecruit(false)
                .writer(member)
                .build();

        classProgramRepository.save(classProgram);

        classProgramRepository.deleteById(classProgram.getId());

        Optional<ClassProgram> deletedClassProgram = classProgramRepository.findById(1L);

        assertFalse(deletedClassProgram.isPresent());
    }

}