package io.wisoft.poomi.global.aop.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class SlackNotificationAspect {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${slack.url}")
    private String slackUrl;

    @Value("${slack.token}")
    private String slackToken;

    @Value("${slack.channel}")
    private String slackChannel;

    @AfterReturning(
            value = "execution(* io.wisoft.poomi.global.exception.RestExceptionHandler.*(..))",
            returning = "errorResponse"
    )
    public ApiResponse<ErrorResponse> notify(
            final JoinPoint joinPoint,
            final ApiResponse<ErrorResponse> errorResponse) {
        sendErrorInfo2Slack(errorResponse.getError().getMessage());

        return errorResponse;
    }

    private void sendErrorInfo2Slack(final String errorMessage) {
        final HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + slackToken);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> messageBody = new HashMap<>();
        messageBody.put("channel", slackChannel);
        messageBody.put("text",
                "[Poom-i]\n" + getCurrentTime() + "\n" + "error_message: " + errorMessage +
                        "\nauthorization: " + request.getHeader("AUTHORIZATION") +
                        "\nhttp_method: " + request.getMethod() +
                        "\nrequest_url: " + request.getRequestURL() +
                        "\nclient_ip: " + getClientIP(request));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(messageBody, headers);

        restTemplate.exchange(slackUrl, HttpMethod.POST, entity, String.class);

    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");

        return formatter.format(new Date());
    }

    private String getClientIP(final HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (!StringUtils.hasText(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

}
