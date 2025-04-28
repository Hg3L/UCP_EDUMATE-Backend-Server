package vn.base.edumate.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.base.edumate.common.exception.ErrorCode;
import vn.base.edumate.common.exception.ResourceNotFoundException;
import vn.base.edumate.user.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", username);
                    return new ResourceNotFoundException(ErrorCode.USER_NOT_EXISTED);
                });
    }
}
