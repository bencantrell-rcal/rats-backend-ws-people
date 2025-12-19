package com.rcal.people.repository.write;

import com.rcal.people.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Repository
@Transactional("writeTransactionManager")
public interface WriteUserSessionRepository
    extends
      JpaRepository<UserSession, String>{

  @Modifying
  @Transactional
  @Query("DELETE FROM UserSession s WHERE s.expiresAt < :time")
  void deleteByExpiresAtBefore(@Param("time") Timestamp time);
}
