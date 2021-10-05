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
    private Set<ChildCareGroup> writtenGroup;

    @ManyToMany(
            mappedBy = "appliers",
            fetch = FetchType.LAZY
    )
    private Set<ChildCareGroup> appliedGroup;

    @ManyToMany(
            mappedBy = "likes",
            fetch = FetchType.LAZY
    )
    private Set<ChildCareGroup> likedGroup;

    public ChildCareGroupProperties() {
        this.writtenGroup = new HashSet<>();
        this.appliedGroup = new HashSet<>();
        this.likedGroup = new HashSet<>();
    }

}
