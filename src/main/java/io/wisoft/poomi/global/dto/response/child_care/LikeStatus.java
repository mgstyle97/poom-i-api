package io.wisoft.poomi.global.dto.response.child_care;

import io.wisoft.poomi.domain.member.Member;

import java.util.Set;

public enum LikeStatus {
    LIKE, NOT_LIKE;

    public static LikeStatus generateLikeStatue(final Set<Member> likes, final Member member) {
        if (likes.contains(member)) {
            return LIKE;
        }
        return NOT_LIKE;
    }

}
