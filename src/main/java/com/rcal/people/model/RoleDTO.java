package com.rcal.people.model;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO{
  private String roleId;
  private String description;
  private List<String> skills;
  private List<String> teams;
}