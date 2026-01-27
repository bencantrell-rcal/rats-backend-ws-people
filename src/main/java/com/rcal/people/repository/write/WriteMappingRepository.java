package com.rcal.people.repository.write;

import com.rcal.people.entity.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("writeTransactionManager")
public interface WriteMappingRepository extends JpaRepository<Mapping, Long>{
  int deleteByFromEntityIdAndFromEntityTypeAndToEntityIdAndToEntityType(
      Long fromEntityId,String fromEntityType,Long toEntityId,
      String toEntityType);
}
