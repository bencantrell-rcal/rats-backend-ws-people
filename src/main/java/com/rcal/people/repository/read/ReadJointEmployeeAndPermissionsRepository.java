package com.rcal.people.repository.read;

import com.rcal.people.entity.EmployeePermission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadJointEmployeeAndPermissionsRepository
    extends
      org.springframework.data.jpa.repository.JpaRepository<EmployeePermission, Integer>{

  @Query(value = "SELECT e.eid, e.first AS firstName, e.last AS lastName, "
      + "GROUP_CONCAT(ep.Permission) AS permissions " + "FROM employees e "
      + "LEFT JOIN employees_permissions ep ON e.eid = ep.eid "
      + "WHERE e.Active = 'Y' "
      + "GROUP BY e.eid, e.first, e.last", nativeQuery = true)
  List<Object[]> findAllActiveEmployeesWithPermissionsNative();
}
