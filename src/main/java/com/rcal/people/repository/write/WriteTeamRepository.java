package com.rcal.people.repository.write;

import com.rcal.people.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("writeTransactionManager")
public interface WriteTeamRepository extends JpaRepository<Team, Integer>{
  @Modifying
  @Query("""
      update Team t
         set t.teamDescription = :description
       where t.teamId = :teamId
      """)
  int updateTeamDescription(Integer teamId,String description);
}
