package com.project.studytogether.jwt;

import com.project.studytogether.entity.User;
import com.project.studytogether.entity.enums.UserMethod;
import com.project.studytogether.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userEntity = userRepository.findByEmailAndMethod(username, UserMethod.일반);

        if (userEntity == null) {

            throw new UsernameNotFoundException("UsernameNotFoundException");
        }
        return new CustomUserDetails(userEntity);
    }
}
