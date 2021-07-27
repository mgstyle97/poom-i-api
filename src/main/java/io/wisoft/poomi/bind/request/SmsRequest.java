package io.wisoft.poomi.bind.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsRequest {

    private String type;
    private String contentType;
    private String countryCode;
    private String from;
    private String content;
    private List<MessageRequest> messages;

}
