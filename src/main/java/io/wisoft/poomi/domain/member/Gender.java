package io.wisoft.poomi.domain.member;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender getGender(final String gender) {
        return Gender.valueOf(gender);
    }
}
