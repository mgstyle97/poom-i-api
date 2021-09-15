package io.wisoft.poomi.common.error;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.utils.ErrorNotificationUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Aspect
public class SlackNotificationAspect {

    private final ErrorNotificationUtils errorNotificationUtils;

    @AfterReturning(value = "execution(* io.wisoft.poomi.common.error.RestExceptionHandler.*(..))", returning = "errorResponse")
    public ApiResponse<ErrorResponse> notify(final JoinPoint joinPoint, final ApiResponse<ErrorResponse> errorResponse) {
        errorNotificationUtils.sendErrorInfo2Slack(errorResponse.getError().getMessage());

        return errorResponse;
    }

}
