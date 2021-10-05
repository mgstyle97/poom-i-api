package io.wisoft.poomi.domain.member;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Embeddable
public class ChildCareGroupProperties {

    @OneToMany(
            mappedBy = "writer",
            fetch = FetchType.LAZY
    )
    private Set<ChildCareGroup> writtenGroups;

    @ManyToMany(
            mappedBy = "appliers",
            fetch = FetchType.LAZY
    )
    private Set<ChildCareGroup> appliedGroups;

    @ManyToMany(
            mappedBy = "likes",
            fetch = FetchType.LAZY
    )
    private Set<ChildCareGroup> likedGroups;

    public ChildCareGroupProperties() {
        this.writtenGroups = new HashSet<>();
        this.appliedGroups = new HashSet<>();
        this.likedGroups = new HashSet<>();
    }

}
