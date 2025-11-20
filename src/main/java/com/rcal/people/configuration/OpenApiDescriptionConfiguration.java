package com.rcal.people.configuration;

public class OpenApiDescriptionConfiguration{
  public static final String HEALTH_DESCRIPTION = "Health check endpoint that returns a timestamp and status to verify service availability.";
  public static final String EMPLOYEES_DESCRIPTION = "Gets all employees and RATS employee data.";
  public static final String TEAMS_DESCRIPTION = "Gets all teams and associated skills";
  public static final String PEOPLE_DESCRIPTION = "Gets all people and associated teams and skills";
  public static final String SKILL_PERSON_DESCRIPTION = "Adds a skill to a person";
  public static final String SKILL_TEAM_DESCRIPTION = "Adds a skill to a team";
  public static final String TEAM_PERSON_DESCRIPTION = "Adds a team to a person";
  public static final String SKILL_PERSON_DELETE_DESCRIPTION = "Deletes a skill from a person";
  public static final String SKILL_TEAM_DELETE_DESCRIPTION = "Deletes a skill from a team";
  public static final String TEAM_PERSON_DELETE_DESCRIPTION = "Deletes a team from a person";
}
