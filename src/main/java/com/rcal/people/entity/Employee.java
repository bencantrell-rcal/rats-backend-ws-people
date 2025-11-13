package com.rcal.people.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "employees")
public class Employee{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "eid")
  private Integer eid;

  @Column(name = "first", nullable = false, length = 45)
  private String firstName;

  @Column(name = "middle", length = 45)
  private String middleName;

  @Column(name = "last", nullable = false, length = 45)
  private String lastName;

  @Column(name = "email", length = 100, unique = true)
  private String email;

  @Column(name = "login", length = 45, unique = true)
  private String login;

  @Column(name = "active", length = 10)
  private String activeStatus; // e.g., "Active", "Inactive", "On Leave"

  @Column(name = "department", length = 255)
  private String department;

  @Column(name = "title", length = 255)
  private String title;

  @Column(name = "category", length = 45)
  private String category; // e.g., "Full-time", "Contract", "Intern"

  @Column(name = "StartDate")
  private LocalDate startDate;

  @Column(name = "StopDate")
  private LocalDate stopDate;

  @Column(name = "birthday")
  private LocalDate birthday;

  @Column(name = "hourly", length = 10)
  private String hourly; // “Yes” or “No” or could be a boolean later

}
