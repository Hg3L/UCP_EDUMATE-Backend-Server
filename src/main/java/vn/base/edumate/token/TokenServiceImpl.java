package vn.base.edumate.token;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.base.edumate.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService{

    private final TokenRepository tokenRepository;

    private final UserService userService;

    @Transactional
    @Override
    public void saveToken(Token token) {
        tokenRepository.save(token);
        log.info("Token saved successfully");
    }

    @Override
    public Token getToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));
    }

    @Override
    public void saveAllToken(List<Token> tokens) {
        tokenRepository.saveAll(tokens);
        log.info("List token saved successfully");
    }

    @Override
    public List<Token> getAllValidTokenByUserId(String uid) {
        return tokenRepository.findValidTokenByUserId(uid);
    }

}
