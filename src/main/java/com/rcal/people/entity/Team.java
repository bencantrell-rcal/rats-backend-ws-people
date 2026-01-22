package com.rcal.people.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "people_teams")
public class Team{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "team_id")
  private Integer teamId;

  @Column(name = "team_name")
  private String teamName;

  @Column(name = "team_description")
  private String teamDescription;
}
