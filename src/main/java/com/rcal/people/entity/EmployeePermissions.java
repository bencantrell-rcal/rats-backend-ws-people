package com.rcal.people.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "employees_permissions")
public class EmployeePermissions{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "Permission", nullable = false)
  private String permission;

  @Column(name = "eid", nullable = false)
  private Integer eid;

}