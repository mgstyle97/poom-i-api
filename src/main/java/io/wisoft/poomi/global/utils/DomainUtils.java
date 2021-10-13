package io.wisoft.poomi.global.utils;

import javax.servlet.http.HttpServletRequest;

public class DomainUtils {

    public static String generateDomainByRequest(final HttpServletRequest request) {
        return request
                .getRequestURL()
                .toString()
                .replace(request.getRequestURI(), "");
    }

}
