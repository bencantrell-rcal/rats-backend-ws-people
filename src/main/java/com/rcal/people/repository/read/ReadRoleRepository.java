package com.rcal.people.repository.read;

import com.rcal.people.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadRoleRepository extends JpaRepository<Role, Integer>{

}
