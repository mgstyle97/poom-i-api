package io.wisoft.poomi.configures.security.oauth;

import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.authority.Authority;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OAuthAttributes(final Map<String, Object> attributes,
                           final String nameAttributeKey,
                           final String name, final String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    public static OAuthAttributes of(final String registrationId,
                                     final String userNameAttributeName,
                                     final Map<String, Object> attributes) {
        switch (registrationId) {
            case "google":
                return ofGoogle(userNameAttributeName, attributes);
            case "facebook":
                return ofFacebook(userNameAttributeName, attributes);
            case "kakao":
                return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(final String userNameAttributeName,
                                            final Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofFacebook(final String userNameAttributeName,
                                              final Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(final String userNameAttributeName,
                                           final Map<String, Object> attributes) {
        String name = (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");
        String email = (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");
        return OAuthAttributes.builder()
                .name(name)
                .email(email)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity(Authority authority) {
        return Member.builder()
                .name(name)
                .email(email)
                .authorities(Collections.singleton(authority))
                .build();
    }

}
