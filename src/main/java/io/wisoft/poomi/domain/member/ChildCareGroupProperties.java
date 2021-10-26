package io.wisoft.poomi.domain.member;

import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Embeddable
public class ChildCareGroupProperties {

    @ManyToMany(
            mappedBy = "participatingMembers",
            fetch = FetchType.LAZY
    )
    private Set<ChildCareGroup> participatingGroups;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "likes")
    private Set<GroupBoard> likeBoards;

    public ChildCareGroupProperties() {
        this.participatingGroups = new HashSet<>();
        this.likeBoards = new HashSet<>();
    }

}
