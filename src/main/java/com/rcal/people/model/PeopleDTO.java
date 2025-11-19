package com.rcal.people.model;

import lombok.Data;
import java.util.List;

@Data
public class PeopleDTO{
  private String personId;
  private List<TeamSkillDTO> teams;
  private List<String> skills;
}