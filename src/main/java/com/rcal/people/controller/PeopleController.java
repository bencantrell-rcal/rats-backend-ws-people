package com.rcal.people.controller;

import com.rcal.people.entity.*;
import com.rcal.people.entity.Mapping;
import com.rcal.people.model.*;
import com.rcal.people.repository.read.ReadRoleRepository;
import com.rcal.people.repository.read.ReadTeamRepository;
import com.rcal.people.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.Exceptions;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
  private final ReadTeamRepository readTeamRepository;
  private final ReadRoleRepository readRoleRepository;

  public PeopleController(EmployeeService employeeService,
      MappingService mappingService, GithubContentService githubContentService,
      EmployeePermissionService employeePermissionService,
      EmployeesPreferredHoursService employeesPreferredHoursService,
      EmployeeLoginService employeeLoginService, PersonService personService,
      TeamService teamService, RoleService roleService,
      SkillService skillService, ReadTeamRepository readTeamRepository,
      ReadRoleRepository readRoleRepository) {
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
    this.readTeamRepository = readTeamRepository;
    this.readRoleRepository = readRoleRepository;
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
  // Purpose: Returns all people (summary view, paginated)
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = "Returns all people (summary view)")
  @GetMapping("people")
  public ResponseEntity<Page<PersonSummaryDTO>> getAllPeople(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "25") int size){

    Pageable pageable = PageRequest.of(page,size);

    Page<Person> peoplePage = personService.getAllPeople(pageable);

    Page<PersonSummaryDTO> summaryPage = peoplePage
        .map(person -> personService.buildPersonSummary(person.getPersonId()));

    return ResponseEntity.ok(summaryPage);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Returns a specific person by ID
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = "Returns a person by ID")
  @GetMapping("people/{personId}")
  public ResponseEntity<?> getPersonById(@PathVariable Integer personId){

    try{
      Person person = personService.getPersonById(personId);

      List<Mapping> roleMappings = mappingService.getHigherEntities(
          Long.valueOf(personId),EntityTypes.PERSON,EntityTypes.ROLE);

      List<RoleBasicDTO> roles = new ArrayList<>();

      for (Mapping mapping : roleMappings){
        roles.add((RoleBasicDTO) mappingService.getBasicByIdAndEntityType(
            mapping.getToEntityId(),EntityTypes.ROLE));
      }

      List<TeamBasicDTO> teams = new ArrayList<>();
      List<Mapping> teamMappings = new ArrayList<>();

      for (RoleBasicDTO role : roles){
        teamMappings.addAll(mappingService.getLowerEntities(
            Long.valueOf(role.getRoleId()),EntityTypes.ROLE,EntityTypes.TEAM));
      }

      for (Mapping mapping : teamMappings){
        teams.add((TeamBasicDTO) mappingService.getBasicByIdAndEntityType(
            mapping.getFromEntityId(),EntityTypes.TEAM));
      }

      List<SkillBasicDTO> skills = new ArrayList<>();

      List<Mapping> skillMappings = mappingService.getLowerEntities(
          Long.valueOf(personId),EntityTypes.PERSON,EntityTypes.SKILL);

      for (Mapping mapping : skillMappings){
        skills.add((SkillBasicDTO) mappingService.getBasicByIdAndEntityType(
            mapping.getFromEntityId(),EntityTypes.SKILL));
      }

      PersonSummaryDTO result = new PersonSummaryDTO(person.getName(),
          Long.valueOf(person.getPersonId()), teams, roles, skills);

      return ResponseEntity.ok(result);
    } catch (Exception e){
      return ResponseEntity.notFound().build();
    }
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
  // Purpose: Deletes a person by ID (and cleans up mappings)
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = "Deletes a person by ID")
  @DeleteMapping("/people/{personId}")
  public ResponseEntity<Void> deletePerson(@PathVariable Integer personId){

    // Remove all mappings involving this person
    mappingService.deleteAllMappingsForEntity(personId.longValue(),
        EntityTypes.PERSON);

    // Delete the person itself
    personService.deletePerson(personId);

    return ResponseEntity.noContent().build();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a person to a role
  // ---------------------------------------------------------------------------
  @Tag(name = "people")
  @Operation(summary = "Adds a person to a role")
  @PostMapping("/people/{personId}/roles/{roleId}")
  public ResponseEntity<Void> addRoleToPerson(@PathVariable Integer personId,
      @PathVariable Integer roleId){

    mappingService.createMapping(personId.longValue(),EntityTypes.PERSON,
        roleId.longValue(),EntityTypes.ROLE);

    return ResponseEntity.noContent().build();
  }

  // ===========================================================================
  // TEAMS
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Returns all teams (id + name only)
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = "Returns all teams (id and name only)")
  @GetMapping("/teams")
  public ResponseEntity<List<TeamBasicDTO>> getAllTeams(){

    List<TeamBasicDTO> teamBasics = teamService.getAllTeams().stream()
        .map(team -> new TeamBasicDTO(team.getTeamId(), team.getTeamName()))
        .toList();

    return ResponseEntity.ok(teamBasics);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Returns a specific team by ID
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = "Returns a team by ID")
  @GetMapping("teams/{teamId}")
  public ResponseEntity<?> getTeamById(@PathVariable Integer teamId){
    try{
      Team team = readTeamRepository.findById(teamId).orElseThrow(
          () -> new RuntimeException("Team not found with id: " + teamId));

      List<Mapping> roleMappings = mappingService.getHigherEntities(
          Long.valueOf(teamId),EntityTypes.TEAM,EntityTypes.ROLE);

      List<RoleBasicDTO> roles = new ArrayList<>();

      for (Mapping mapping : roleMappings){
        roles.add((RoleBasicDTO) mappingService.getBasicByIdAndEntityType(
            mapping.getToEntityId(),EntityTypes.ROLE));
      }

      List<Person> people = new ArrayList<>();
      List<Mapping> peopleMappings = new ArrayList<>();

      for (RoleBasicDTO role : roles){
        peopleMappings.addAll(
            mappingService.getLowerEntities(Long.valueOf(role.getRoleId()),
                EntityTypes.ROLE,EntityTypes.PERSON));
      }

      for (Mapping mapping : peopleMappings){
        people.add((Person) mappingService.getBasicByIdAndEntityType(
            mapping.getFromEntityId(),EntityTypes.PERSON));
      }

      List<SkillBasicDTO> skills = new ArrayList<>();

      List<Mapping> skillMappings = mappingService.getLowerEntities(
          Long.valueOf(teamId),EntityTypes.TEAM,EntityTypes.SKILL);

      for (Mapping mapping : skillMappings){
        skills.add((SkillBasicDTO) mappingService.getBasicByIdAndEntityType(
            mapping.getFromEntityId(),EntityTypes.SKILL));
      }

      TeamSummaryDTO result = new TeamSummaryDTO(team.getTeamName(),
          team.getTeamDescription(), roles, people, skills);

      return ResponseEntity.ok(result);
    } catch (Exception e){
      return ResponseEntity.notFound().build();
    }
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
  // Purpose: Deletes a team by ID (and cleans up mappings)
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = "Deletes a team by ID")
  @DeleteMapping("/teams/{teamId}")
  public ResponseEntity<Void> deleteTeam(@PathVariable Integer teamId){

    // Remove all mappings involving this team
    mappingService.deleteAllMappingsForEntity(teamId.longValue(),
        EntityTypes.TEAM);

    // Delete the team itself
    teamService.deleteTeam(teamId);

    return ResponseEntity.noContent().build();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a team to a role
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = "Adds a team to a role")
  @PostMapping("/teams/{teamId}/roles/{roleId}")
  public ResponseEntity<Void> addRoleToTeam(@PathVariable Integer teamId,
      @PathVariable Integer roleId){

    mappingService.createMapping(teamId.longValue(),EntityTypes.TEAM,
        roleId.longValue(),EntityTypes.ROLE);

    return ResponseEntity.noContent().build();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Updates a team's description
  // ---------------------------------------------------------------------------
  @Tag(name = "teams")
  @Operation(summary = "Updates a team's description")
  @PutMapping("/teams/{teamId}/description")
  public ResponseEntity<Void> updateTeamDescription(
      @PathVariable Integer teamId,
      @RequestBody(required = false) String description){

    teamService.updateTeamDescription(teamId,description);
    return ResponseEntity.noContent().build();
  }

  // ===========================================================================
  // ROLES
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Returns all roles (id + name only)
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = "Returns all roles (id and name only)")
  @GetMapping("/roles")
  public ResponseEntity<List<RoleBasicDTO>> getAllRoles(){

    List<RoleBasicDTO> roleBasics = roleService.getAllRoles().stream()
        .map(role -> new RoleBasicDTO(role.getRoleId(), role.getRoleName()))
        .toList();

    return ResponseEntity.ok(roleBasics);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Returns a specific role by ID
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = "Returns a role by ID")
  @GetMapping("roles/{roleId}")
  public ResponseEntity<?> getRoleById(@PathVariable Integer roleId){
    try{
      Role role = readRoleRepository.findById(roleId).orElseThrow(
          () -> new RuntimeException("Role not found with id: " + roleId));

      List<Mapping> teamMappings = mappingService.getLowerEntities(
          Long.valueOf(roleId),EntityTypes.ROLE,EntityTypes.TEAM);

      List<TeamBasicDTO> teams = new ArrayList<>();

      for (Mapping mapping : teamMappings){
        teams.add((TeamBasicDTO) mappingService.getBasicByIdAndEntityType(
            mapping.getFromEntityId(),EntityTypes.TEAM));
      }

      List<Mapping> peopleMappings = mappingService.getLowerEntities(
          Long.valueOf(roleId),EntityTypes.ROLE,EntityTypes.PERSON);

      List<Person> people = new ArrayList<>();

      for (Mapping mapping : peopleMappings){
        people.add((Person) mappingService.getBasicByIdAndEntityType(
            mapping.getFromEntityId(),EntityTypes.PERSON));
      }

      List<Mapping> skillMappings = mappingService.getLowerEntities(
          Long.valueOf(roleId),EntityTypes.ROLE,EntityTypes.SKILL);

      List<SkillBasicDTO> skills = new ArrayList<>();

      for (Mapping mapping : skillMappings){
        skills.add((SkillBasicDTO) mappingService.getBasicByIdAndEntityType(
            mapping.getFromEntityId(),EntityTypes.SKILL));
      }

      RoleSummaryDTO result = new RoleSummaryDTO(role.getRoleName(),
          role.getRoleDescription(), teams, skills, people);

      return ResponseEntity.ok(result);
    } catch (Exception e){
      return ResponseEntity.notFound().build();
    }
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
  // Purpose: Deletes a role by ID (and cleans up mappings)
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = "Deletes a role by ID")
  @DeleteMapping("/roles/{roleId}")
  public ResponseEntity<Void> deleteRole(@PathVariable Integer roleId){

    // Remove all mappings involving this role
    mappingService.deleteAllMappingsForEntity(roleId.longValue(),
        EntityTypes.ROLE);

    // Delete the role itself
    roleService.deleteRole(roleId);

    return ResponseEntity.noContent().build();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Updates a role's description
  // ---------------------------------------------------------------------------
  @Tag(name = "roles")
  @Operation(summary = "Updates a role's description")
  @PutMapping("/roles/{roleId}/description")
  public ResponseEntity<Void> updateRoleDescription(
      @PathVariable Integer roleId,
      @RequestBody(required = false) String description){

    roleService.updateRoleDescription(roleId,description);
    return ResponseEntity.noContent().build();
  }

  // ===========================================================================
  // SKILLS
  // ===========================================================================

  // ---------------------------------------------------------------------------
  // Purpose: Returns all skills (id + name only)
  // ---------------------------------------------------------------------------
  @Tag(name = "skills")
  @Operation(summary = "Returns all skills (id and name only)")
  @GetMapping("/skills")
  public ResponseEntity<List<SkillBasicDTO>> getAllSkills(){

    List<SkillBasicDTO> skillBasics = skillService.getAllSkills().stream().map(
        skill -> new SkillBasicDTO(skill.getSkillId(), skill.getSkillName()))
        .toList();

    return ResponseEntity.ok(skillBasics);
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
  // Purpose: Deletes a skill by ID (and cleans up mappings)
  // ---------------------------------------------------------------------------
  @Tag(name = "skills")
  @Operation(summary = "Deletes a skill by ID")
  @DeleteMapping("/skills/{skillId}")
  public ResponseEntity<Void> deleteSkill(@PathVariable Integer skillId){

    // Remove all mappings involving this skill
    mappingService.deleteAllMappingsForEntity(skillId.longValue(),
        EntityTypes.SKILL);

    // Delete the skill itself
    skillService.deleteSkill(skillId);

    return ResponseEntity.noContent().build();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a skill to a team
  // ---------------------------------------------------------------------------
  @Tag(name = "skills")
  @Operation(summary = "Adds a skill to a team")
  @PostMapping("/skills/{skillId}/teams/{teamId}")
  public ResponseEntity<Void> addSkillToTeam(@PathVariable Integer skillId,
      @PathVariable Integer teamId){

    mappingService.createMapping(skillId.longValue(),EntityTypes.SKILL,
        teamId.longValue(),EntityTypes.TEAM);

    return ResponseEntity.noContent().build();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a skill to a role
  // ---------------------------------------------------------------------------
  @Tag(name = "skills")
  @Operation(summary = "Adds a skill to a role")
  @PostMapping("/skills/{skillId}/roles/{roleId}")
  public ResponseEntity<Void> addSkillToRole(@PathVariable Integer skillId,
      @PathVariable Integer roleId){

    mappingService.createMapping(skillId.longValue(),EntityTypes.SKILL,
        roleId.longValue(),EntityTypes.ROLE);

    return ResponseEntity.noContent().build();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Adds a skill to a person
  // ---------------------------------------------------------------------------
  @Tag(name = "skills")
  @Operation(summary = "Adds a skill to a person")
  @PostMapping("/skills/{skillId}/people/{personId}")
  public ResponseEntity<Void> addSkillToPerson(@PathVariable Integer skillId,
      @PathVariable Integer personId){

    mappingService.createMapping(skillId.longValue(),EntityTypes.SKILL,
        personId.longValue(),EntityTypes.PERSON);

    return ResponseEntity.noContent().build();
  }

  // ---------------------------------------------------------------------------
  // Purpose: Updates a skill's description
  // ---------------------------------------------------------------------------
  @Tag(name = "skills")
  @Operation(summary = "Updates a skill's description")
  @PutMapping("/skills/{skillId}/description")
  public ResponseEntity<Void> updateSkillDescription(
      @PathVariable Integer skillId,
      @RequestBody(required = false) String description){

    skillService.updateSkillDescription(skillId,description);
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