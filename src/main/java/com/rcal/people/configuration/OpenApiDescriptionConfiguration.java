package com.rcal.people.configuration;

public class OpenApiDescriptionConfiguration{

  public static final String HEALTH_DESCRIPTION = "Health check endpoint that returns a timestamp and status to verify service availability.";
  public static final String EMPLOYEES_DESCRIPTION = "Gets all employees and RATS employee data.";
  public static final String TEAMS_DESCRIPTION = "Gets all teams and associated skills";
  public static final String PEOPLE_DESCRIPTION = "Gets all people with their associated roles and skills";
  public static final String SKILL_PERSON_DESCRIPTION = "Adds a skill to a person";
  public static final String SKILL_PERSON_DELETE_DESCRIPTION = "Deletes a skill from a person";
  public static final String ROLE_PERSON_DESCRIPTION = "Adds a role to a person";
  public static final String ROLE_PERSON_DELETE_DESCRIPTION = "Deletes a role from a person";
  public static final String SKILL_TEAM_DESCRIPTION = "Adds a skill to a team";
  public static final String SKILL_TEAM_DELETE_DESCRIPTION = "Deletes a skill from a team";
  public static final String TEAM_ROLE_DESCRIPTION = "Adds a team to a role";
  public static final String TEAM_ROLE_DELETE_DESCRIPTION = "Deletes a team from a role";
  public static final String SKILL_ROLE_DESCRIPTION = "Adds a skill to a role";
  public static final String SKILL_ROLE_DELETE_DESCRIPTION = "Deletes a skill from a role";
  public static final String ROLES_DESCRIPTION = "Gets all roles";
  public static final String SKILLS_DESCRIPTION = "Gets all skills";
  public static final String TEAMS_DESCRIPTIONS_DESCRIPTION = "Adds a description to a team";
  public static final String ROLES_DESCRIPTIONS_DESCRIPTION = "Adds a description to a role";
  public static final String TEAMS_DESCRIPTIONS_DELETE_DESCRIPTION = "Deletes a description from a team";
  public static final String ROLES_DESCRIPTIONS_DELETE_DESCRIPTION = "Deletes a description from a role";
  public static final String PERMISSIONS_DELETE_DESCRIPTION = "Deletes a permission from an employee";
  public static final String PERMISSIONS_ADD_DESCRIPTION = "Adds a permission to an employee";
  public static final String PERMISSIONS_GET_UNIQUE_DESCRIPTION = "Returns all unique permission names";
  public static final String PERMISSIONS_GET_DESCRIPTION = "Returns all active employees and their permissions";
}
