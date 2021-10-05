package io.wisoft.poomi.domain.child_care.group.comment;

import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    default Comment getCommentById(final Long commentId) {
        return this.findById(commentId).orElseThrow(
                () -> new NotFoundEntityDataException("comment id: " + commentId + "에 관한 데이터를 찾지 못했습니다.")
        );
    }

}
