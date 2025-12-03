package com.rcal.people.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "employees_permissions")
public class EmployeePermission{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "Permission", nullable = false, length = 255)
  private String permission;

  @Column(name = "eid", nullable = false)
  private Integer employeeId;

  @Column(name = "Class", length = 45)
  private String permissionClass;

  @Column(name = "Created_When", nullable = false)
  private LocalDateTime createdWhen;

  @Column(name = "Given_By")
  private Integer givenBy;
}
