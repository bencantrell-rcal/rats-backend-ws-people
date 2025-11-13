package com.rcal.people.controller;

import com.rcal.people.entity.Employee;
import com.rcal.people.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rcal.people.configuration.OpenApiDescriptionConfiguration.*;

@RestController
public class PeopleController{

  private final EmployeeService employeeService;

  public PeopleController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  // ---------------------------------------------------------------------------
  // Purpose: This method serves as a check to verify if the app is exposing
  // its endpoints correctly. When this endpoint is hit, it responds "healthy"
  // with a timestamp attached
  // ---------------------------------------------------------------------------
  @Operation(summary = HEALTH_DESCRIPTION)
  @GetMapping("/health")
  public ResponseEntity<Map<String, String>> health(){
    DateTimeFormatter FORMATTER = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String timestamp = LocalDateTime.now(ZoneOffset.UTC).format(FORMATTER);

    Map<String, String> response = new HashMap<>();
    response.put("status","Healthy");
    response.put("timestamp",timestamp);

    System.out.println("Healthy " + timestamp);
    return ResponseEntity.ok(response);
  }

  // ---------------------------------------------------------------------------
  // Purpose: Gets all employees and RATS employee data
  // ---------------------------------------------------------------------------
  @Operation(summary = EMPLOYEES_DESCRIPTION)
  @GetMapping("/employees")
  public List<Employee> getAllEmployees(){
    return employeeService.getAllEmployees();
  }

}
