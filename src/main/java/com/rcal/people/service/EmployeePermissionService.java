package com.rcal.people.service;

import com.rcal.people.entity.EmployeePermission;
import com.rcal.people.model.EmployeePermissionsDTO;
import com.rcal.people.repository.read.ReadEmployeePermissionRepository;
import com.rcal.people.repository.read.ReadJointEmployeeAndPermissionsRepository;
import com.rcal.people.repository.write.WriteEmployeePermissionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeePermissionService{

  private final ReadEmployeePermissionRepository readEmployeePermissionRepository;
  private final WriteEmployeePermissionRepository writeEmployeePermissionRepository;

  private final ReadJointEmployeeAndPermissionsRepository readJointEmployeeAndPermissionsRepository;

  public EmployeePermissionService(
      ReadEmployeePermissionRepository readEmployeePermissionRepository,
      WriteEmployeePermissionRepository writeEmployeePermissionRepository,
      ReadJointEmployeeAndPermissionsRepository readJointEmployeeAndPermissionsRepository) {
    this.readEmployeePermissionRepository = readEmployeePermissionRepository;
    this.writeEmployeePermissionRepository = writeEmployeePermissionRepository;
    this.readJointEmployeeAndPermissionsRepository = readJointEmployeeAndPermissionsRepository;
  }

  public EmployeePermission addPermission(Integer employeeId,String permission,
      String permissionClass){
    EmployeePermission ep = new EmployeePermission();
    ep.setEmployeeId(employeeId);
    ep.setPermission(permission);
    ep.setPermissionClass(permissionClass); // nullable
    return writeEmployeePermissionRepository.save(ep);
  }

  public boolean deletePermission(Integer employeeId,String permission){
    Optional<EmployeePermission> epOpt = writeEmployeePermissionRepository
        .findAll().stream().filter(ep -> ep.getEmployeeId().equals(employeeId)
            && ep.getPermission().equals(permission))
        .findFirst();

    if (epOpt.isPresent()){
      writeEmployeePermissionRepository.delete(epOpt.get());
      return true;
    }

    return false;
  }

  public List<String> getAllUniquePermissions(){
    return readEmployeePermissionRepository.findAll().stream()
        .map(EmployeePermission::getPermission).distinct()
        .collect(Collectors.toList());
  }

  public List<EmployeePermissionsDTO> getAllActiveEmployeesWithPermissions(){
    List<Object[]> results = readJointEmployeeAndPermissionsRepository
        .findAllActiveEmployeesWithPermissionsNative();
    List<EmployeePermissionsDTO> dtoList = new ArrayList<>();

    for (Object[] row : results){
      Integer eid = ((Number) row[0]).intValue();
      String firstName = (String) row[1];
      String lastName = (String) row[2];
      String permissionsConcat = (String) row[3]; // comma-separated permissions
      List<String> permissions = permissionsConcat != null
          && !permissionsConcat.isEmpty()
              ? Arrays.asList(permissionsConcat.split(","))
              : new ArrayList<>();

      dtoList.add(
          new EmployeePermissionsDTO(eid, firstName, lastName, permissions));
    }

    return dtoList;
  }
}
