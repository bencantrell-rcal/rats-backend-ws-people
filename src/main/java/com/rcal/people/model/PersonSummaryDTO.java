package com.rcal.people.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PersonSummaryDTO{

  private String name;

  private List<TeamBasicDTO> teams;

  private List<RoleBasicDTO> roles;

  private List<SkillBasicDTO> skills;
}
