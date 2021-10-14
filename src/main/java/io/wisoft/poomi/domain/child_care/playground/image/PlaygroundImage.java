package io.wisoft.poomi.domain.child_care.playground.image;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
@SequenceGenerator(
        name = "playground_image_sequence_generator",
        sequenceName = "playground_image_sequence",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "playground_image")
public class PlaygroundImage {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "playground_image_sequence_generator"
    )
    @Column(name = "id")
    private Long id;



}
