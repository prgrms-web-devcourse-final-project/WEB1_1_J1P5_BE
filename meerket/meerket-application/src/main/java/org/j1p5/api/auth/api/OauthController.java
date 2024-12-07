package org.j1p5.api.auth.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.auth.AuthManager;
import org.j1p5.api.auth.dto.LoginRequest;
import org.j1p5.api.auth.dto.LoginResponse;
import org.j1p5.api.auth.dto.SessionInfo;
import org.j1p5.api.auth.usecase.OauthLoginUsecase;
import org.j1p5.api.global.response.Response;
import org.j1p5.domain.user.UserInfo;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OauthController {

    private final OauthLoginUsecase oauthLoginUsecase;
    private final AuthManager authManager;
    private final HttpSessionSecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    @PostMapping
    public Response<LoginResponse> login(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        UserInfo user = oauthLoginUsecase.login(loginRequest.code(), loginRequest.provider());

        SecurityContext context = authManager.setContext(SessionInfo.of(user.pk(), user.role()));
        securityContextRepository.saveContext(context, request, response);

        return Response.onSuccess(LoginResponse.from(user));
    }

    @PostMapping("/logout")
    public Response<Void> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return Response.onSuccess();
    }
}
