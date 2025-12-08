package com.rcal.people.repository.read;

import com.rcal.people.entity.EmployeesPreferredHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadEmployeesPreferredHoursRepository
    extends
      JpaRepository<EmployeesPreferredHours, Long>{
  List<EmployeesPreferredHours> findByEmployeeIdOrderByEmployeeIdAscDayOfWeekAscStartTimeAsc(
      Long employeeId);

  @Query(value = "SELECT * FROM employees_preferred_hours "
      + "ORDER BY employee_id ASC, "
      + "FIELD(day_of_week, 'Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'), "
      + "start_time ASC", nativeQuery = true)
  List<EmployeesPreferredHours> findAllOrdered();

}