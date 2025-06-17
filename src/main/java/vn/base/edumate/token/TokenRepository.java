package vn.base.edumate.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.base.edumate.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Query("select t from Token t " +
            "where t.user.id = :uid " +
            "and (t.expired = false or t.revoked = false )")
    List<Token> findValidTokenByUserId(String uid);
}
