package com.rcal.people.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "people_people")
public class Person{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "person_id", updatable = false, insertable = false)
  private Integer personId;

  @Column(name = "name")
  private String name;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "headshot")
  private byte[] headshot;
}
