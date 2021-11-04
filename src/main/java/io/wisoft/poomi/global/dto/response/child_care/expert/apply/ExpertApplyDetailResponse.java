package io.wisoft.poomi.global.dto.response.child_care.expert.apply;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.expert.apply.ChildCareExpertApply;
import io.wisoft.poomi.domain.member.child.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;

@Getter
@Setter
public class ExpertApplyDetailResponse {

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
    @JsonFormat(pattern = "yyyy.MM.ss")
    private Date childBirthday;

    @Builder
    public ExpertApplyDetailResponse(final Long applyId, final String applyContents,
                                     final Long applierId, final String applierNick) {
        this.applyId = applyId;
        this.applyContents = applyContents;
        this.applierId = applierId;
        this.applierNick = applierNick;
    }

    public static ExpertApplyDetailResponse of(final ChildCareExpertApply expertApply) {
        ExpertApplyDetailResponse applyDetail = ExpertApplyDetailResponse.builder()
                .applyId(expertApply.getId())
                .applyContents(expertApply.getContents())
                .applierId(expertApply.getWriter().getId())
                .applierNick(expertApply.getWriter().getNick())
                .build();
        applyDetail.setChildData(Optional.ofNullable(expertApply.getChild()));

        return applyDetail;
    }

    public void setChildData(final Optional<Child> optionalChild) {
        optionalChild.ifPresent(child -> {
            this.childId = child.getId();
            this.childName = child.getName();
            this.childBirthday = child.getBirthday();
        });
    }


}
