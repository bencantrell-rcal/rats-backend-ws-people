package com.rcal.people.service;

import com.rcal.people.entity.Role;
import com.rcal.people.repository.read.ReadRoleRepository;
import com.rcal.people.repository.write.WriteRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService{

  private final ReadRoleRepository readRoleRepository;
  private final WriteRoleRepository writeRoleRepository;

  public RoleService(ReadRoleRepository readRoleRepository,
      WriteRoleRepository writeRoleRepository) {
    this.readRoleRepository = readRoleRepository;
    this.writeRoleRepository = writeRoleRepository;
  }

  // ---------------------------------------------------------------------------
  // Get all roles
  // ---------------------------------------------------------------------------
  public List<Role> getAllRoles(){
    return readRoleRepository.findAll();
  }

  // ---------------------------------------------------------------------------
  // Get a specific role by ID (Optional)
  // ---------------------------------------------------------------------------
  public Optional<Role> getRoleByIdOptional(Integer roleId){
    return readRoleRepository.findById(roleId);
  }

  // ---------------------------------------------------------------------------
  // Create a new role
  // ---------------------------------------------------------------------------
  public Role createRole(String roleName,String roleDescription){

    if (roleName == null){
      throw new IllegalArgumentException("Role name must not be null");
    }

    // Keep only alphabetic characters and spaces
    String sanitizedName = roleName.replaceAll("[^A-Za-z ]","").trim()
        .replaceAll("\\s+"," ");

    if (sanitizedName.isEmpty()){
      throw new IllegalArgumentException(
          "Role name must contain alphabetic characters");
    }

    Role role = new Role();
    role.setRoleName(sanitizedName);
    role.setRoleDescription(roleDescription);

    return writeRoleRepository.save(role);
  }

  // ---------------------------------------------------------------------------
  // Delete a role by ID
  // ---------------------------------------------------------------------------
  public void deleteRole(Integer roleId){
    writeRoleRepository.deleteById(roleId);
  }

  // ---------------------------------------------------------------------------
  // Update role description
  // ---------------------------------------------------------------------------
  public void updateRoleDescription(Integer roleId,String description){

    if (roleId == null){
      throw new IllegalArgumentException("Role ID must not be null");
    }

    int updatedRows = writeRoleRepository.updateRoleDescription(roleId,
        description);

    if (updatedRows == 0){
      throw new RuntimeException("Role not found with id: " + roleId);
    }
  }
}
