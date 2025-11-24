package com.rcal.people.service;

import com.rcal.people.entity.Mapping;
import com.rcal.people.model.PeopleDTO;
import com.rcal.people.model.RoleDTO;
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

  /**
   * Returns all teams with their skills.
   */
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

  /**
   * Return all people with:
   * - roles
   * - skills
   */
  public List<PeopleDTO> getAllPeople(){

    // People with roles
    List<String> rolePeople = readMappingRepository
        .findByFromEntityTypeAndToEntityType("PERSON","ROLE").stream()
        .map(Mapping::getFromEntityId).distinct().toList();

    // People with skills
    List<String> skillPeople = readMappingRepository
        .findByFromEntityTypeAndToEntityType("PERSON","SKILL").stream()
        .map(Mapping::getFromEntityId).distinct().toList();

    // Merge
    List<String> personIds = new ArrayList<>(rolePeople);
    for (String id : skillPeople){
      if (!personIds.contains(id)){
        personIds.add(id);
      }
    }

    List<PeopleDTO> result = new ArrayList<>();

    for (String personId : personIds){
      PeopleDTO dto = new PeopleDTO();
      dto.setPersonId(personId);

      // Roles
      List<Mapping> roleMappings = readMappingRepository
          .findByFromEntityIdAndFromEntityTypeAndToEntityType(personId,"PERSON",
              "ROLE");

      List<String> roles = roleMappings.stream().map(Mapping::getToEntityId)
          .toList();
      dto.setRoles(roles);

      // Skills
      List<Mapping> skillMappings = readMappingRepository
          .findByFromEntityIdAndFromEntityTypeAndToEntityType(personId,"PERSON",
              "SKILL");

      List<String> skills = skillMappings.stream().map(Mapping::getToEntityId)
          .toList();
      dto.setSkills(skills);

      result.add(dto);
    }

    return result;
  }

  // ---------------------- WRITE METHODS ----------------------

  /** Add a skill to a person */
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

  /** Add a role to a person */
  public void addRoleToPerson(String personId,String roleId){
    if (readMappingRepository
        .findByFromEntityIdAndFromEntityTypeAndToEntityIdAndToEntityType(
            personId,"PERSON",roleId,"ROLE")
        .isEmpty()){

      Mapping mapping = new Mapping();
      mapping.setFromEntityId(personId);
      mapping.setFromEntityType("PERSON");
      mapping.setToEntityId(roleId);
      mapping.setToEntityType("ROLE");

      writeMappingRepository.save(mapping);
    }
  }

  /** Add a skill to a team */
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

  /** Add a team to a role */
  public void addTeamToRole(String roleId,String teamId){
    if (readMappingRepository
        .findByFromEntityIdAndFromEntityTypeAndToEntityIdAndToEntityType(roleId,
            "ROLE",teamId,"TEAM")
        .isEmpty()){

      Mapping mapping = new Mapping();
      mapping.setFromEntityId(roleId);
      mapping.setFromEntityType("ROLE");
      mapping.setToEntityId(teamId);
      mapping.setToEntityType("TEAM");

      writeMappingRepository.save(mapping);
    }
  }

  /** Add a skill to a role */
  public void addSkillToRole(String roleId,String skillId){
    if (readMappingRepository
        .findByFromEntityIdAndFromEntityTypeAndToEntityIdAndToEntityType(roleId,
            "ROLE",skillId,"SKILL")
        .isEmpty()){

      Mapping mapping = new Mapping();
      mapping.setFromEntityId(roleId);
      mapping.setFromEntityType("ROLE");
      mapping.setToEntityId(skillId);
      mapping.setToEntityType("SKILL");

      writeMappingRepository.save(mapping);
    }
  }

  // ---------- DELETE OPERATIONS ----------

  public void deleteSkillFromPerson(String personId,String skillName){
    writeMappingRepository
        .deleteByFromEntityTypeAndFromEntityIdAndToEntityTypeAndToEntityId(
            "PERSON",personId,"SKILL",skillName);
  }

  public void deleteRoleFromPerson(String personId,String roleId){
    writeMappingRepository
        .deleteByFromEntityTypeAndFromEntityIdAndToEntityTypeAndToEntityId(
            "PERSON",personId,"ROLE",roleId);
  }

  public void deleteSkillFromTeam(String teamName,String skillName){
    writeMappingRepository
        .deleteByFromEntityTypeAndFromEntityIdAndToEntityTypeAndToEntityId(
            "TEAM",teamName,"SKILL",skillName);
  }

  public void deleteTeamFromRole(String roleId,String teamId){
    writeMappingRepository
        .deleteByFromEntityTypeAndFromEntityIdAndToEntityTypeAndToEntityId(
            "ROLE",roleId,"TEAM",teamId);
  }

  public void deleteSkillFromRole(String roleId,String skillId){
    writeMappingRepository
        .deleteByFromEntityTypeAndFromEntityIdAndToEntityTypeAndToEntityId(
            "ROLE",roleId,"SKILL",skillId);
  }

  public List<String> getAllSkills(){
    return readMappingRepository.findByToEntityType("SKILL").stream()
        .map(Mapping::getToEntityId).distinct().toList();
  }

  public List<RoleDTO> getAllRoles(){

    // Collect all unique role IDs (from any ROLE → TEAM or ROLE → SKILL
    // mapping)
    List<String> roleIds = readMappingRepository.findByFromEntityType("ROLE")
        .stream().map(Mapping::getFromEntityId).distinct().toList();

    List<RoleDTO> result = new ArrayList<>();

    for (String roleId : roleIds){
      RoleDTO dto = new RoleDTO();
      dto.setRoleId(roleId);

      // Teams for this role
      List<String> teams = readMappingRepository
          .findByFromEntityIdAndFromEntityTypeAndToEntityType(roleId,"ROLE",
              "TEAM")
          .stream().map(Mapping::getToEntityId).toList();
      dto.setTeams(teams);

      // Skills for this role
      List<String> skills = readMappingRepository
          .findByFromEntityIdAndFromEntityTypeAndToEntityType(roleId,"ROLE",
              "SKILL")
          .stream().map(Mapping::getToEntityId).toList();
      dto.setSkills(skills);

      result.add(dto);
    }

    return result;
  }
}
