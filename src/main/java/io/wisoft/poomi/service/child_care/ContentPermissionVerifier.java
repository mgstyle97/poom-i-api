package io.wisoft.poomi.service.child_care;

import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.global.exception.exceptions.NoPermissionOfContentException;
import org.springframework.stereotype.Service;

@Service
public class ContentPermissionVerifier {

    public static void verifyModifyPermission(final Member approved, final Member unapproved) {
        if (!approved.equals(unapproved)) {
            throw new NoPermissionOfContentException();
        }
    }

}
