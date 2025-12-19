package com.rcal.people.repository.read;

import com.rcal.people.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReadUserSessionRepository
    extends
      JpaRepository<UserSession, String>{
  Optional<UserSession> findBySessionId(String sessionId);
}
