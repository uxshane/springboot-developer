package me.minkyu.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.minkyu.springbootdeveloper.domain.User;
import me.minkyu.springbootdeveloper.dto.AddUserRequest;
import me.minkyu.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

}
