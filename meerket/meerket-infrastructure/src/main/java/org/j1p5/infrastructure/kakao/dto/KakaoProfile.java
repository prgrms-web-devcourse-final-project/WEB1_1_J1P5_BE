package org.j1p5.infrastructure.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.j1p5.domain.auth.dto.OauthProfile;

public class KakaoProfile implements OauthProfile {

    @JsonProperty("id")
    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    @Getter
    public static class KakaoAccount {
        @JsonProperty("email")
        private String email;
    }
}
