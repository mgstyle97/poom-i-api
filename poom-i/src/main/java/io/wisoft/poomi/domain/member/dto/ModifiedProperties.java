package io.wisoft.poomi.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class ModifiedProperties {

    private String name;
    private String phoneNumber;
    private String password;
    private String nick;
    private String address;
    private boolean isBabysitter;

}
