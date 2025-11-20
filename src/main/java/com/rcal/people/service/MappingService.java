package com.rcal.people.service;

import com.rcal.people.entity.Mapping;
import com.rcal.people.model.PeopleDTO;
import com.rcal.people.model.TeamSkillDTO;
import com.rcal.people.repository.read.ReadMappingRepository;
import com.rcal.people.repository.write.WriteMappingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MappingService{

  private final ReadMappingRepository readMappingRepository;
  private final WriteMappingRepository writeMappingRepository;

  public MappingService(ReadMappingRepository readMappingRepository,
      WriteMappingRepository writeMappingRepository) {
    this.readMappingRepository = readMappingRepository;
    this.writeMappingRepository = writeMappingRepository;
  }

  // ---------------------- READ METHODS ----------------------

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
      dto.setTeamName(teamId); // teamName is just the ID string
      dto.setSkills(skills);

      result.add(dto);
    }

    return result;
  }

  public List<PeopleDTO> getAllPeople(){
    List<String> personIds = readMappingRepository
        .findByFromEntityTypeAndToEntityType("person","team").stream()
        .map(Mapping::getFromEntityId).distinct().toList();

    List<String> personSkillIds = readMappingRepository
        .findByFromEntityTypeAndToEntityType("person","skill").stream()
        .map(Mapping::getFromEntityId).distinct().toList();

    for (String id : personSkillIds){
      if (!personIds.contains(id)){
        personIds.add(id);
      }
    }

    List<PeopleDTO> result = new ArrayList<>();
    List<TeamSkillDTO> allTeams = getAllTeamsWithSkills();

    for (String personId : personIds){
      PeopleDTO dto = new PeopleDTO();
      dto.setPersonId(personId);

      List<Mapping> teamMappings = readMappingRepository
          .findByFromEntityIdAndFromEntityTypeAndToEntityType(personId,"person",
              "team");

      List<TeamSkillDTO> teams = new ArrayList<>();
      for (Mapping m : teamMappings){
        allTeams.stream().filter(t -> t.getTeamName().equals(m.getToEntityId()))
            .findFirst().ifPresent(teams::add);
      }
      dto.setTeams(teams);

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

  // ---------------------- WRITE METHODS ----------------------

  public void addSkillToPerson(String personId,String skillName){
    if (readMappingRepository
        .findByFromEntityIdAndFromEntityTypeAndToEntityIdAndToEntityType(
            personId,"PERSON",skillName,"SKILL")
        .isEmpty()){

      Mapping mapping = new Mapping();
      mapping.setFromEntityId(personId);
      mapping.setFromEntityType("PERSON");
      mapping.setToEntityId(skillName);
      mapping.setToEntityType("SKILL");

      writeMappingRepository.save(mapping);
    }
  }

  public void addSkillToTeam(String teamName,String skillName){
    if (readMappingRepository
        .findByFromEntityIdAndFromEntityTypeAndToEntityIdAndToEntityType(
            teamName,"TEAM",skillName,"SKILL")
        .isEmpty()){

      Mapping mapping = new Mapping();
      mapping.setFromEntityId(teamName);
      mapping.setFromEntityType("TEAM");
      mapping.setToEntityId(skillName);
      mapping.setToEntityType("SKILL");

      writeMappingRepository.save(mapping);
    }
  }

  public void addTeamToPerson(String personId,String teamName){
    if (readMappingRepository
        .findByFromEntityIdAndFromEntityTypeAndToEntityIdAndToEntityType(
            personId,"PERSON",teamName,"TEAM")
        .isEmpty()){

      Mapping mapping = new Mapping();
      mapping.setFromEntityId(personId);
      mapping.setFromEntityType("PERSON");
      mapping.setToEntityId(teamName);
      mapping.setToEntityType("TEAM");

      writeMappingRepository.save(mapping);
    }
  }

  // ---------- DELETE OPERATIONS ----------

  // Delete a skill from a person
  public void deleteSkillFromPerson(String personId,String skillName){
    writeMappingRepository
        .deleteByFromEntityTypeAndFromEntityIdAndToEntityTypeAndToEntityId(
            "PERSON",personId,"SKILL",skillName);
  }

  // Delete a skill from a team
  public void deleteSkillFromTeam(String teamName,String skillName){
    writeMappingRepository
        .deleteByFromEntityTypeAndFromEntityIdAndToEntityTypeAndToEntityId(
            "TEAM",teamName,"SKILL",skillName);
  }

  // Delete a team from a person
  public void deleteTeamFromPerson(String personId,String teamName){
    writeMappingRepository
        .deleteByFromEntityTypeAndFromEntityIdAndToEntityTypeAndToEntityId(
            "PERSON",personId,"TEAM",teamName);
  }
}
