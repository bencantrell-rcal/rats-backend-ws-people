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
  private final PersonService personService;
  private final TeamService teamService;
  private final RoleService roleService;
  private final SkillService skillService;

  public PeopleController(EmployeeService employeeService,
      MappingService mappingService, GithubContentService githubContentService,
      EmployeePermissionService employeePermissionService,
      EmployeesPreferredHoursService employeesPreferredHoursService,
      EmployeeLoginService employeeLoginService, PersonService personService,
      TeamService teamService, RoleService roleService,
      SkillService skillService) {
    this.employeeService = employeeService;
    this.mappingService = mappingService;
    this.githubContentService = githubContentService;
    this.employeePermissionService = employeePermissionService;
    this.employeesPreferredHoursService = employeesPreferredHoursService;
    this.employeeLoginService = employeeLoginService;
    this.personService = personService;
    this.teamService = teamService;
    this.roleService = roleService;
    this.skillService = skillService;
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
  // PEOPLE (ROLES & SKILLS)
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Returns all people
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = "Returns all people")
  @GetMapping("people")
  public ResponseEntity<List<Person>> getAllPeople(){
    List<Person> people = personService.getAllPeople();
    return ResponseEntity.ok(people);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Returns a specific person by ID
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = "Returns a person by ID")
  @GetMapping("people/{personId}")
  public ResponseEntity<Person> getPersonById(@PathVariable Integer personId){

    return personService.getPersonByIdOptional(personId).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // ---------------------------------------------------------------------------
  // Purpose: Creates a new person
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = "Creates a new person")
  @PostMapping("/people")
  public ResponseEntity<Person> createPerson(@RequestBody String name){

    Person createdPerson = personService.createPerson(name);
    return ResponseEntity.ok(createdPerson);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Deletes a person by ID
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = "Deletes a person by ID")
  @DeleteMapping("/people/{personId}")
  public ResponseEntity<Void> deletePerson(@PathVariable Integer personId){

    personService.deletePerson(personId);
    return ResponseEntity.noContent().build();
  }

  // ===========================================================================
  // TEAMS
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Returns all teams
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = "Returns all teams")
  @GetMapping("teams")
  public ResponseEntity<List<Team>> getAllTeams(){

    List<Team> teams = teamService.getAllTeams();
    return ResponseEntity.ok(teams);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Returns a specific team by ID
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = "Returns a team by ID")
  @GetMapping("teams/{teamId}")
  public ResponseEntity<Team> getTeamById(@PathVariable Integer teamId){

    return teamService.getTeamByIdOptional(teamId).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // ---------------------------------------------------------------------------
  // Purpose: Creates a new team
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = "Creates a new team")
  @PostMapping("/teams")
  public ResponseEntity<Team> createTeam(@RequestParam String teamName,
      @RequestParam(required = false) String teamDescription){

    Team createdTeam = teamService.createTeam(teamName,teamDescription);
    return ResponseEntity.ok(createdTeam);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Deletes a team by ID
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = "Deletes a team by ID")
  @DeleteMapping("/teams/{teamId}")
  public ResponseEntity<Void> deleteTeam(@PathVariable Integer teamId){

    teamService.deleteTeam(teamId);
    return ResponseEntity.noContent().build();
  }

  // ===========================================================================
  // ROLES
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Returns all roles
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = "Returns all roles")
  @GetMapping("roles")
  public ResponseEntity<List<Role>> getAllRoles(){

    List<Role> roles = roleService.getAllRoles();
    return ResponseEntity.ok(roles);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Returns a specific role by ID
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = "Returns a role by ID")
  @GetMapping("roles/{roleId}")
  public ResponseEntity<Role> getRoleById(@PathVariable Integer roleId){

    return roleService.getRoleByIdOptional(roleId).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // ---------------------------------------------------------------------------
  // Purpose: Creates a new role
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = "Creates a new role")
  @PostMapping("/roles")
  public ResponseEntity<Role> createRole(@RequestParam String roleName,
      @RequestParam(required = false) String roleDescription){

    Role createdRole = roleService.createRole(roleName,roleDescription);
    return ResponseEntity.ok(createdRole);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Deletes a role by ID
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = "Deletes a role by ID")
  @DeleteMapping("/roles/{roleId}")
  public ResponseEntity<Void> deleteRole(@PathVariable Integer roleId){

    roleService.deleteRole(roleId);
    return ResponseEntity.noContent().build();
  }

  // ===========================================================================
  // SKILLS
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Returns all skills
  // ---------------------------------------------------------------------------
  @Tag(name = "skills")
  @Operation(summary = "Returns all skills")
  @GetMapping("skills")
  public ResponseEntity<List<Skill>> getAllSkills(){

    List<Skill> skills = skillService.getAllSkills();
    return ResponseEntity.ok(skills);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Returns a specific skill by ID
  // ---------------------------------------------------------------------------
  @Tag(name = "skills")
  @Operation(summary = "Returns a skill by ID")
  @GetMapping("skills/{skillId}")
  public ResponseEntity<Skill> getSkillById(@PathVariable Integer skillId){

    return skillService.getSkillByIdOptional(skillId).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // ---------------------------------------------------------------------------
  // Purpose: Creates a new skill
  // ---------------------------------------------------------------------------
  @Tag(name = "skills")
  @Operation(summary = "Creates a new skill")
  @PostMapping("/skills")
  public ResponseEntity<Skill> createSkill(@RequestParam String skillName,
      @RequestParam(required = false) String skillDescription){

    Skill createdSkill = skillService.createSkill(skillName,skillDescription);
    return ResponseEntity.ok(createdSkill);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Deletes a skill by ID
  // ---------------------------------------------------------------------------
  @Tag(name = "skills")
  @Operation(summary = "Deletes a skill by ID")
  @DeleteMapping("/skills/{skillId}")
  public ResponseEntity<Void> deleteSkill(@PathVariable Integer skillId){

    skillService.deleteSkill(skillId);
    return ResponseEntity.noContent().build();
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
}