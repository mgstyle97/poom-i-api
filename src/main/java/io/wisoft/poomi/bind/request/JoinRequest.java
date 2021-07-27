package io.wisoft.poomi.bind.request;

import io.wisoft.poomi.domain.member.child.Child;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class JoinRequest {

    private String name;
    private int age;
    private String phoneNumber;
    private String email;
    private String loginId;
    private String password;
    private String nick;
    private List<Child> children;

}
