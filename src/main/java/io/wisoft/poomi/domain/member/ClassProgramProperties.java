package io.wisoft.poomi.domain.member;

import io.wisoft.poomi.domain.program.classes.ClassProgram;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Embeddable
public class ClassProgramProperties {

    @OneToMany(
            mappedBy = "writer",
            fetch = FetchType.LAZY
    )
    private Set<ClassProgram> writtenClasses;

    @ManyToMany(
            mappedBy = "appliers",
            fetch = FetchType.LAZY
    )
    private Set<ClassProgram> appliedClasses;

    @ManyToMany(
            mappedBy = "likes",
            fetch = FetchType.LAZY
    )
    private Set<ClassProgram> likedClasses;

    public ClassProgramProperties() {
        this.writtenClasses = new HashSet<>();
        this.appliedClasses = new HashSet<>();
        this.likedClasses = new HashSet<>();
    }

}
