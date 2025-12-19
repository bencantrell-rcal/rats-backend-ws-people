package com.rcal.people.repository.read;

import com.rcal.people.entity.EmployeePermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadEmployeePermissionsRepository
    extends
      JpaRepository<EmployeePermissions, Integer>{

  List<EmployeePermissions> findByEid(Integer eid);

}
