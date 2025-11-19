package com.rcal.people.model;

import lombok.Data;
import java.util.List;

@Data
public class TeamSkillDTO{
  private String teamName;
  private List<String> skills;
}