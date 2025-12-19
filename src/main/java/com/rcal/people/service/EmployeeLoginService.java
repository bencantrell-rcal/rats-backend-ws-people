package com.rcal.people.service;

import com.rcal.people.entity.EmployeeLogin;
import com.rcal.people.entity.UserSession;
import com.rcal.people.repository.read.ReadEmployeeLoginRepository;
import com.rcal.people.repository.read.ReadEmployeePermissionsRepository;
import com.rcal.people.repository.read.ReadUserSessionRepository;
import com.rcal.people.repository.write.WriteUserSessionRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeLoginService{

  private final ReadEmployeeLoginRepository readEmployeeLoginRepository;
  private final ReadEmployeePermissionsRepository readEmployeePermissionsRepository;
  private final ReadUserSessionRepository readUserSessionRepository;
  private final WriteUserSessionRepository writeUserSessionRepository;

  public EmployeeLoginService(
      ReadEmployeeLoginRepository readEmployeeLoginRepository,
      ReadEmployeePermissionsRepository readEmployeePermissionsRepository,
      ReadUserSessionRepository readUserSessionRepository,
      WriteUserSessionRepository writeUserSessionRepository) {
    this.readEmployeeLoginRepository = readEmployeeLoginRepository;
    this.readEmployeePermissionsRepository = readEmployeePermissionsRepository;
    this.readUserSessionRepository = readUserSessionRepository;
    this.writeUserSessionRepository = writeUserSessionRepository;
  }

  public Optional<EmployeeLogin> authenticate(String login,String password){
    return readEmployeeLoginRepository.findByLoginAndPassword(login,password);
  }

  public Optional<EmployeeLogin> findByEid(Integer eid){
    return readEmployeeLoginRepository.findById(eid);
  }

  public List<String> getPermissionsByEid(Integer eid){
    return readEmployeePermissionsRepository.findByEid(eid).stream()
        .map(p -> p.getPermission()).collect(Collectors.toList());
  }

  public Optional<UserSession> getSession(String sessionId){
    return readUserSessionRepository.findBySessionId(sessionId)
        .filter(session -> !session.isExpired());
  }

  public void deleteExpiredSessions(){
    Timestamp now = new Timestamp(System.currentTimeMillis());
    writeUserSessionRepository.deleteByExpiresAtBefore(now);
  }
}
