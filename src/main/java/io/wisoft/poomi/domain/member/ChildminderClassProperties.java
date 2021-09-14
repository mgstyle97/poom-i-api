package io.wisoft.poomi.domain.member;

import io.wisoft.poomi.domain.childminder.classes.ChildminderClass;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Embeddable
public class ChildminderClassProperties {

    @OneToMany(
            mappedBy = "writer",
            fetch = FetchType.LAZY
    )
    private Set<ChildminderClass> writtenClasses;

    @ManyToMany(
            mappedBy = "appliers",
            fetch = FetchType.LAZY
    )
    private Set<ChildminderClass> appliedClasses;

    @ManyToMany(
            mappedBy = "likes",
            fetch = FetchType.LAZY
    )
    private Set<ChildminderClass> likedClasses;

    public ChildminderClassProperties() {
        this.writtenClasses = new HashSet<>();
        this.appliedClasses = new HashSet<>();
        this.likedClasses = new HashSet<>();
    }

}
