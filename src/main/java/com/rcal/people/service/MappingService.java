package com.rcal.people.service;

import com.rcal.people.entity.Mapping;
import com.rcal.people.model.PeopleDTO;
import com.rcal.people.model.TeamSkillDTO;
import com.rcal.people.repository.read.ReadMappingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MappingService{

  private final ReadMappingRepository readMappingRepository;

  public MappingService(ReadMappingRepository readMappingRepository) {
    this.readMappingRepository = readMappingRepository;
  }

  public List<TeamSkillDTO> getAllTeamsWithSkills(){
    List<String> teamIds = readMappingRepository.findAllTeamsWithSkills();
    List<TeamSkillDTO> result = new ArrayList<>();

    for (String teamId : teamIds){
      List<Mapping> skillMappings = readMappingRepository
          .findByFromEntityIdAndFromEntityTypeAndToEntityType(teamId,"TEAM",
              "SKILL");

      List<String> skills = new ArrayList<>();
      for (Mapping m : skillMappings){
        skills.add(m.getToEntityId());
      }

      TeamSkillDTO dto = new TeamSkillDTO();
      dto.setTeamName(teamId);
      dto.setSkills(skills);

      result.add(dto);
    }

    return result;
  }

  public List<PeopleDTO> getAllPeople(){

    // Find all unique people IDs that have any mappings
    List<String> personIds = readMappingRepository
        .findByFromEntityTypeAndToEntityType("person","team").stream()
        .map(Mapping::getFromEntityId).distinct().toList();

    // Include people mapped directly to skills
    List<String> personSkillIds = readMappingRepository
        .findByFromEntityTypeAndToEntityType("person","skill").stream()
        .map(Mapping::getFromEntityId).distinct().toList();

    // Merge lists
    for (String id : personSkillIds){
      if (!personIds.contains(id)){
        personIds.add(id);
      }
    }

    List<PeopleDTO> result = new ArrayList<>();

    // Load all teams once to reuse
    List<TeamSkillDTO> allTeams = getAllTeamsWithSkills();

    for (String personId : personIds){
      PeopleDTO dto = new PeopleDTO();
      dto.setPersonId(personId);

      // Teams
      List<Mapping> teamMappings = readMappingRepository
          .findByFromEntityIdAndFromEntityTypeAndToEntityType(personId,"person",
              "team");

      List<TeamSkillDTO> teams = new ArrayList<>();
      for (Mapping m : teamMappings){
        // Find full TeamSkillDTO by teamName (formerly teamId)
        allTeams.stream().filter(t -> t.getTeamName().equals(m.getToEntityId()))
            .findFirst().ifPresent(teams::add);
      }
      dto.setTeams(teams);

      // Individual skills
      List<Mapping> skillMappings = readMappingRepository
          .findByFromEntityIdAndFromEntityTypeAndToEntityType(personId,"person",
              "skill");

      List<String> skills = new ArrayList<>();
      for (Mapping m : skillMappings){
        skills.add(m.getToEntityId());
      }
      dto.setSkills(skills);

      result.add(dto);
    }

    return result;
  }
}
