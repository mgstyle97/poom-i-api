package io.wisoft.poomi.service.auth.certification;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.domain.auth.residence.ResidenceCertification;
import io.wisoft.poomi.domain.auth.residence.ResidenceCertificationRepository;
import io.wisoft.poomi.domain.child_care.expert.ChildCareExpert;
import io.wisoft.poomi.domain.child_care.expert.RecruitType;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.file.UploadFileRepository;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.child.Child;
import io.wisoft.poomi.global.dto.response.auth.SmsResultResponse;
import io.wisoft.poomi.global.dto.response.auth.SmsVerifyResponse;
import io.wisoft.poomi.global.dto.request.auth.*;
import io.wisoft.poomi.global.oauth2.properties.sms.NCSProperty;
import io.wisoft.poomi.domain.auth.property.email.EmailCertification;
import io.wisoft.poomi.domain.auth.property.email.EmailCertificationRepository;
import io.wisoft.poomi.domain.auth.property.sms.SmsCertification;
import io.wisoft.poomi.domain.auth.property.sms.SmsCertificationRepository;
import io.wisoft.poomi.global.utils.LocalDateTimeUtils;
import io.wisoft.poomi.global.utils.UploadFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CertificationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    private final NCSProperty ncsProperty;
    private final SmsCertificationRepository smsCertificationRepository;

    private final UploadFileUtils uploadFileUtils;
    private final UploadFileRepository uploadFileRepository;
    private final ResidenceCertificationRepository residenceCertificationRepository;

    @Value("${spring.mail.username}")
    private static String FROM_ADDRESS;
    private final JavaMailSender javaMailSender;
    private final EmailCertificationRepository emailCertificationRepository;

    @Transactional
    public SmsResultResponse sendSmsToPhoneNumber(final SmsSendRequest smsSendRequest) {
        String phoneNumber = smsSendRequest.getPhoneNumber();
        String certificationNumber = generateCertificationNumber();
        String content = generateCertificationMessage(certificationNumber);

        List<MessageRequest> messages = Collections
                .singletonList(new MessageRequest(phoneNumber, content));
        log.info("Generate message: {}", messages);

        String requestBody = generateRequestBody(messages);
        SmsResultResponse smsResultResponse = sendSmsRequest(requestBody);

        smsCertificationRepository.save(
                SmsCertification.of(
                        phoneNumber, certificationNumber,
                        jwtTokenProvider.generatePropertyValidationToken()
                )
        );
        log.info("Save phone number and certification number temporarily: {}", phoneNumber);

        deleteAllSmsExpiredEntity();

        return smsResultResponse;
    }

    @Transactional
    public SmsVerifyResponse verifyToPhoneNumber(SmsVerifyRequest verifyRequest) {
        SmsCertification smsCertification = smsCertificationRepository
                .getByPhoneNumber(verifyRequest.getPhoneNumber());
        log.info("Generate certification data of phone number: {}", verifyRequest.getPhoneNumber());

        smsCertification.verifyCertificationNumber(verifyRequest.getCertificationNumber());
        log.info("Successful verify of phone number: {}", verifyRequest.getPhoneNumber());

        smsCertificationRepository.delete(smsCertification);
        log.info("Delete verified phone number: {}", smsCertification.getPhoneNumber());

        deleteAllSmsExpiredEntity();

        return new SmsVerifyResponse(verifyRequest.getPhoneNumber());
    }

    @Transactional
    public void sendMailToMailAddress(final MailSendRequest mailSendRequest) {
        String certificationNumber = generateCertificationNumber();
        String content = generateCertificationMessage(certificationNumber);

        sendMessageByEmail(mailSendRequest.getEmail(), "[POOM-i] 이메일 인증 안내", content);

        emailCertificationRepository.save(
                EmailCertification.of(
                        mailSendRequest.getEmail(), certificationNumber,
                        jwtTokenProvider.generatePropertyValidationToken())
        );

        deleteAllMailExpiredEntity();
    }

    @Transactional
    public void verifyMailToMailAddress(final MailVerifyRequest mailVerifyRequest) {
        EmailCertification emailCertification = emailCertificationRepository
                .getByEmail(mailVerifyRequest.getEmail());
        log.info("Generate certification data of email: {}", mailVerifyRequest.getEmail());

        emailCertification.verifyCertificationNumber(mailVerifyRequest.getCertificationNumber());
        log.info("Verify success of email: {}", mailVerifyRequest.getEmail());

        emailCertificationRepository.delete(emailCertification);
        log.info("Delete verified email: {}", emailCertification.getEmail());

        deleteAllMailExpiredEntity();
    }

    public void sendMailOfSignupApproved(final String account) {
        final String approvedMessage =
                "[POOMI-i]" + account + "님의 회원가입이 승인되었습니다.";

        sendMessageByEmail(account, "[POOM-i] 회원가입 안내", approvedMessage);
    }

    public void sendSmsOfExpertApproved(final ChildCareExpert expert) {
        sendSmsOfExpertApproved(expert, expert.getManager());
        sendSmsOfExpertApproved(expert, expert.getCaringChild().getParent());
    }

    private void sendSmsOfExpertApproved(final ChildCareExpert expert, final Member member) {
        final String message = generateMessageOfExpertByMember(expert, member);
        String phoneNumber = member.getPhoneNumber();

        List<MessageRequest> messages = Collections
                .singletonList(new MessageRequest(phoneNumber, message));
        log.info("Generate message: {}", messages);

        String requestBody = generateLMSRequestBody(messages);
        sendSmsRequest(requestBody);

    }

    @Transactional
    public void registerResidenceCertification(
            final Member member,
            final String residenceCertificationFileData) {
        UploadFile residenceFile = uploadFileUtils.saveFileAndConvertImage(residenceCertificationFileData);
        uploadFileRepository.save(residenceFile);
        log.info("Upload S3 Residence file id: {}", residenceFile.getId());

        ResidenceCertification residenceCertification = ResidenceCertification.builder()
                .member(member)
                .residenceFile(residenceFile)
                .build();
        residenceCertificationRepository.save(residenceCertification);
        log.info("Save Residence Certification id: {}", residenceCertification.getId());

        member.setResidenceCertification(residenceCertification);
        memberRepository.save(member);

    }

    private String generateCertificationNumber() {
        String certificationNumber = RandomStringUtils.randomNumeric(6);
        log.info("Generate certification number: {}", certificationNumber);

        return certificationNumber;
    }

    private String generateCertificationMessage(final String certificationNumber) {
        return "[POOM-i] 인증번호 [" + certificationNumber + "]을 입력해주세요.";
    }

    private String generateMessageOfExpertManager(final ChildCareExpert expert) {
        return "[POOM-i]" +
                "\n품앗이가 매칭되었습니다." +
                "\n보호자 닉네임 : " + expert.getCaringChild().getParent().getNick() +
                "\n보호자 전화번호 : " + expert.getCaringChild().getParent().getPhoneNumber() +
                "\n자녀 이름 : " +
                expert.getCaringChild().getName() +
                "(" + new SimpleDateFormat("yyyy.MM.dd").format(expert.getCaringChild().getBirthday()) + ")" +
                "\n활동 시간 : " +
                LocalDateTimeUtils
                        .convertToString("yyyy.MM.dd HH:mm:ss", expert.getStartTime()) + " ~ " +
                LocalDateTimeUtils
                        .convertToString("yyyy.MM.dd HH:mm:ss", expert.getEndTime());
    }

    private String generateMessageOfExpertParent(final ChildCareExpert expert) {
        Child managerChild = null;
        if (expert.getRecruitType().equals(RecruitType.RECRUIT)) {
            managerChild = expert.getCaringChild();
        } else {
            managerChild = expert.getWriterChild();
        }

        return "[POOM-i]" +
                "\n품앗이가 매칭되었습니다." +
                "\n품앗이꾼 닉네임 : " + expert.getManager().getNick() +
                "\n품앗이꾼 전화번호 : " + expert.getManager().getPhoneNumber() +
                generateChildInfo(managerChild) +
                "\n활동 시간 : " +
                LocalDateTimeUtils
                        .convertToString("yyyy.MM.dd HH:mm:ss", expert.getStartTime()) + " ~ " +
                LocalDateTimeUtils
                        .convertToString("yyyy.MM.dd HH:mm:ss", expert.getEndTime());
    }

    private String generateChildInfo(final Child child) {
        if (child != null) {
            return "\n 품앗이꾼 자녀 이름 : " +
                    child.getName() +
                    "(" + new SimpleDateFormat("yyyy.MM.dd").format(child.getBirthday()) + ")";
        }
        return "\n";
    }

    private String generateMessageOfExpertByMember(final ChildCareExpert expert, final Member member) {
        if (expert.getManager().equals(member)) {
            return generateMessageOfExpertManager(expert);
        }
        return generateMessageOfExpertParent(expert);
    }

    private void setHeaders(HttpHeaders headers) {
        Long time = System.currentTimeMillis();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", ncsProperty.getAccessKey());

        String signature = generateSignature(time);
        headers.set("x-ncp-apigw-signature-v2", signature);
    }

    private String generateSignature(Long time) {
        String space = " ";                    // one space
        String newLine = "\n";                    // new line
        String method = "POST";                    // method
        String url = "/sms/v2/services/" + ncsProperty.getServiceId() + "/messages";    // url (include query string)
        String timestamp = time.toString();            // current timestamp (epoch)
        String accessKey = ncsProperty.getAccessKey();            // access key id (from portal or Sub Account)
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
            log.error("Error message: {}", e.getMessage());

            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }

    private String generateRequestBody(List<MessageRequest> messages) {
        try {
            SmsRequest smsRequest = SmsRequest.of(ncsProperty.getFrom(), messages);
            String requestBody = smsRequest.toJson();
            log.info("Generate request: {}", requestBody);

            return requestBody;
        } catch (JsonProcessingException e) {
            log.error("Json Processing Error message: {}", e.getMessage());

            throw new IllegalArgumentException("SMS 요청 바디를 생성하는 데 실패했습니다.");
        }

    }

    private String generateLMSRequestBody(List<MessageRequest> messages) {
        try {
            SmsRequest smsRequest = SmsRequest.LMS(ncsProperty.getFrom(), messages);

            return smsRequest.toJson();
        } catch (JsonProcessingException e) {
            log.error("Json Processing Error message: {}", e.getMessage());

            throw new IllegalArgumentException("LMS 요청 바디를 생성하는 데 실패했습니다.");
        }
    }

    private SmsResultResponse sendSmsRequest(final String requestBody) {
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        SmsResultResponse smsResultResponse = restTemplate.postForObject(
                "https://sens.apigw.ntruss.com/sms/v2/services/" + ncsProperty.getServiceId() + "/messages",
                httpEntity, SmsResultResponse.class
        );

        return smsResultResponse;
    }

    private void sendMessageByEmail(final String email, final String subject, final String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(FROM_ADDRESS);
        message.setSubject(subject);
        message.setText(content);

        javaMailSender.send(message);
    }

    private void deleteAllSmsExpiredEntity() {
        List<SmsCertification> smsCertificationList = smsCertificationRepository.findAll();
        smsCertificationRepository.deleteAll(
                smsCertificationList.stream()
                        .filter(smsCertification -> expiredCertificationTime(smsCertification.getExpiredValidationToken()))
                        .collect(Collectors.toSet())
        );
        log.info("Delete All expired sms certification");
    }

    private void deleteAllMailExpiredEntity() {
        List<EmailCertification> emailCertificationList = emailCertificationRepository.findAll();
        emailCertificationRepository.deleteAll(
                emailCertificationList.stream()
                        .filter(emailCertification -> expiredCertificationTime(emailCertification.getExpiredValidationToken()))
                        .collect(Collectors.toSet())
        );
        log.info("Delete All expired email certification");
    }

    private boolean expiredCertificationTime(final String expirationToken) {
        try {
            jwtTokenProvider.validateToken(expirationToken);
        } catch (ExpiredJwtException e) {
            return true;
        }

        return false;
    }

}
