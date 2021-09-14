package io.wisoft.poomi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.poomi.bind.dto.member.sms.SmsResultDto;
import io.wisoft.poomi.bind.dto.member.sms.SmsVerifyDto;
import io.wisoft.poomi.bind.request.member.sms.MessageRequest;
import io.wisoft.poomi.bind.request.member.sms.SmsRequest;
import io.wisoft.poomi.bind.request.member.sms.SmsVerifyRequest;
import io.wisoft.poomi.common.property.NCSProperty;
import io.wisoft.poomi.domain.member.sms.SmsCertification;
import io.wisoft.poomi.domain.member.sms.SmsCertificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsService {

    private final NCSProperty ncsProperty;
    private final SmsCertificationRepository smsCertificationRepository;

    public SmsResultDto sendSms(String phoneNumber) throws JsonProcessingException, URISyntaxException {
        String certificationNumber = RandomStringUtils.randomNumeric(6);
        smsCertificationRepository.save(
                SmsCertification.builder()
                        .phoneNumber(phoneNumber)
                        .certificationNumber(certificationNumber)
                        .build()
        );

        log.info("Save phone_number temporarily: {}", phoneNumber);

        String content = "[POOM-i] 인증번호 [" + certificationNumber + "]을 입력해주세요.";
        List<MessageRequest> messages = new ArrayList<>();
        messages.add(new MessageRequest(phoneNumber, content));

        log.info("Generate message: {}", messages);

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

        log.info("Generate request: {}", jsonBody);

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
                        () -> new IllegalArgumentException("No phone number data")
                );

        log.info("Generate certification data of phone number: {}", verifyRequest.getPhoneNumber());

        smsCertification.verifyCertificationNumber(verifyRequest.getCertificationNumber());
        smsCertificationRepository.delete(smsCertification);

        log.info("Successful verify of phone number: {}", verifyRequest.getPhoneNumber());

        return new SmsVerifyDto(verifyRequest.getPhoneNumber());
    }

    private void setHeaders(HttpHeaders headers, Long time) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", ncsProperty.getAccessKey());
    }

    private String generateSignature(Long time) {
        String space = " ";					// one space
        String newLine = "\n";					// new line
        String method = "POST";					// method
        String url = "/sms/v2/services/" + ncsProperty.getServiceId() + "/messages";	// url (include query string)
        String timestamp = time.toString();			// current timestamp (epoch)
        String accessKey = ncsProperty.getAccessKey();			// access key id (from portal or Sub Account)
        String secretKey = ncsProperty.getSecretKey();

        String message = method +
                space +
                url +
                newLine +
                timestamp +
                newLine +
                accessKey;

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
