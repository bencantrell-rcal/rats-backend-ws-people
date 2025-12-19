package com.rcal.people.controller;

import com.rcal.people.entity.*;
import com.rcal.people.model.*;
import com.rcal.people.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.rcal.people.configuration.OpenApiDescriptionConfiguration.*;

@RestController
public class PeopleController{

  private final EmployeeService employeeService;
  private final MappingService mappingService;
  private final GithubContentService githubContentService;
  private final EmployeePermissionService employeePermissionService;
  private final EmployeesPreferredHoursService employeesPreferredHoursService;
  private final EmployeeLoginService employeeLoginService;

  public PeopleController(EmployeeService employeeService,
      MappingService mappingService, GithubContentService githubContentService,
      EmployeePermissionService employeePermissionService,
      EmployeesPreferredHoursService employeesPreferredHoursService,
      EmployeeLoginService employeeLoginService) {
    this.employeeService = employeeService;
    this.mappingService = mappingService;
    this.githubContentService = githubContentService;
    this.employeePermissionService = employeePermissionService;
    this.employeesPreferredHoursService = employeesPreferredHoursService;
    this.employeeLoginService = employeeLoginService;
  }

  // ===========================================================================
  // HEALTH CHECK
  // ===========================================================================

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

  // ===========================================================================
  // EMPLOYEES
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Gets all employees and RATS employee data
  // ---------------------------------------------------------------------------
  @Tag(name = "employees")
  @Operation(summary = EMPLOYEES_DESCRIPTION)
  @GetMapping("/employees")
  public List<Employee> getAllEmployees(){
    return employeeService.getAllEmployees();
  }

  // ===========================================================================
  // PEOPLE (ROLES & SKILLS)
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Gets all people with roles and skills
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = PEOPLE_DESCRIPTION)
  @GetMapping("/people")
  public List<PeopleDTO> getAllPeople(){
    return mappingService.getAllPeople();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a skill to a person
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = SKILL_PERSON_DESCRIPTION)
  @PostMapping("/people/skills")
  public ResponseEntity<String> addSkillToPerson(@RequestParam String personId,
      @RequestParam String skillName){

    mappingService.addSkillToPerson(personId,skillName);
    return ResponseEntity.ok("Skill added to person successfully");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Deletes a skill from a person
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = SKILL_PERSON_DELETE_DESCRIPTION)
  @DeleteMapping("/people/skills")
  public ResponseEntity<String> deleteSkillFromPerson(
      @RequestParam String personId,@RequestParam String skillName){

    mappingService.deleteSkillFromPerson(personId,skillName);
    return ResponseEntity.ok("Skill removed from person successfully");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a role to a person
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = ROLE_PERSON_DESCRIPTION)
  @PostMapping("/people/roles")
  public ResponseEntity<String> addRoleToPerson(@RequestParam String personId,
      @RequestParam String roleId){

    mappingService.addRoleToPerson(personId,roleId);
    return ResponseEntity.ok("Role added to person successfully");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Deletes a role from a person
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = ROLE_PERSON_DELETE_DESCRIPTION)
  @DeleteMapping("/people/roles")
  public ResponseEntity<String> deleteRoleFromPerson(
      @RequestParam String personId,@RequestParam String roleId){

    mappingService.deleteRoleFromPerson(personId,roleId);
    return ResponseEntity.ok("Role removed from person successfully");
  }

  // ===========================================================================
  // TEAMS
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Gets all teams and associated skills
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = TEAMS_DESCRIPTION)
  @GetMapping("/teams")
  public List<TeamSkillDTO> getTeamsWithSkills(){
    return mappingService.getAllTeamsWithSkills();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a skill to a team
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = SKILL_TEAM_DESCRIPTION)
  @PostMapping("/teams/skills")
  public ResponseEntity<String> addSkillToTeam(@RequestParam String teamId,
      @RequestParam String skillName){

    mappingService.addSkillToTeam(teamId,skillName);
    return ResponseEntity.ok("Skill added to team successfully");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Deletes a skill from a team
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = SKILL_TEAM_DELETE_DESCRIPTION)
  @DeleteMapping("/teams/skills")
  public ResponseEntity<String> deleteSkillFromTeam(@RequestParam String teamId,
      @RequestParam String skillName){

    mappingService.deleteSkillFromTeam(teamId,skillName);
    return ResponseEntity.ok("Skill removed from team successfully");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a description to a team
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = TEAMS_DESCRIPTIONS_DESCRIPTION)
  @PostMapping("/teams/descriptions")
  public ResponseEntity<String> addTeamDescription(@RequestParam String teamId,
      @RequestParam String description){

    mappingService.addDescriptionToTeam(teamId,description);
    return ResponseEntity.ok("Description added to team");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Delete a description from a team
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = TEAMS_DESCRIPTIONS_DELETE_DESCRIPTION)
  @DeleteMapping("/teams/descriptions")
  public ResponseEntity<String> deleteTeamDescription(
      @RequestParam String teamId){
    mappingService.deleteDescriptionFromTeam(teamId);
    return ResponseEntity.ok("Description removed from team: " + teamId);
  }

  // ===========================================================================
  // ROLES
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Gets all roles with associated teams and skills
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = ROLES_DESCRIPTION)
  @GetMapping("/roles")
  public List<RoleDTO> getAllRoles(){
    return mappingService.getAllRoles();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a team to a role
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = TEAM_ROLE_DESCRIPTION)
  @PostMapping("/roles/teams")
  public ResponseEntity<String> addTeamToRole(@RequestParam String roleId,
      @RequestParam String teamId){

    mappingService.addTeamToRole(roleId,teamId);
    return ResponseEntity.ok("Team added to role successfully");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Deletes a team from a role
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = TEAM_ROLE_DELETE_DESCRIPTION)
  @DeleteMapping("/roles/teams")
  public ResponseEntity<String> deleteTeamFromRole(@RequestParam String roleId,
      @RequestParam String teamId){

    mappingService.deleteTeamFromRole(roleId,teamId);
    return ResponseEntity.ok("Team removed from role successfully");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a skill to a role
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = SKILL_ROLE_DESCRIPTION)
  @PostMapping("/roles/skills")
  public ResponseEntity<String> addSkillToRole(@RequestParam String roleId,
      @RequestParam String skillId){

    mappingService.addSkillToRole(roleId,skillId);
    return ResponseEntity.ok("Skill added to role successfully");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Deletes a skill from a role
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = SKILL_ROLE_DELETE_DESCRIPTION)
  @DeleteMapping("/roles/skills")
  public ResponseEntity<String> deleteSkillFromRole(@RequestParam String roleId,
      @RequestParam String skillId){

    mappingService.deleteSkillFromRole(roleId,skillId);
    return ResponseEntity.ok("Skill removed from role successfully");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a description to a role
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = ROLES_DESCRIPTIONS_DESCRIPTION)
  @PostMapping("/roles/descriptions")
  public ResponseEntity<String> addRoleDescription(@RequestParam String roleId,
      @RequestParam String description){

    mappingService.addDescriptionToRole(roleId,description);
    return ResponseEntity.ok("Description added to role");
  }

  // ---------------------------------------------------------------------------
  // Purpose: Deletes a description from a role
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = ROLES_DESCRIPTIONS_DELETE_DESCRIPTION)
  @DeleteMapping("/roles/descriptions")
  public ResponseEntity<String> deleteRoleDescription(
      @RequestParam String roleId){
    mappingService.deleteDescriptionFromRole(roleId);
    return ResponseEntity.ok("Description removed from role: " + roleId);
  }

  // ===========================================================================
  // SKILLS
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Gets all skills
  // ---------------------------------------------------------------------------
  @Tag(name = "skills")
  @Operation(summary = SKILLS_DESCRIPTION)
  @GetMapping("/skills")
  public List<String> getAllSkills(){
    return mappingService.getAllSkills();
  }

  // ===========================================================================
  // PERMISSIONS
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Returns all unique permission names
  // ---------------------------------------------------------------------------
  @Tag(name = "permissions")
  @Operation(summary = PERMISSIONS_GET_UNIQUE_DESCRIPTION)
  @GetMapping("/permissions")
  public ResponseEntity<List<String>> getAllUniquePermissions(){
    List<String> permissions = employeePermissionService
        .getAllUniquePermissions();
    return ResponseEntity.ok(permissions);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Returns all active employees and their permissions
  // ---------------------------------------------------------------------------
  @Tag(name = "permissions")
  @Operation(summary = PERMISSIONS_GET_DESCRIPTION)
  @GetMapping("/employees/permissions")
  public ResponseEntity<List<EmployeePermissionsDTO>> getAllActiveEmployeesWithPermissions(){
    List<EmployeePermissionsDTO> employeesWithPermissions = employeePermissionService
        .getAllActiveEmployeesWithPermissions();
    return ResponseEntity.ok(employeesWithPermissions);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a permission to an employee
  // ---------------------------------------------------------------------------
  @Tag(name = "permissions")
  @Operation(summary = PERMISSIONS_ADD_DESCRIPTION)
  @PostMapping("/permissions")
  public ResponseEntity<String> addPermission(@RequestParam Integer employeeId,
      @RequestParam String permission,
      @RequestParam(required = false) String permissionClass){
    EmployeePermission ep = employeePermissionService.addPermission(employeeId,
        permission,permissionClass);
    return ResponseEntity.ok("Permission added: " + ep.getPermission()
        + " to employee ID: " + employeeId);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Deletes a permission from an employee
  // ---------------------------------------------------------------------------
  @Tag(name = "permissions")
  @Operation(summary = PERMISSIONS_DELETE_DESCRIPTION)
  @DeleteMapping("/permissions")
  public ResponseEntity<String> deletePermission(
      @RequestParam Integer employeeId,@RequestParam String permission){
    boolean deleted = employeePermissionService.deletePermission(employeeId,
        permission);
    if (deleted){
      return ResponseEntity.ok("Permission removed: " + permission
          + " from employee ID: " + employeeId);
    } else{
      return ResponseEntity.badRequest()
          .body("Permission not found for employee ID: " + employeeId);
    }
  }

  // ===========================================================================
  // PREFERRED HOURS
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Returns all active employees with their preferred time blocks
  // ---------------------------------------------------------------------------
  @Tag(name = "preferred hours")
  @Operation(summary = HOURS_ALL_DESCRIPTION)
  @GetMapping("/hours")
  public ResponseEntity<List<EmployeesPreferredHoursDTO>> getAllActiveEmployeesWithPreferredHours(){

    List<EmployeesPreferredHoursDTO> result = employeesPreferredHoursService
        .getAllActiveEmployeesWithPreferredHours();

    return ResponseEntity.ok(result);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Get all preferred time blocks for an employee
  // ---------------------------------------------------------------------------
  @Tag(name = "preferred hours")
  @Operation(summary = HOURS_DESCRIPTION)
  @GetMapping("/employees/{employeeId}/hours")
  public ResponseEntity<EmployeesPreferredHoursDTO> getPreferredHours(
      @PathVariable Long employeeId){

    EmployeesPreferredHoursDTO dto = employeesPreferredHoursService
        .getPreferredHoursWithName(employeeId);

    return ResponseEntity.ok(dto);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Add a preferred time block for an employee
  // ---------------------------------------------------------------------------
  @Tag(name = "preferred hours")
  @Operation(summary = HOURS_ADD_DESCRIPTION)
  @PostMapping("/employees/{employeeId}/hours")
  public ResponseEntity<EmployeesPreferredHours> addPreferredHours(
      @PathVariable Long employeeId,@RequestParam String dayOfWeek,
      @RequestParam String startTime,@RequestParam String endTime){

    EmployeesPreferredHours created = employeesPreferredHoursService
        .addPreferredHours(employeeId,dayOfWeek,startTime,endTime);

    return ResponseEntity.ok(created);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Delete a preferred time block for an employee
  // ---------------------------------------------------------------------------
  @Tag(name = "preferred hours")
  @Operation(summary = HOURS_DELETE_DESCRIPTION)
  @DeleteMapping("/employees/{employeeId}/hours/{blockId}")
  public ResponseEntity<String> deletePreferredHours(
      @PathVariable Long employeeId,@PathVariable Long blockId){

    employeesPreferredHoursService.deletePreferredHours(employeeId,blockId);

    return ResponseEntity.ok("Preferred hours block deleted");
  }

  // ===========================================================================
  // DOCUMENTATION
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Retrieves markdown documentation files from GitHub
  // ---------------------------------------------------------------------------
  @Tag(name = "documentation")
  @Operation(summary = "Fetch markdown documentation content")
  @GetMapping("/docs/content")
  public ResponseEntity<String> getMarkdownFile(@RequestParam String fileName){
    return ResponseEntity
        .ok(githubContentService.fetchPublicMarkdown(fileName));
  }

  // ===========================================================================
  // LOGIN FLOW
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Validates user session based on SESSION_ID cookie, deletes expired
  // sessions from the user_session table, returns list of user permissions.
  // ---------------------------------------------------------------------------
  @Tag(name = "login flow")
  @Operation(summary = PERMISSIONS_DESCRIPTION)
  @GetMapping("/user/permissions")
  public ResponseEntity<Map<String, Object>> checkSession(
      @CookieValue(value = "SESSION_ID", required = false) String sessionId){

    employeeLoginService.deleteExpiredSessions(); // clean up expired sessions

    if (sessionId == null || sessionId.isEmpty()){
      return ResponseEntity.status(401)
          .body(Map.of("status","error","message","No session cookie found"));
    }

    Optional<UserSession> session = employeeLoginService.getSession(sessionId);

    if (session.isEmpty() || session.get().isExpired()){
      return ResponseEntity.status(401).body(
          Map.of("status","error","message","Invalid or expired session"));
    }

    int userId = session.get().getUserId();
    EmployeeLogin user = employeeLoginService.findByEid(userId).orElseThrow();

    // Fetch permissions for this user
    List<String> permissions = employeeLoginService
        .getPermissionsByEid(user.getEid());

    return ResponseEntity.ok(Map.of("status","success","eid",user.getEid(),
        "firstName",user.getFirstName(),"lastName",user.getLastName(),
        "permissions",permissions));
  }
}