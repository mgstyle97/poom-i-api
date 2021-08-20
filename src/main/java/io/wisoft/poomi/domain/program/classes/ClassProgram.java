package io.wisoft.poomi.domain.program.classes;

import io.wisoft.poomi.bind.request.ClassProgramRegisterRequest;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.program.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "class_sequence_generator",
        sequenceName = "class_sequence",
        initialValue = 1,
        allocationSize = 1
)
public class ClassProgram extends BaseTimeEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "class_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(
            name = "contents",
            columnDefinition = "TEXT"
    )
    private String contents;

    @Column(name = "capacity")
    private Long capacity;

    @Column(name = "is_recruit")
    private Boolean isRecruit;

    @Column(name = "is_board")
    private Boolean isBoard;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "member_id",
            referencedColumnName = "id"
    )
    private Member writer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "address_tag_id",
            referencedColumnName = "id"
    )
    private AddressTag addressTag;

    @Builder
    public ClassProgram(final String title, final String contents,
                        final Long capacity,
                        final Boolean isRecruit, final Boolean isBoard,
                        final Member writer) {
        this.title = title;
        this.contents = contents;
        this.capacity = capacity;
        this.isRecruit = isRecruit;
        this.isBoard = isBoard;
        this.writer = writer;
        this.addressTag = writer.getAddressTag();
    }

    public static ClassProgram of(final Member member,
                                  final ClassProgramRegisterRequest classProgramRegisterRequest) {
        ClassProgram classProgram = ClassProgram.builder()
                .title(classProgramRegisterRequest.getTitle())
                .contents(classProgramRegisterRequest.getContents())
                .capacity(classProgramRegisterRequest.getCapacity())
                .isRecruit(classProgramRegisterRequest.getIsRecruit())
                .isBoard(classProgramRegisterRequest.getIsBoard())
                .writer(member)
                .build();

        return classProgram;
    }

}
