package vn.base.edumate.token;

import java.util.List;

public interface TokenService {

    void saveToken(Token token);

    Token getToken(String token);

    void saveAllToken(List<Token> tokens);

    List<Token> getAllValidTokenByUserId(String uid);

}
