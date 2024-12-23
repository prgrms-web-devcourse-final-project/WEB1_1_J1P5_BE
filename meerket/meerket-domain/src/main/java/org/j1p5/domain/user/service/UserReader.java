package org.j1p5.domain.user.service;

import static org.j1p5.domain.global.exception.DomainErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.global.exception.DomainException;
import org.j1p5.domain.user.entity.Provider;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {

    private final UserRepository userRepository;

    public UserEntity read(String socialId, String provider) {
        return userRepository
                .findBySocialIdAndProvider(socialId, Provider.valueOf(provider))
                .orElse(null);
    }

    public UserEntity getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new DomainException(USER_NOT_FOUND));
    }
}
