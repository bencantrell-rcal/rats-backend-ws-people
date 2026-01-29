package com.rcal.people.service;

import com.rcal.people.entity.Skill;
import com.rcal.people.repository.read.ReadSkillRepository;
import com.rcal.people.repository.write.WriteSkillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SkillService{

  private final ReadSkillRepository readSkillRepository;
  private final WriteSkillRepository writeSkillRepository;
  private final GithubRepoService githubRepoService;

  public SkillService(ReadSkillRepository readSkillRepository,
      WriteSkillRepository writeSkillRepository,
      GithubRepoService githubRepoService) {
    this.readSkillRepository = readSkillRepository;
    this.writeSkillRepository = writeSkillRepository;
    this.githubRepoService = githubRepoService;
  }

  // ---------------------------------------------------------------------------
  // Get all skills
  // ---------------------------------------------------------------------------
  public List<Skill> getAllSkills(){
    return readSkillRepository.findAll();
  }

  // ---------------------------------------------------------------------------
  // Get a specific skill by ID (Optional)
  // ---------------------------------------------------------------------------
  public Optional<Skill> getSkillByIdOptional(Integer skillId){
    return readSkillRepository.findById(skillId);
  }

  // ---------------------------------------------------------------------------
  // Create a new skill
  // ---------------------------------------------------------------------------
  public Skill createSkill(String skillName,String skillDescription){

    if (skillName == null){
      throw new IllegalArgumentException("Skill name must not be null");
    }

    // Keep only alphabetic characters and spaces
    String sanitizedName = skillName.replaceAll("[^A-Za-z ]","").trim()
        .replaceAll("\\s+"," ");

    if (sanitizedName.isEmpty()){
      throw new IllegalArgumentException(
          "Skill name must contain alphabetic characters");
    }

    if (skillDescription != null){
      // Allow letters, numbers, basic punctuation, and spaces (NO quotes)
      skillDescription = skillDescription.replaceAll("[^A-Za-z0-9 .,!?()-]","")
          .trim().replaceAll("\\s+"," ");
    }

    Skill skill = new Skill();
    skill.setSkillName(sanitizedName);
    skill.setSkillDescription(skillDescription);

    return writeSkillRepository.save(skill);
  }

  // ---------------------------------------------------------------------------
  // Delete a skill by ID
  // ---------------------------------------------------------------------------
  public void deleteSkill(Integer skillId){
    writeSkillRepository.deleteById(skillId);
  }

  // ---------------------------------------------------------------------------
  // Update skill description
  // ---------------------------------------------------------------------------
  public void updateSkillDescription(Integer skillId,String description){

    if (skillId == null){
      throw new IllegalArgumentException("Skill ID must not be null");
    }

    if (description != null){
      // Allow letters, numbers, basic punctuation, and spaces (NO quotes)
      description = description.replaceAll("[^A-Za-z0-9 .,!?()-]","").trim()
          .replaceAll("\\s+"," ");
    }

    int updatedRows = writeSkillRepository.updateSkillDescription(skillId,
        description);

    if (updatedRows == 0){
      throw new RuntimeException("Skill not found with id: " + skillId);
    }
  }

  public List<Skill> createSkillsFromGithub(long installationId,String repo,
      String skillsDirectory,String branch) throws Exception{

    List<String> skillNames = githubRepoService
        .listMarkdownFiles(installationId,repo,skillsDirectory,branch);

    List<Skill> created = new ArrayList<>();

    for (String name : skillNames){
      if (!readSkillRepository.existsBySkillName(name)){
        Skill skill = new Skill();
        skill.setSkillName(name);
        created.add(writeSkillRepository.save(skill));
      }
    }

    return created;
  }

}
