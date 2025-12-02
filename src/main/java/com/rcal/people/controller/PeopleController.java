package com.rcal.people.controller;

import com.rcal.people.entity.Employee;
import com.rcal.people.model.PeopleDTO;
import com.rcal.people.model.RoleDTO;
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
  // Purpose: Gets all teams and associated skills
  // ---------------------------------------------------------------------------
  @Operation(summary = TEAMS_DESCRIPTION)
  @GetMapping("/teams")
  public List<TeamSkillDTO> getTeamsWithSkills(){
    return mappingService.getAllTeamsWithSkills();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Gets all people with roles and skills
  // ---------------------------------------------------------------------------
  @Operation(summary = PEOPLE_DESCRIPTION)
  @GetMapping("/people")
  public List<PeopleDTO> getAllPeople(){
    return mappingService.getAllPeople();
  }

  // ---------------------------------------------------------------------------
  // Adds a skill to a person
  // ---------------------------------------------------------------------------
  @Operation(summary = SKILL_PERSON_DESCRIPTION)
  @PostMapping("/people/skills")
  public ResponseEntity<String> addSkillToPerson(@RequestParam String personId,
      @RequestParam String skillName){

    mappingService.addSkillToPerson(personId,skillName);
    return ResponseEntity.ok("Skill added to person successfully");
  }

  // ---------------------------------------------------------------------------
  // Adds a role to a person
  // ---------------------------------------------------------------------------
  @Operation(summary = ROLE_PERSON_DESCRIPTION)
  @PostMapping("/people/roles")
  public ResponseEntity<String> addRoleToPerson(@RequestParam String personId,
      @RequestParam String roleId){

    mappingService.addRoleToPerson(personId,roleId);
    return ResponseEntity.ok("Role added to person successfully");
  }

  // ---------------------------------------------------------------------------
  // Adds a skill to a team
  // ---------------------------------------------------------------------------
  @Operation(summary = SKILL_TEAM_DESCRIPTION)
  @PostMapping("/teams/skills")
  public ResponseEntity<String> addSkillToTeam(@RequestParam String teamId,
      @RequestParam String skillName){

    mappingService.addSkillToTeam(teamId,skillName);
    return ResponseEntity.ok("Skill added to team successfully");
  }

  // ---------------------------------------------------------------------------
  // Adds a team to a role
  // ---------------------------------------------------------------------------
  @Operation(summary = TEAM_ROLE_DESCRIPTION)
  @PostMapping("/roles/teams")
  public ResponseEntity<String> addTeamToRole(@RequestParam String roleId,
      @RequestParam String teamId){

    mappingService.addTeamToRole(roleId,teamId);
    return ResponseEntity.ok("Team added to role successfully");
  }

  // ---------------------------------------------------------------------------
  // Adds a skill to a role
  // ---------------------------------------------------------------------------
  @Operation(summary = SKILL_ROLE_DESCRIPTION)
  @PostMapping("/roles/skills")
  public ResponseEntity<String> addSkillToRole(@RequestParam String roleId,
      @RequestParam String skillId){

    mappingService.addSkillToRole(roleId,skillId);
    return ResponseEntity.ok("Skill added to role successfully");
  }

  // ---------------------------------------------------------------------------
  // Deletes a skill from a person
  // ---------------------------------------------------------------------------
  @Operation(summary = SKILL_PERSON_DELETE_DESCRIPTION)
  @DeleteMapping("/people/skills")
  public ResponseEntity<String> deleteSkillFromPerson(
      @RequestParam String personId,@RequestParam String skillName){

    mappingService.deleteSkillFromPerson(personId,skillName);
    return ResponseEntity.ok("Skill removed from person successfully");
  }

  // ---------------------------------------------------------------------------
  // Deletes a role from a person
  // ---------------------------------------------------------------------------
  @Operation(summary = ROLE_PERSON_DELETE_DESCRIPTION)
  @DeleteMapping("/people/roles")
  public ResponseEntity<String> deleteRoleFromPerson(
      @RequestParam String personId,@RequestParam String roleId){

    mappingService.deleteRoleFromPerson(personId,roleId);
    return ResponseEntity.ok("Role removed from person successfully");
  }

  // ---------------------------------------------------------------------------
  // Deletes a skill from a team
  // ---------------------------------------------------------------------------
  @Operation(summary = SKILL_TEAM_DELETE_DESCRIPTION)
  @DeleteMapping("/teams/skills")
  public ResponseEntity<String> deleteSkillFromTeam(@RequestParam String teamId,
      @RequestParam String skillName){

    mappingService.deleteSkillFromTeam(teamId,skillName);
    return ResponseEntity.ok("Skill removed from team successfully");
  }

  // ---------------------------------------------------------------------------
  // Deletes a team from a role
  // ---------------------------------------------------------------------------
  @Operation(summary = TEAM_ROLE_DELETE_DESCRIPTION)
  @DeleteMapping("/roles/teams")
  public ResponseEntity<String> deleteTeamFromRole(@RequestParam String roleId,
      @RequestParam String teamId){

    mappingService.deleteTeamFromRole(roleId,teamId);
    return ResponseEntity.ok("Team removed from role successfully");
  }

  // ---------------------------------------------------------------------------
  // Deletes a skill from a role
  // ---------------------------------------------------------------------------
  @Operation(summary = SKILL_ROLE_DELETE_DESCRIPTION)
  @DeleteMapping("/roles/skills")
  public ResponseEntity<String> deleteSkillFromRole(@RequestParam String roleId,
      @RequestParam String skillId){

    mappingService.deleteSkillFromRole(roleId,skillId);
    return ResponseEntity.ok("Skill removed from role successfully");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Gets all roles with associated teams and skills
  // ---------------------------------------------------------------------------
  @Operation(summary = ROLES_DESCRIPTION)
  @GetMapping("/roles")
  public List<RoleDTO> getAllRoles(){
    return mappingService.getAllRoles();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Gets all skills
  // ---------------------------------------------------------------------------
  @Operation(summary = SKILLS_DESCRIPTION)
  @GetMapping("/skills")
  public List<String> getAllSkills(){
    return mappingService.getAllSkills();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a description to a team
  // ---------------------------------------------------------------------------
  @Operation(summary = TEAMS_DESCRIPTIONS_DESCRIPTION)
  @PostMapping("/teams/descriptions")
  public ResponseEntity<String> addTeamDescription(@RequestParam String teamId,
      @RequestParam String description){

    mappingService.addDescriptionToTeam(teamId,description);
    return ResponseEntity.ok("Description added to team");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a description to a role
  // ---------------------------------------------------------------------------
  @Operation(summary = ROLES_DESCRIPTIONS_DESCRIPTION)
  @PostMapping("/roles/descriptions")
  public ResponseEntity<String> addRoleDescription(@RequestParam String roleId,
      @RequestParam String description){

    mappingService.addDescriptionToRole(roleId,description);
    return ResponseEntity.ok("Description added to role");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Delete a description from a team
  // ---------------------------------------------------------------------------
  @Operation(summary = TEAMS_DESCRIPTIONS_DELETE_DESCRIPTION)
  @DeleteMapping("/teams/descriptions")
  public ResponseEntity<String> deleteTeamDescription(
      @RequestParam String teamId){
    mappingService.deleteDescriptionFromTeam(teamId);
    return ResponseEntity.ok("Description removed from team: " + teamId);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Deletes a description from a role
  // ---------------------------------------------------------------------------
  @Operation(summary = ROLES_DESCRIPTIONS_DELETE_DESCRIPTION)
  @DeleteMapping("/roles/descriptions")
  public ResponseEntity<String> deleteRoleDescription(
      @RequestParam String roleId){
    mappingService.deleteDescriptionFromRole(roleId);
    return ResponseEntity.ok("Description removed from role: " + roleId);
  }

  @GetMapping("/docs/content")
  public ResponseEntity<String> getMarkdownFile(@RequestParam String fileName){
    return ResponseEntity
        .ok(githubContentService.fetchPublicMarkdown(fileName));
  }

}
