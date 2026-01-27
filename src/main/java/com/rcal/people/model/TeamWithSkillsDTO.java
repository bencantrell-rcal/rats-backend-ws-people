package com.rcal.people.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TeamWithSkillsDTO{
  private TeamBasicDTO team;
  private List<SkillBasicDTO> skills;
}
