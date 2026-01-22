package com.rcal.people.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "people_skills")
public class Skill{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "skill_id")
  private Integer skillId;

  @Column(name = "skill_name")
  private String skillName;

  @Column(name = "skill_description")
  private String skillDescription;
}
