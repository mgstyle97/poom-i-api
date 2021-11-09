package io.wisoft.poomi.controller;

import io.wisoft.poomi.configures.web.resolver.SignInMember;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.global.dto.request.child_care.playground.ResidenceCertificationRegisterRequest;
import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.auth.SmsResultResponse;
import io.wisoft.poomi.global.dto.response.auth.SmsVerifyResponse;
import io.wisoft.poomi.global.dto.request.auth.MailSendRequest;
import io.wisoft.poomi.global.dto.request.auth.MailVerifyRequest;
import io.wisoft.poomi.global.dto.request.auth.SmsSendRequest;
import io.wisoft.poomi.global.dto.request.auth.SmsVerifyRequest;
import io.wisoft.poomi.service.auth.certification.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping("/sms-certification/send")
    public ApiResponse<SmsResultResponse> sendCertificationNumberToPhone(
            @RequestBody @Valid final SmsSendRequest smsSendRequest) {
        return ApiResponse.succeed(
                HttpStatus.CREATED,
                certificationService.sendSmsToPhoneNumber(smsSendRequest));
    }

    @PostMapping("/sms-certification/verify")
    public ApiResponse<SmsVerifyResponse> verifyCertificationNumberToPhone(
            @RequestBody @Valid final SmsVerifyRequest smsVerifyRequest) {
        return ApiResponse.succeed(
                HttpStatus.OK,
                certificationService.verifyToPhoneNumber(smsVerifyRequest));
    }

    @PostMapping("/mail-certification/send")
    public void sendCertificationNumberToMail(
            @RequestBody @Valid final MailSendRequest mailSendRequest) {
        certificationService.sendMailToMailAddress(mailSendRequest);
    }

    @PostMapping("/mail-certification/verify")
    public void verifyCertificationNumberToMail(
            @RequestBody @Valid final MailVerifyRequest mailVerifyRequest) {
        certificationService.verifyMailToMailAddress(mailVerifyRequest);
    }

    @PostMapping("/residence-certification")
    public void registerResidenceCertification(
            @RequestBody @Valid final ResidenceCertificationRegisterRequest registerRequest,
            @SignInMember final Member member) {
        certificationService.registerResidenceCertification(member, registerRequest.getFileData());
    }

}
