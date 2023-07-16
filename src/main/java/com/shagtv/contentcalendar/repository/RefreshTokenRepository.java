package com.shagtv.contentcalendar.repository;

import com.shagtv.contentcalendar.model.RefreshToken;
import com.shagtv.contentcalendar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
