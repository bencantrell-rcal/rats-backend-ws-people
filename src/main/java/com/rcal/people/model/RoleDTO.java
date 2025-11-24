package com.rcal.people.model;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO{
  private String roleId;
  private List<String> skills;
  private List<String> teams;
}
