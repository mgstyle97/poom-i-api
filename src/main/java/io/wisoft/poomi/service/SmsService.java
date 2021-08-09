package io.wisoft.poomi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.poomi.bind.dto.SmsResultDto;
import io.wisoft.poomi.bind.dto.SmsVerifyDto;
import io.wisoft.poomi.bind.request.MessageRequest;
import io.wisoft.poomi.bind.request.SmsRequest;
import io.wisoft.poomi.bind.request.SmsVerifyRequest;
import io.wisoft.poomi.common.property.NCSProperty;
import io.wisoft.poomi.domain.member.sms.SmsCertification;
import io.wisoft.poomi.domain.member.sms.SmsCertificationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final NCSProperty ncsProperty;
    private final SmsCertificationRepository smsCertificationRepository;

    @Transactional
    public SmsResultDto sendSms(String phoneNumber) throws JsonProcessingException, URISyntaxException {
        String certificationNumber = RandomStringUtils.randomNumeric(6);
        smsCertificationRepository.save(
                SmsCertification.builder()
                .phoneNumber(phoneNumber)
                .certificationNumber(certificationNumber)
                .build()
        );

        String content = "[POOM-i] 인증번호 [" + certificationNumber + "]을 입력해주세요.";
        List<MessageRequest> messages = new ArrayList<>();
        messages.add(new MessageRequest(phoneNumber, content));

        SmsRequest smsRequest = SmsRequest.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(ncsProperty.getFrom())
                .content("test message")
                .messages(messages)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(smsRequest);

        Long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers, time);

        String signature = generateSignature(time);
        headers.set("x-ncp-apigw-signature-v2", signature);

        HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        SmsResultDto smsResultDto = restTemplate.postForObject(
                new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + ncsProperty.getServiceId() + "/messages"),
                body, SmsResultDto.class
        );

        return smsResultDto;
    }

    @Transactional
    public SmsVerifyDto verify(SmsVerifyRequest verifyRequest) {
        SmsCertification smsCertification =
                smsCertificationRepository.findByPhoneNumber(verifyRequest.getPhoneNumber())
                .orElseThrow(
                        IllegalArgumentException::new
                );
        smsCertification.verifyCertificationNumber(verifyRequest.getCertificationNumber());
        smsCertificationRepository.delete(smsCertification);

        return new SmsVerifyDto(verifyRequest.getPhoneNumber());
    }

    private void setHeaders(HttpHeaders headers, Long time) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", ncsProperty.getAccessKey());
    }

    private String generateSignature(Long time) {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + ncsProperty.getServiceId() + "/messages";
        String timestamp = time.toString();
        String accessKey = ncsProperty.getAccessKey();
        String secretKey = ncsProperty.getSecretKey();

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            String encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

            return encodeBase64String;
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new IllegalArgumentException();
        }
    }

}
