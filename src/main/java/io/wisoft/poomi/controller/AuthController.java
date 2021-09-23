package io.wisoft.poomi.controller;

import io.wisoft.poomi.global.dto.response.ApiResponse;
import io.wisoft.poomi.global.dto.response.auth.SmsResultResponse;
import io.wisoft.poomi.global.dto.response.auth.SmsVerifyResponse;
import io.wisoft.poomi.global.dto.request.auth.MailSendRequest;
import io.wisoft.poomi.global.dto.request.auth.MailVerifyRequest;
import io.wisoft.poomi.global.dto.request.auth.SmsSendRequest;
import io.wisoft.poomi.global.dto.request.auth.SmsVerifyRequest;
import io.wisoft.poomi.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sms-certification/send")
    public ApiResponse<SmsResultResponse> sendCertificationNumberToPhone(
            @RequestBody @Valid final SmsSendRequest smsSendRequest) {
        return ApiResponse.succeed(HttpStatus.CREATED, authService.sendSmsToPhoneNumber(smsSendRequest));
    }

    @PostMapping("/sms-certification/verify")
    public ApiResponse<SmsVerifyResponse> verifyCertificationNumberToPhone(
            @RequestBody @Valid final SmsVerifyRequest smsVerifyRequest) {
        return ApiResponse.succeed(HttpStatus.OK, authService.verifyToPhoneNumber(smsVerifyRequest));
    }

    @PostMapping("/mail-certification/send")
    public void sendCertificationNumberToMail(
            @RequestBody @Valid final MailSendRequest mailSendRequest) {
        authService.sendMailToMailAddress(mailSendRequest);
    }

    @PostMapping("/mail-certification/verify")
    public void verifyCertificationNumberToMail(
            @RequestBody @Valid final MailVerifyRequest mailVerifyRequest) {
        authService.verifyMailToMailAddress(mailVerifyRequest);
    }

}
