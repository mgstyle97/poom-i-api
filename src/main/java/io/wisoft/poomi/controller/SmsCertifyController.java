package io.wisoft.poomi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.SmsResultDto;
import io.wisoft.poomi.bind.dto.SmsVerifyDto;
import io.wisoft.poomi.bind.request.SmsSendRequest;
import io.wisoft.poomi.bind.request.SmsVerifyRequest;
import io.wisoft.poomi.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SmsCertifyController {

    private final SmsService smsService;

    @PostMapping("/sms-certification/send")
    public ApiResponse<SmsResultDto> sendCertificationNumber(@RequestBody @Valid SmsSendRequest smsSendRequest) {
        try {
            return ApiResponse.succeed(smsService.sendSms(smsSendRequest.getPhoneNumber()));
        } catch (JsonProcessingException | URISyntaxException e) {
            throw new IllegalArgumentException();
        }
    }

    @PostMapping("/sms-certification/verify")
    public ApiResponse<SmsVerifyDto> verifyCertificationNumber(@RequestBody @Valid SmsVerifyRequest smsVerifyRequest) {
        return ApiResponse.succeed(smsService.verify(smsVerifyRequest));
    }

}
