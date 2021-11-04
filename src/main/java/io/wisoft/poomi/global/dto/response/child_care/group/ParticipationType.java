package io.wisoft.poomi.global.dto.response.child_care.group;

import io.wisoft.poomi.domain.member.Member;

public enum ParticipationType {
    MANAGE, PARTICIPATION;

    public static ParticipationType getParticipationTypeByIsWriter(final Member writer, final Member member) {
        if (writer.equals(member)) {
            return MANAGE;
        }

        return PARTICIPATION;
    }
}
