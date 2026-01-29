package com.rcal.people.model;

import com.rcal.people.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TeamSummaryDTO{

  private String teamName;

  private Integer teamId;

  private String teamDescription;

  private List<RoleBasicDTO> roles;

  private List<PersonBasicDTO> people;

  private List<SkillBasicDTO> skills;
}
