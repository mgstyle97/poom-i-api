package io.wisoft.poomi.domain.member;

import io.wisoft.poomi.domain.member.exception.DuplicateMemberException;
import io.wisoft.poomi.response.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("io.wisoft.poomi.domain.member")
public class MemberExceptionHandler {

    @ExceptionHandler(DuplicateMemberException.class)
    public ApiResult<?> duplicateMember(DuplicateMemberException ex) {
        return ApiResult.get(HttpStatus.CONFLICT, ex.getMember(), "Duplicate member data exists");
    }

}
