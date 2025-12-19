package com.rcal.people.repository.read;

import com.rcal.people.entity.EmployeeLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReadEmployeeLoginRepository
    extends
      JpaRepository<EmployeeLogin, Integer>{

  Optional<EmployeeLogin> findByLoginAndPassword(String login,String password);

  Optional<EmployeeLogin> findByLogin(String login);
}
