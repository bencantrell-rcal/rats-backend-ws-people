package com.rcal.people.repository.read;

import com.rcal.people.entity.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadMappingRepository extends JpaRepository<Mapping, Long>{

  List<Mapping> findByFromEntityTypeAndFromEntityIdAndToEntityType(
      String fromEntityType,Long fromEntityId,String toEntityType);

  List<Mapping> findByToEntityTypeAndToEntityIdAndFromEntityType(
      String toEntityType,Long toEntityId,String fromEntityType);

  List<Mapping> findByFromEntityTypeAndFromEntityId(String fromEntityType,
      Long fromEntityId);

  List<Mapping> findByToEntityTypeAndToEntityId(String toEntityType,
      Long toEntityId);
}
