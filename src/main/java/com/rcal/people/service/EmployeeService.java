package com.rcal.people.service;

import com.rcal.people.entity.Employee;
import com.rcal.people.repository.read.ReadEmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService{

  private final ReadEmployeeRepository readEmployeeRepository;

  public EmployeeService(ReadEmployeeRepository readEmployeeRepository) {
    this.readEmployeeRepository = readEmployeeRepository;
  }

  public List<Employee> getAllEmployees(){
    return readEmployeeRepository.findAll();
  }
}
