package io.wisoft.poomi.global.dto.request.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private String to;
    private String content;

}
