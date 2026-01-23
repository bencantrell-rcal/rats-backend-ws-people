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
  private Long fromEntityId;

  @Column(name = "from_entity_type", nullable = false)
  private String fromEntityType;

  @Column(name = "to_entity_id", nullable = false)
  private Long toEntityId;

  @Column(name = "to_entity_type", nullable = false)
  private String toEntityType;
}