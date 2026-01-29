package com.rcal.people.service;

import com.rcal.people.entity.Team;
import com.rcal.people.repository.read.ReadTeamRepository;
import com.rcal.people.repository.write.WriteTeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService{

  private final ReadTeamRepository readTeamRepository;
  private final WriteTeamRepository writeTeamRepository;

  public TeamService(ReadTeamRepository readTeamRepository,
      WriteTeamRepository writeTeamRepository) {
    this.readTeamRepository = readTeamRepository;
    this.writeTeamRepository = writeTeamRepository;
  }

  // ---------------------------------------------------------------------------
  // Get all teams
  // ---------------------------------------------------------------------------
  public List<Team> getAllTeams(){
    return readTeamRepository.findAll();
  }

  // ---------------------------------------------------------------------------
  // Get a specific team by ID (Optional)
  // ---------------------------------------------------------------------------
  public Optional<Team> getTeamByIdOptional(Integer teamId){
    return readTeamRepository.findById(teamId);
  }

  // ---------------------------------------------------------------------------
  // Create a new team
  // ---------------------------------------------------------------------------
  public Team createTeam(String teamName,String teamDescription){

    if (teamName == null){
      throw new IllegalArgumentException("Team name must not be null");
    }

    // Keep only alphabetic characters and spaces
    String sanitizedName = teamName.replaceAll("[^A-Za-z ]","").trim()
        .replaceAll("\\s+"," ");

    if (sanitizedName.isEmpty()){
      throw new IllegalArgumentException(
          "Team name must contain alphabetic characters");
    }

    if (teamDescription != null){
      // Allow letters, numbers, basic punctuation, and spaces (NO quotes)
      teamDescription = teamDescription.replaceAll("[^A-Za-z0-9 .,!?()-]","")
          .trim().replaceAll("\\s+"," ");
    }

    Team team = new Team();
    team.setTeamName(sanitizedName);
    team.setTeamDescription(teamDescription);

    return writeTeamRepository.save(team);
  }

  // ---------------------------------------------------------------------------
  // Delete a team by ID
  // ---------------------------------------------------------------------------
  public void deleteTeam(Integer teamId){
    writeTeamRepository.deleteById(teamId);
  }

  // ---------------------------------------------------------------------------
  // Update team description
  // ---------------------------------------------------------------------------
  public void updateTeamDescription(Integer teamId,String description){

    if (teamId == null){
      throw new IllegalArgumentException("Team ID must not be null");
    }

    if (description != null){
      // Allow letters, numbers, basic punctuation, and spaces (NO quotes)
      description = description.replaceAll("[^A-Za-z0-9 .,!?()-]","").trim()
          .replaceAll("\\s+"," ");
    }

    int updatedRows = writeTeamRepository.updateTeamDescription(teamId,
        description);

    if (updatedRows == 0){
      throw new RuntimeException("Team not found with id: " + teamId);
    }
  }

}
