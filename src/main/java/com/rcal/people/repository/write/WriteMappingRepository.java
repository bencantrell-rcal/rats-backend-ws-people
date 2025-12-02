package com.rcal.people.repository.write;

import com.rcal.people.entity.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("writeTransactionManager")
public interface WriteMappingRepository extends JpaRepository<Mapping, Long>{

  // Delete a specific mapping (PERSON → SKILL, ROLE → TEAM, etc.)
  void deleteByFromEntityTypeAndFromEntityIdAndToEntityTypeAndToEntityId(
      String fromEntityType,String fromEntityId,String toEntityType,
      String toEntityId);

  // Delete all mappings of a target type for the given entity
  void deleteByFromEntityTypeAndFromEntityIdAndToEntityType(
      String fromEntityType,String fromEntityId,String toEntityType);
}
