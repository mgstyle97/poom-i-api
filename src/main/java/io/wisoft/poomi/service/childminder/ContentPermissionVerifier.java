package io.wisoft.poomi.service.childminder;

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
    
    public static void verifyAccessPermission(final AddressTag approved, final AddressTag unapproved) {
        if (!approved.equals(unapproved)) {
            throw new NoPermissionOfContentException();
        }
    }

}
