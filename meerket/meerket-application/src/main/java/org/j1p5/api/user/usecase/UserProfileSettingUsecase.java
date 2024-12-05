package org.j1p5.api.user.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.user.service.UserProfileService;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class UserProfileSettingUsecase {

    private final UserProfileService userProfileService;

    public void execute(Long userId, String nickname, File file) {
        if (file != null) {
            userProfileService.updateProfile(userId, file);
        }
        userProfileService.updateNickname(userId, nickname);
    }
}