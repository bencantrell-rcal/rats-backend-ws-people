package com.rcal.people.repository.read;

import com.rcal.people.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadTeamRepository extends JpaRepository<Team, Integer>{

}
