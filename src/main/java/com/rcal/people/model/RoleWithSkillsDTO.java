package com.rcal.people.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoleWithSkillsDTO{
  private RoleBasicDTO role;
  List<SkillBasicDTO> skills;
}
