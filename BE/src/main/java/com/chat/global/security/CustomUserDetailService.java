package com.chat.global.security;

import com.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username)
                .orElseThrow(NotFoundException::new);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
