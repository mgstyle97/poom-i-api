package io.wisoft.poomi.domain.program.classes;

import io.wisoft.poomi.domain.member.Member;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.auditing.config.AuditingConfiguration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

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
    private ClassProgramRepository classProgramRepository;

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
                        .writer(new Member())
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
                .writer(new Member())
                .build();

        classProgramRepository.save(classProgram);

        classProgramRepository.deleteById(classProgram.getId());

        Optional<ClassProgram> deletedClassProgram = classProgramRepository.findById(1L);

        assertFalse(deletedClassProgram.isPresent());
    }

}