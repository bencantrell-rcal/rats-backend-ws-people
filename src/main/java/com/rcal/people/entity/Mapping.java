package com.rcal.people.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "people_mappings")
public class Mapping{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "from_entity_id", nullable = false)
  private String fromEntityId;

  @Column(name = "from_entity_type", nullable = false)
  private String fromEntityType;

  @Column(name = "to_entity_id", nullable = false)
  private String toEntityId;

  @Column(name = "to_entity_type", nullable = false)
  private String toEntityType;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate(){
    LocalDateTime now = LocalDateTime.now();
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate(){
    updatedAt = LocalDateTime.now();
  }
}