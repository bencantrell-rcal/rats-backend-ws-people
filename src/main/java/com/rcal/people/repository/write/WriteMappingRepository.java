package com.rcal.people.repository.write;

import com.rcal.people.entity.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("writeTransactionManager")
public interface WriteMappingRepository extends JpaRepository<Mapping, Long>{

  void deleteByFromEntityTypeAndFromEntityIdAndToEntityTypeAndToEntityId(
      String fromEntityType,String fromEntityId,String toEntityType,
      String toEntityId);

}