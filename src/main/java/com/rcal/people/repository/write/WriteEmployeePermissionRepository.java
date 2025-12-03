package com.rcal.people.repository.write;

import com.rcal.people.entity.EmployeePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("writeTransactionManager")
public interface WriteEmployeePermissionRepository
    extends
      JpaRepository<EmployeePermission, Integer>{
}
