package com.example.feelsun.config.auth;

import com.example.feelsun.domain.User;
import com.example.feelsun.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class PrincipalUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userJpaRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return null;
        }
        User user = optionalUser.get();
        log.info("user : " + user);
        return new PrincipalUserDetails(user);

    }

}
