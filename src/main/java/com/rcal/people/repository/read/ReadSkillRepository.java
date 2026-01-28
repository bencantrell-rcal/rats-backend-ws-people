package com.rcal.people.repository.read;

import com.rcal.people.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadSkillRepository extends JpaRepository<Skill, Integer>{

  boolean existsBySkillName(String name);
}
