package io.wisoft.poomi.bind.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorNotificationUtils {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${slack.url}")
    private String slackUrl;

    @Value("${slack.token}")
    private String slackToken;

    @Value("${slack.channel}")
    private String slackChannel;

    public void sendErrorInfo2Slack(final String errorMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + slackToken);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> request = new HashMap<>();
        request.put("channel", slackChannel);
        request.put("text", "[Poom-i]\n" + getCurrentTime() + "\n" + errorMessage);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);


        ResponseEntity<String> response = restTemplate.exchange(slackUrl, HttpMethod.POST, entity, String.class);

    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");

        return formatter.format(new Date());
    }

}
