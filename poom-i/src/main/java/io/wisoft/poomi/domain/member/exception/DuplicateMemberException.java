package io.wisoft.poomi.domain.member.exception;

import io.wisoft.poomi.domain.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DuplicateMemberException extends RuntimeException{

    private final Member member;

}
