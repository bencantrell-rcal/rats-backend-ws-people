package com.rcal.people.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "people_roles")
public class Role{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Integer roleId;

  @Column(name = "role_name")
  private String roleName;

  @Column(name = "role_description")
  private String roleDescription;
}
