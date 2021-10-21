package io.wisoft.poomi.global.dto.response.child_care.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.child_care.group.board.GroupBoard;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@Setter
public class GroupBoardRegisterResponse {

    @JsonProperty("board_id")
    private Long boardId;

    private String writer;

    @JsonProperty("requested_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestedAt;

    public GroupBoardRegisterResponse(final GroupBoard board) {
        this.boardId = board.getId();
        this.writer = board.getWriter().getNick();
        this.requestedAt = new Date();
    }

}
