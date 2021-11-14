package io.wisoft.poomi.domain.child_care.playground.vote;

import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaygroundVoteRepository extends JpaRepository<PlaygroundVote, Long> {

    default PlaygroundVote getById(final Long voteId) {
        return this.findById(voteId).orElseThrow(
                () -> new NotFoundEntityDataException("vote id: " + voteId + " 에 대한 데이터를 찾지 못했습니다.")
        );
    }

}
