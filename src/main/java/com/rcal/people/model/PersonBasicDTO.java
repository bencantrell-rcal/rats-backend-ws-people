package com.rcal.people.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonBasicDTO{
  private Integer personId;
  private String name;
}
