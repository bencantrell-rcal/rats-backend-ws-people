package com.rcal.people.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_session")
public class UserSession{

  @Id
  @Column(name = "session_id", length = 36, nullable = false)
  private String sessionId;

  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Timestamp createdAt;

  @Column(name = "expires_at", nullable = false)
  private Timestamp expiresAt;

  @PrePersist
  public void prePersist(){
    if (sessionId == null){
      sessionId = UUID.randomUUID().toString();
    }
    if (createdAt == null){
      createdAt = new Timestamp(System.currentTimeMillis());
    }
  }

  public boolean isExpired(){
    return expiresAt.before(new Timestamp(System.currentTimeMillis()));
  }
}
