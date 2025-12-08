package com.rcal.people.repository.read;

import com.rcal.people.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadEmployeeRepository
    extends
      JpaRepository<Employee, Integer>{

  Employee findById(int id);

  List<Employee> findByActiveStatusOrderByEidAsc(String activeStatus);

}
