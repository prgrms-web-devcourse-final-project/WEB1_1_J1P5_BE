package org.j1p5.infrastructure.kakao;

import org.j1p5.domain.auth.dto.KakaoToken;
import org.j1p5.infrastructure.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakao-auth-client", url = "https://kauth.kakao.com", configuration = FeignClientConfig.class)
public interface KakaoAuthClient {

    @PostMapping("/oauth/token")
    KakaoToken getToken(
            @RequestParam(name = "grant_type") String type,
            @RequestParam(name = "client_id") String clientId,
            @RequestParam(name = "redirect_uri") String redirectUri,
            @RequestParam(name = "code") String code
    );
}
