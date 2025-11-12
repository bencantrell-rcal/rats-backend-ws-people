package com.rcal.people.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "employees")
public class Employee{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "eid")
  private Integer eid;

  @Column(name = "first")
  private String firstName;

  @Column(name = "last")
  private String lastName;

}
