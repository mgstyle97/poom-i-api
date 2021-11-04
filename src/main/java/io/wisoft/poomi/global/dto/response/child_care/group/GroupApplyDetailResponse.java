package io.wisoft.poomi.global.dto.response.child_care.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.apply.GroupApply;
import io.wisoft.poomi.domain.member.child.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;

@Getter
@Setter
public class GroupApplyDetailResponse {

    @JsonProperty("apply_id")
    private Long applyId;

    @JsonProperty("apply_contents")
    private String applyContents;

    @JsonProperty("applier_id")
    private Long applierId;

    @JsonProperty("applier_nick")
    private String applierNick;

    @JsonProperty("child_id")
    private Long childId;

    @JsonProperty("child_name")
    private String childName;

    @JsonProperty("child_birthday")
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date childBirthday;

    @Builder
    public GroupApplyDetailResponse(final Long applyId, final String applyContents,
                                    final Long applierId, final String applierNick) {
        this.applyId = applyId;
        this.applyContents = applyContents;
        this.applierId = applierId;
        this.applierNick = applierNick;
    }

    public static GroupApplyDetailResponse of(final GroupApply groupApply) {
        GroupApplyDetailResponse applyDetail = GroupApplyDetailResponse.builder()
                .applyId(groupApply.getId())
                .applyContents(groupApply.getContents())
                .applierId(groupApply.getWriter().getId())
                .applierNick(groupApply.getWriter().getNick())
                .build();
        applyDetail.setChildData(Optional.ofNullable(groupApply.getChild()));

        return applyDetail;
    }

    public void setChildData(final Optional<Child>optionalChild) {
        optionalChild.ifPresent(child -> {
            this.childId = child.getId();
            this.childName = child.getName();
            this.childBirthday = child.getBirthday();
        });
    }

}
