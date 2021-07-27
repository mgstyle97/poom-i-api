package io.wisoft.poomi.bind.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private String to;
    private String content;

}
