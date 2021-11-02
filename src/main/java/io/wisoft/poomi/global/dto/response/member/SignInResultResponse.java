package io.wisoft.poomi.global.dto.response.member;

import io.wisoft.poomi.configures.security.jwt.JwtToken;
import io.wisoft.poomi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInResultResponse {

    private SigninResponse signinResponse;

    private JwtToken jwtToken;

    @Builder
    public SignInResultResponse(final SigninResponse signinResponse,
                                final JwtToken jwtToken) {
        this.signinResponse = signinResponse;
        this.jwtToken = jwtToken;
    }

    public static SignInResultResponse of(final Member member, final JwtToken jwtToken) {
        return SignInResultResponse.builder()
                .signinResponse(SigninResponse.of(member))
                .jwtToken(jwtToken)
                .build();
    }

}
