package io.wisoft.poomi.bind.request.member.sms;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private String to;
    private String content;

}
