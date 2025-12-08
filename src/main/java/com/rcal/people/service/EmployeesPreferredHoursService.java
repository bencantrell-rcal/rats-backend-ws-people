package com.rcal.people.service;

import com.rcal.people.entity.Employee;
import com.rcal.people.entity.EmployeesPreferredHours;
import com.rcal.people.model.EmployeesPreferredHoursDTO;
import com.rcal.people.repository.read.ReadEmployeeRepository;
import com.rcal.people.repository.read.ReadEmployeesPreferredHoursRepository;
import com.rcal.people.repository.write.WriteEmployeesPreferredHoursRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeesPreferredHoursService{

  private final ReadEmployeesPreferredHoursRepository readEmployeesPreferredHoursRepository;
  private final WriteEmployeesPreferredHoursRepository writeEmployeesPreferredHoursRepository;

  private final ReadEmployeeRepository readEmployeeRepository;

  public EmployeesPreferredHoursService(
      ReadEmployeesPreferredHoursRepository readEmployeesPreferredHoursRepository,
      WriteEmployeesPreferredHoursRepository writeEmployeesPreferredHoursRepository,
      ReadEmployeeRepository readEmployeeRepository) {
    this.readEmployeesPreferredHoursRepository = readEmployeesPreferredHoursRepository;
    this.writeEmployeesPreferredHoursRepository = writeEmployeesPreferredHoursRepository;
    this.readEmployeeRepository = readEmployeeRepository;
  }

  // ---------------------------------------------------------------------------
  // GET preferred hours for an employee
  // ---------------------------------------------------------------------------
  public EmployeesPreferredHoursDTO getPreferredHoursWithName(Long employeeId){

    Employee employee = readEmployeeRepository.findById(employeeId.intValue());
    List<EmployeesPreferredHours> blocks = readEmployeesPreferredHoursRepository
        .findByEmployeeIdOrderByEmployeeIdAscDayOfWeekAscStartTimeAsc(
            employeeId);

    EmployeesPreferredHoursDTO dto = new EmployeesPreferredHoursDTO();
    dto.setEmployeeId(employeeId);
    dto.setFirstName(employee.getFirstName());
    dto.setLastName(employee.getLastName());
    dto.setPreferredHours(blocks);

    return dto;
  }

  // ---------------------------------------------------------------------------
  // ADD a time block for an employee ID
  // ---------------------------------------------------------------------------
  public EmployeesPreferredHours addPreferredHours(Long employeeId,
      String dayOfWeek,String startTime,String endTime){

    EmployeesPreferredHours eph = new EmployeesPreferredHours();
    eph.setEmployeeId(employeeId);
    eph.setDayOfWeek(dayOfWeek);
    eph.setStartTime(java.time.LocalTime.parse(startTime));
    eph.setEndTime(java.time.LocalTime.parse(endTime));

    return writeEmployeesPreferredHoursRepository.save(eph);
  }

  // ---------------------------------------------------------------------------
  // DELETE a time block by ID
  // ---------------------------------------------------------------------------
  public void deletePreferredHours(Long employeeId,Long blockId){
    writeEmployeesPreferredHoursRepository.deleteByEmployeeIdAndId(employeeId,
        blockId);
  }

  // ---------------------------------------------------------------------------
  // GET: All active employees with their preferred hours
  // ---------------------------------------------------------------------------
  public List<EmployeesPreferredHoursDTO> getAllActiveEmployeesWithPreferredHours(){

    List<Employee> activeEmployees = readEmployeeRepository
        .findByActiveStatusOrderByEidAsc("Y");

    // Get all preferred blocks, globally sorted
    List<EmployeesPreferredHours> allBlocks = readEmployeesPreferredHoursRepository
        .findAllOrdered();

    // Group by employeeId
    Map<Long, List<EmployeesPreferredHours>> blocksByEmployee = allBlocks
        .stream()
        .collect(Collectors.groupingBy(EmployeesPreferredHours::getEmployeeId));

    List<EmployeesPreferredHoursDTO> result = new ArrayList<>();

    for (Employee emp : activeEmployees){
      EmployeesPreferredHoursDTO dto = new EmployeesPreferredHoursDTO();
      dto.setEmployeeId(emp.getEid().longValue());
      dto.setFirstName(emp.getFirstName());
      dto.setLastName(emp.getLastName());
      dto.setPreferredHours(
          blocksByEmployee.getOrDefault(emp.getEid().longValue(),List.of()));
      result.add(dto);
    }

    return result;
  }

}
