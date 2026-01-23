package com.rcal.people.model;

import com.rcal.people.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TeamSummaryDTO{

  private String teamName;

  private String teamDescription;

  private List<RoleBasicDTO> roles;

  private List<Person> people;

  private List<SkillBasicDTO> skills;
}
