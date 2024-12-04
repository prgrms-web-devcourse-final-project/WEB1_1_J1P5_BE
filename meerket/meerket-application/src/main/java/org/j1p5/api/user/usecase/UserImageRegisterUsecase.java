package org.j1p5.api.user.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.user.service.UserRegisterService;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class UserImageRegisterUsecase {

    private final UserRegisterService userRegisterService;

    public void execute(Long userId, File imageFile) {
        userRegisterService.updateProfile(userId, imageFile);
    }
}