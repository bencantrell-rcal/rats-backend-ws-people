package com.rcal.people.service;

import com.rcal.people.entity.Mapping;
import com.rcal.people.entity.Person;
import com.rcal.people.model.*;
import com.rcal.people.repository.read.ReadPersonRepository;
import com.rcal.people.repository.write.WritePersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService{

  private final ReadPersonRepository readPersonRepository;
  private final WritePersonRepository writePersonRepository;
  private final MappingService mappingService;

  public PersonService(ReadPersonRepository readPersonRepository,
      WritePersonRepository writePersonRepository,
      MappingService mappingService) {
    this.readPersonRepository = readPersonRepository;
    this.writePersonRepository = writePersonRepository;
    this.mappingService = mappingService;
  }

  public PersonSummaryDTO buildPersonSummary(Integer personId){

    Person person = getPersonById(personId);

    // ---------------- Roles ----------------
    List<Mapping> roleMappings = mappingService.getHigherEntities(
        personId.longValue(),EntityTypes.PERSON,EntityTypes.ROLE);

    List<RoleBasicDTO> roles = new ArrayList<>();
    for (Mapping mapping : roleMappings){
      roles.add((RoleBasicDTO) mappingService
          .getBasicByIdAndEntityType(mapping.getToEntityId(),EntityTypes.ROLE));
    }

    // ---------------- Teams ----------------
    List<TeamBasicDTO> teams = new ArrayList<>();
    List<Mapping> teamMappings = new ArrayList<>();

    for (RoleBasicDTO role : roles){
      teamMappings.addAll(mappingService.getLowerEntities(
          role.getRoleId().longValue(),EntityTypes.ROLE,EntityTypes.TEAM));
    }

    for (Mapping mapping : teamMappings){
      teams.add((TeamBasicDTO) mappingService.getBasicByIdAndEntityType(
          mapping.getFromEntityId(),EntityTypes.TEAM));
    }

    // ---------------- Skills ----------------
    List<SkillBasicDTO> skills = new ArrayList<>();

    List<Mapping> skillMappings = mappingService.getLowerEntities(
        personId.longValue(),EntityTypes.PERSON,EntityTypes.SKILL);

    for (Mapping mapping : skillMappings){
      skills.add((SkillBasicDTO) mappingService.getBasicByIdAndEntityType(
          mapping.getFromEntityId(),EntityTypes.SKILL));
    }

    return new PersonSummaryDTO(person.getName(), teams, roles, skills);
  }

  public Page<Person> getAllPeople(Pageable pageable){
    return readPersonRepository.findAll(pageable);
  }

  // ---------------------------------------------------------------------------
  // Get all people
  // ---------------------------------------------------------------------------
  public List<Person> getAllPeople(){
    return readPersonRepository.findAll();
  }

  // ---------------------------------------------------------------------------
  // Get a specific person by ID
  // ---------------------------------------------------------------------------
  public Person getPersonById(Integer personId){
    return readPersonRepository.findById(personId).orElseThrow(
        () -> new RuntimeException("Person not found with id: " + personId));
  }

  // ---------------------------------------------------------------------------
  // Create a new person
  // ---------------------------------------------------------------------------
  public Person createPerson(String name){

    if (name == null){
      throw new IllegalArgumentException("Person name must not be null");
    }

    // Keep only alphabetic characters and spaces
    String sanitizedName = name.replaceAll("[^A-Za-z ]","").trim()
        .replaceAll("\\s+"," ");

    if (sanitizedName.isEmpty()){
      throw new IllegalArgumentException(
          "Person name must contain alphabetic characters");
    }

    Person person = new Person();
    person.setName(sanitizedName);

    return writePersonRepository.save(person);
  }

  // ---------------------------------------------------------------------------
  // Delete a person by ID
  // ---------------------------------------------------------------------------
  public void deletePerson(Integer personId){
    writePersonRepository.deleteById(personId);
  }

  // ---------------------------------------------------------------------------
  // Get a specific person by ID (Optional)
  // ---------------------------------------------------------------------------
  public Optional<Person> getPersonByIdOptional(Integer personId){
    return readPersonRepository.findById(personId);
  }

}
