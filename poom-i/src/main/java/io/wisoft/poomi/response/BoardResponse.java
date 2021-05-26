package io.wisoft.poomi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.poomi.domain.board.Board;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class BoardResponse {

    @JsonProperty("title")
    private String title;

    @JsonProperty("contents")
    private String contents;

    @JsonProperty("writer")
    private String writer;

    @JsonProperty("views")
    private int views;

    private BoardResponse(final Board board) {
        BeanUtils.copyProperties(board, this);
        this.writer = board.getMember().getNick();
    }

    public static BoardResponse get(final Board board) {
        return new BoardResponse(board);
    }

}
