package com.rcal.people.model;

import com.rcal.people.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoleSummaryDTO{

  private String roleName;

  private Integer roleId;

  private String roleDescription;

  private List<TeamBasicDTO> teams;

  private List<SkillBasicDTO> skills;

  private List<PersonBasicDTO> people;
}
