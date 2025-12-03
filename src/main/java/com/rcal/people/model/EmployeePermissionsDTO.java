package com.rcal.people.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeePermissionsDTO{
  private Integer eid;
  private String firstName;
  private String lastName;
  private List<String> permissions;
}