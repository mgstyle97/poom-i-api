package io.wisoft.poomi.domain.member.child;

import io.wisoft.poomi.bind.request.ChildAddRequest;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@SequenceGenerator(
        name = "child_sequence_generator",
        sequenceName = "child_sequence",
        initialValue = 1,
        allocationSize = 1
)
public class Child {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "child_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "school")
    private String school;

    @Column(name = "special_note")
    private String specialNote;

    public static Child of(ChildAddRequest childAddRequest, ChildRepository childRepository) {
        Child child = childRepository.findByName(childAddRequest.getName())
                .orElse(new Child());

        BeanUtils.copyProperties(childAddRequest, child);
        childRepository.save(child);

        return child;
    }

}
