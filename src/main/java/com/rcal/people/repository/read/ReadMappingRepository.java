package com.rcal.people.repository.read;

import com.rcal.people.entity.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReadMappingRepository extends JpaRepository<Mapping, Long>{

  List<Mapping> findByFromEntityTypeAndToEntityType(String fromType,
      String toType);

  List<Mapping> findByFromEntityIdAndFromEntityTypeAndToEntityType(
      String fromEntityId,String fromEntityType,String toEntityType);

  @Query("""
      SELECT DISTINCT m.fromEntityId
      FROM Mapping m
      WHERE m.fromEntityType = 'TEAM'
        AND m.toEntityType = 'SKILL'
      """)
  List<String> findAllTeamsWithSkills();

  Optional<Mapping> findByFromEntityIdAndFromEntityTypeAndToEntityIdAndToEntityType(
      String fromEntityId,String fromEntityType,String toEntityId,
      String toEntityType);

  List<Mapping> findByToEntityType(String toEntityType);

  List<Mapping> findByFromEntityType(String fromEntityType);
}
