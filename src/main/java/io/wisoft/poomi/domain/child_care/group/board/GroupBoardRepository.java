package io.wisoft.poomi.domain.child_care.group.board;

import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupBoardRepository extends JpaRepository<GroupBoard, Long> {

    default GroupBoard getBoardById(final Long boardId) {
        return this.findById(boardId).orElseThrow(
                () -> new NotFoundEntityDataException("board id" + boardId + "에 관한 데이터를 찾지 못했습니다.")
        );
    }

}
