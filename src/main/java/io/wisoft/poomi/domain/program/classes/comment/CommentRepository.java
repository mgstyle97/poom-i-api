package io.wisoft.poomi.domain.program.classes.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    default Comment getCommentById(final Long commentId) {
        return this.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("No comment data by comment id")
        );
    }

}
