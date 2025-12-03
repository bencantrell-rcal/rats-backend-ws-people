package com.rcal.people.repository.read;

import com.rcal.people.entity.EmployeePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadEmployeePermissionRepository
    extends
      JpaRepository<EmployeePermission, Integer>{
}
