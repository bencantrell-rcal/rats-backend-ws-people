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

      // --- Teams for this role ---
      List<String> teams = readMappingRepository
          .findByFromEntityIdAndFromEntityTypeAndToEntityType(roleId,"ROLE",
              "TEAM")
          .stream().map(Mapping::getToEntityId).toList();
      dto.setTeams(teams);

      // --- Skills for this role ---
      List<String> skills = readMappingRepository
          .findByFromEntityIdAndFromEntityTypeAndToEntityType(roleId,"ROLE",
              "SKILL")
          .stream().map(Mapping::getToEntityId).toList();
      dto.setSkills(skills);

      // --- Description for this role ---
      List<Mapping> descMappings = readMappingRepository
          .findByFromEntityIdAndFromEntityTypeAndToEntityType(roleId,"ROLE",
              "DESCRIPTION");

      String description = descMappings.isEmpty()
          ? null
          : descMappings.get(0).getToEntityId();

      dto.setDescription(description);

      result.add(dto);
    }

    return result;
  }

  public void addDescriptionToTeam(String teamId,String description){
    // delete any existing description if you want single-description behavior:
    writeMappingRepository.deleteByFromEntityTypeAndFromEntityIdAndToEntityType(
        "TEAM",teamId,"DESCRIPTION");

    Mapping mapping = new Mapping();
    mapping.setFromEntityId(teamId);
    mapping.setFromEntityType("TEAM");
    mapping.setToEntityId(description);
    mapping.setToEntityType("DESCRIPTION");

    writeMappingRepository.save(mapping);
  }

  public void addDescriptionToRole(String roleId,String description){
    writeMappingRepository.deleteByFromEntityTypeAndFromEntityIdAndToEntityType(
        "ROLE",roleId,"DESCRIPTION");

    Mapping mapping = new Mapping();
    mapping.setFromEntityId(roleId);
    mapping.setFromEntityType("ROLE");
    mapping.setToEntityId(description);
    mapping.setToEntityType("DESCRIPTION");

    writeMappingRepository.save(mapping);
  }

  public void deleteDescriptionFromTeam(String teamId){
    writeMappingRepository.deleteByFromEntityTypeAndFromEntityIdAndToEntityType(
        "TEAM",teamId,"DESCRIPTION");
  }

  public void deleteDescriptionFromRole(String roleId){
    writeMappingRepository.deleteByFromEntityTypeAndFromEntityIdAndToEntityType(
        "ROLE",roleId,"DESCRIPTION");
  }
}
