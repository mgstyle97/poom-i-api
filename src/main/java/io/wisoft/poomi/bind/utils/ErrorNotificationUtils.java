package io.wisoft.poomi.bind.utils;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.common.error.ErrorResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorNotificationUtils {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${web-hook.url}")
    private String webHookUrl;

    public void sendErrorInfo2Slack(final String errorMessage) {
        Map<String, Object> request = new HashMap<>();
        request.put("username", "poom-i-error-bot");
        request.put("text", "[Poom-i]\n" + getCurrentTime() + "\n" + errorMessage);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request);

        restTemplate.exchange(
                webHookUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");

        return formatter.format(new Date());
    }

}
