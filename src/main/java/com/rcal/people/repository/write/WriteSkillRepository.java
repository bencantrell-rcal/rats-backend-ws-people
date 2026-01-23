package com.rcal.people.repository.write;

import com.rcal.people.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("writeTransactionManager")
public interface WriteSkillRepository extends JpaRepository<Skill, Integer>{
  @Modifying
  @Query("""
      update Skill s
         set s.skillDescription = :description
       where s.skillId = :skillId
      """)
  int updateSkillDescription(Integer skillId,String description);
}
