package com.rcal.people.controller;

import com.rcal.people.entity.Employee;
import com.rcal.people.model.PeopleDTO;
import com.rcal.people.model.TeamSkillDTO;
import com.rcal.people.service.EmployeeService;
import com.rcal.people.service.GithubContentService;
import com.rcal.people.service.MappingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rcal.people.configuration.OpenApiDescriptionConfiguration.*;

@RestController
public class PeopleController{

  private final EmployeeService employeeService;
  private final MappingService mappingService;
  private final GithubContentService githubContentService;

  public PeopleController(EmployeeService employeeService,
      MappingService mappingService,
      GithubContentService githubContentService) {
    this.employeeService = employeeService;
    this.mappingService = mappingService;
    this.githubContentService = githubContentService;
  }

  // ---------------------------------------------------------------------------
  // Purpose: This method serves as a check to verify if the app is exposing
  // its endpoints correctly. When this endpoint is hit, it responds "healthy"
  // with a timestamp attached
  // ---------------------------------------------------------------------------
  @Operation(summary = HEALTH_DESCRIPTION)
  @GetMapping("/health")
  public ResponseEntity<Map<String, String>> health(){
    DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String timestamp = LocalDateTime.now(ZoneOffset.UTC).format(FORMATTER);

    Map<String, String> response = new HashMap<>();
    response.put("status","Healthy");
    response.put("timestamp",timestamp);

    System.out.println("Healthy " + timestamp);
    return ResponseEntity.ok(response);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Gets all employees and RATS employee data
  // ---------------------------------------------------------------------------
  @Operation(summary = EMPLOYEES_DESCRIPTION)
  @GetMapping("/employees")
  public List<Employee> getAllEmployees(){
    return employeeService.getAllEmployees();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Get all teams and associated skills
  // ---------------------------------------------------------------------------
  @Operation(summary = TEAMS_DESCRIPTION)
  @GetMapping("/teams")
  public List<TeamSkillDTO> getTeamsWithSkills(){
    return mappingService.getAllTeamsWithSkills();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Get all people and associated teams and skills
  // ---------------------------------------------------------------------------
  @Operation(summary = PEOPLE_DESCRIPTION)
  @GetMapping("/people")
  public List<PeopleDTO> getAllPeople(){
    return mappingService.getAllPeople();
  }

  // ---------------------------------------------------------------------------
  // Add a skill to a person
  // ---------------------------------------------------------------------------
  @Operation(summary = SKILL_PERSON_DESCRIPTION)
  @PostMapping("/people/skills")
  public ResponseEntity<String> addSkillToPerson(
      @RequestParam String personName,@RequestParam String skillName){
    mappingService.addSkillToPerson(personName,skillName);
    return ResponseEntity.ok("Skill added to person successfully");
  }

  // ---------------------------------------------------------------------------
  // Add a skill to a team
  // ---------------------------------------------------------------------------
  @Operation(summary = SKILL_TEAM_DESCRIPTION)
  @PostMapping("/teams/skills")
  public ResponseEntity<String> addSkillToTeam(@RequestParam String teamName,
      @RequestParam String skillName){
    mappingService.addSkillToTeam(teamName,skillName);
    return ResponseEntity.ok("Skill added to team successfully");
  }

  // ---------------------------------------------------------------------------
  // Add a team to a person
  // ---------------------------------------------------------------------------
  @Operation(summary = TEAM_PERSON_DESCRIPTION)
  @PostMapping("/people/teams")
  public ResponseEntity<String> addTeamToPerson(@RequestParam String personName,
      @RequestParam String teamName){
    mappingService.addTeamToPerson(personName,teamName);
    return ResponseEntity.ok("Team added to person successfully");
  }

  // ---------------------------------------------------------------------------
  // Deletes a skill from a person
  // ---------------------------------------------------------------------------
  @Operation(summary = SKILL_PERSON_DELETE_DESCRIPTION)
  @DeleteMapping("/people/skills")
  public ResponseEntity<String> deleteSkillFromPerson(
      @RequestParam String personName,@RequestParam String skillName){
    mappingService.deleteSkillFromPerson(personName,skillName);
    return ResponseEntity.ok("Skill removed from person successfully");
  }

  // ---------------------------------------------------------------------------
  // Deletes a skill from a team
  // ---------------------------------------------------------------------------
  @Operation(summary = SKILL_TEAM_DELETE_DESCRIPTION)
  @DeleteMapping("/teams/skills")
  public ResponseEntity<String> deleteSkillFromTeam(
      @RequestParam String teamName,@RequestParam String skillName){
    mappingService.deleteSkillFromTeam(teamName,skillName);
    return ResponseEntity.ok("Skill removed from team successfully");
  }

  // ---------------------------------------------------------------------------
  // Deletes a team from a person
  // ---------------------------------------------------------------------------
  @Operation(summary = TEAM_PERSON_DELETE_DESCRIPTION)
  @DeleteMapping("/people/teams")
  public ResponseEntity<String> deleteTeamFromPerson(
      @RequestParam String personName,@RequestParam String teamName){
    mappingService.deleteTeamFromPerson(personName,teamName);
    return ResponseEntity.ok("Team removed from person successfully");
  }

  @GetMapping("/docs/content")
  public ResponseEntity<String> getMarkdownFile(@RequestParam String fileName){
    return ResponseEntity
        .ok(githubContentService.fetchPublicMarkdown(fileName));
  }

}
