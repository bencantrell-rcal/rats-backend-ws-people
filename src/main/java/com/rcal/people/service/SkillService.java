package com.rcal.people.service;

import com.rcal.people.entity.Skill;
import com.rcal.people.repository.read.ReadSkillRepository;
import com.rcal.people.repository.write.WriteSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService{

  private final ReadSkillRepository readSkillRepository;
  private final WriteSkillRepository writeSkillRepository;

  public SkillService(ReadSkillRepository readSkillRepository,
      WriteSkillRepository writeSkillRepository) {
    this.readSkillRepository = readSkillRepository;
    this.writeSkillRepository = writeSkillRepository;
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
}
