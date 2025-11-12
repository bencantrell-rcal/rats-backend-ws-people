package com.rcal.people.repository.read;

import com.rcal.people.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadEmployeeRepository
    extends
      JpaRepository<Employee, Integer>{

}
