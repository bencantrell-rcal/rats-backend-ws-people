package com.rcal.people.service;

import com.rcal.people.entity.Mapping;
import com.rcal.people.model.EntityTypes;
import com.rcal.people.model.RoleBasicDTO;
import com.rcal.people.model.SkillBasicDTO;
import com.rcal.people.model.TeamBasicDTO;
import com.rcal.people.repository.read.*;
import com.rcal.people.repository.write.WriteMappingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MappingService{

  private final ReadMappingRepository readMappingRepository;
  private final WriteMappingRepository writeMappingRepository;
  private final ReadPersonRepository readPersonRepository;
  private final ReadRoleRepository readRoleRepository;
  private final ReadSkillRepository readSkillRepository;
  private final ReadTeamRepository readTeamRepository;

  public MappingService(ReadMappingRepository readMappingRepository,
      WriteMappingRepository writeMappingRepository,
      ReadPersonRepository readPersonRepository,
      ReadRoleRepository readRoleRepository,
      ReadSkillRepository readSkillRepository,
      ReadTeamRepository readTeamRepository) {

    this.readMappingRepository = readMappingRepository;
    this.writeMappingRepository = writeMappingRepository;
    this.readPersonRepository = readPersonRepository;
    this.readRoleRepository = readRoleRepository;
    this.readSkillRepository = readSkillRepository;
    this.readTeamRepository = readTeamRepository;
  }

  // ---------------------------------------------------------------------------
  // Create a new mapping (generic: PERSON > SKILL, ROLE > TEAM, etc.)
  // ---------------------------------------------------------------------------
  public Mapping createMapping(Long fromEntityId,String fromEntityType,
      Long toEntityId,String toEntityType){

    validateEntitiesExist(fromEntityId,fromEntityType,toEntityId,toEntityType);

    Mapping mapping = new Mapping();
    mapping.setFromEntityId(fromEntityId);
    mapping.setFromEntityType(fromEntityType);
    mapping.setToEntityId(toEntityId);
    mapping.setToEntityType(toEntityType);

    return writeMappingRepository.save(mapping);
  }

  public void deleteMapping(Long fromEntityId,String fromEntityType,
      Long toEntityId,String toEntityType){

    // optional but recommended for symmetry & better errors
    validateEntitiesExist(fromEntityId,fromEntityType,toEntityId,toEntityType);

    int deleted = writeMappingRepository
        .deleteByFromEntityIdAndFromEntityTypeAndToEntityIdAndToEntityType(
            fromEntityId,fromEntityType,toEntityId,toEntityType);

    if (deleted == 0){
      throw new EntityNotFoundException(
          String.format("Mapping not found: %s(%d) -> %s(%d)",fromEntityType,
              fromEntityId,toEntityType,toEntityId));
    }
  }

  // ---------------------------------------------------------------------------
  // Returns entities higher in the hierarchy
  // ---------------------------------------------------------------------------
  public List<Mapping> getHigherEntities(Long fromEntityId,
      String fromEntityType,String toEntityType){
    return readMappingRepository
        .findByFromEntityTypeAndFromEntityIdAndToEntityType(fromEntityType,
            fromEntityId,toEntityType);
  }

  // ---------------------------------------------------------------------------
  // Returns entities lower in the hierarchy
  // ---------------------------------------------------------------------------
  public List<Mapping> getLowerEntities(Long toEntityId,String toEntityType,
      String fromEntityType){
    return readMappingRepository
        .findByToEntityTypeAndToEntityIdAndFromEntityType(toEntityType,
            toEntityId,fromEntityType);
  }

  public boolean entityExists(Long entityId,String entityType){

    if (entityType == null){
      throw new IllegalArgumentException("Entity type must not be null");
    }

    String normalizedType = entityType.trim().toUpperCase();

    switch (normalizedType) {

      case EntityTypes.PERSON :
        return readPersonRepository.existsById(entityId.intValue());

      case EntityTypes.ROLE :
        return readRoleRepository.existsById(entityId.intValue());

      case EntityTypes.SKILL :
        return readSkillRepository.existsById(entityId.intValue());

      case EntityTypes.TEAM :
        return readTeamRepository.existsById(entityId.intValue());

      default :
        throw new IllegalArgumentException(
            "Unsupported entity type: " + entityType);
    }
  }

  public void validateEntitiesExist(Long fromEntityId,String fromEntityType,
      Long toEntityId,String toEntityType){

    if (!entityExists(fromEntityId,fromEntityType)){
      throw new IllegalArgumentException(
          "From-entity not found: " + fromEntityType + " id=" + fromEntityId);
    }

    if (!entityExists(toEntityId,toEntityType)){
      throw new IllegalArgumentException(
          "To-entity not found: " + toEntityType + " id=" + toEntityId);
    }
  }

  public Object getBasicByIdAndEntityType(Long entityId,String entityType){
    if (entityType == null){
      throw new IllegalArgumentException("Entity type must not be null");
    }

    String normalizedType = entityType.trim().toUpperCase();

    switch (normalizedType) {

      case EntityTypes.PERSON :
        return readPersonRepository.findById(entityId.intValue())
            .orElseThrow(() -> new RuntimeException(
                "Person not found with id: " + entityId));

      case EntityTypes.ROLE :
        return readRoleRepository.findById(entityId.intValue())
            .map(role -> new RoleBasicDTO(role.getRoleId(), role.getRoleName()))
            .orElseThrow(() -> new RuntimeException(
                "Role not found with id: " + entityId));

      case EntityTypes.SKILL :
        return readSkillRepository.findById(entityId.intValue())
            .map(skill -> new SkillBasicDTO(skill.getSkillId(),
                skill.getSkillName()))
            .orElseThrow(() -> new RuntimeException(
                "Skill not found with id: " + entityId));

      case EntityTypes.TEAM :
        return readTeamRepository.findById(entityId.intValue())
            .map(team -> new TeamBasicDTO(team.getTeamId(), team.getTeamName()))
            .orElseThrow(() -> new RuntimeException(
                "Team not found with id: " + entityId));

      default :
        throw new IllegalArgumentException(
            "Unsupported entity type: " + entityType);
    }
  }

  // ---------------------------------------------------------------------------
  // Deletes all mappings where the entity appears as FROM or TO
  // ---------------------------------------------------------------------------
  public void deleteAllMappingsForEntity(Long entityId,String entityType){

    if (entityId == null || entityType == null){
      throw new IllegalArgumentException("Entity id and type must not be null");
    }

    String normalizedType = entityType.trim().toUpperCase();

    // Entity is the parent (FROM)
    List<Mapping> fromMappings = readMappingRepository
        .findByFromEntityTypeAndFromEntityId(normalizedType,entityId);

    // Entity is the child (TO)
    List<Mapping> toMappings = readMappingRepository
        .findByToEntityTypeAndToEntityId(normalizedType,entityId);

    writeMappingRepository.deleteAll(fromMappings);
    writeMappingRepository.deleteAll(toMappings);
  }

}
