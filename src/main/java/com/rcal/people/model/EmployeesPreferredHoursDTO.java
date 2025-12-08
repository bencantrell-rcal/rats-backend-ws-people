package com.rcal.people.model;

import com.rcal.people.entity.EmployeesPreferredHours;
import lombok.Data;

import java.util.List;

@Data
public class EmployeesPreferredHoursDTO{
  private Long employeeId;
  private String firstName;
  private String lastName;
  private List<EmployeesPreferredHours> preferredHours;
}
