package io.wisoft.poomi.global.dto.response.child_care.playground.vote;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.playground.vote.ExpiredStatus;
import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.file.UploadFile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class PlaygroundVoteLookupResponse {

    @JsonProperty("vote_id")
    private Long voteId;

    @JsonProperty("registrant")
    private String registrant;

    @JsonProperty("address")
    private String address;

    @JsonProperty("detail_address")
    private String detailAddress;

    @JsonProperty("purpose_using")
    private String purposeUsing;

    @JsonProperty("expired_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiredAt;

    @JsonProperty("expired_status")
    private String expiredStatus;

    @JsonProperty("image_uris")
    private List<String> imageURIs;

    @JsonProperty("voting_yet_list")
    Map<String, List<String>> notVotingList;

    @Builder
    public PlaygroundVoteLookupResponse(final Long voteId,
                                        final String purposeUsing, final String address, final String detailAddress,
                                        final Date expiredAt, final ExpiredStatus expiredStatus,
                                        final Set<UploadFile> images, final String registrant,
                                        final Map<String, List<String>> notVotingList) {
        this.voteId = voteId;
        this.purposeUsing = purposeUsing;
        this.address = address;
        this.detailAddress = detailAddress;
        this.expiredAt = expiredAt;
        this.expiredStatus = expiredStatus.toString();
        this.imageURIs = images.stream()
                .map(UploadFile::getFileAccessURI)
                .collect(Collectors.toList());
        this.registrant = registrant;
        this.notVotingList = notVotingList;
    }

    public static PlaygroundVoteLookupResponse of(final PlaygroundVote playgroundVote, final Date expiredAt) {
        return PlaygroundVoteLookupResponse.builder()
                .voteId(playgroundVote.getId())
                .purposeUsing(playgroundVote.getPurposeUsing())
                .address(playgroundVote.getAddress().getAddress())
                .detailAddress(playgroundVote.getAddress().getDetailAddress())
                .expiredAt(expiredAt)
                .expiredStatus(playgroundVote.getExpiredStatus())
                .images(playgroundVote.getImages())
                .registrant(playgroundVote.getRegistrant().getNick())
                .notVotingList(playgroundVote.getNotVotingDongAndHo())
                .build();
    }

}
