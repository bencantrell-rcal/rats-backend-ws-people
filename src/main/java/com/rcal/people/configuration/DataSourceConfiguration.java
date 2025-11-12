package com.rcal.people.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration{

  @Value("${spring.datasource.read.url}")
  private String readUrl;

  @Value("${spring.datasource.read.username}")
  private String readUsername;

  @Value("${spring.datasource.read.password}")
  private String readPassword;

  @Value("${spring.datasource.write.url}")
  private String writeUrl;

  @Value("${spring.datasource.write.username}")
  private String writeUsername;

  @Value("${spring.datasource.write.password}")
  private String writePassword;

  // ---------------------------------------------------------------------------
  // Purpose: Creates a datasource bean for the read source. Created using
  // injected spring values from application.properties (which are sourced from
  // the configmap in a deployment)
  // ---------------------------------------------------------------------------
  @Bean(name = "readDataSource")
  public HikariDataSource readDataSource(){
    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl(readUrl);
    ds.setUsername(readUsername);
    ds.setPassword(readPassword);
    ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
    return ds;
  }

  // ---------------------------------------------------------------------------
  // Purpose: Creates a datasource bean for the write source. Created using
  // injected spring values from application.properties (which are sourced from
  // the configmap in a deployment)
  // ---------------------------------------------------------------------------
  @Bean(name = "writeDataSource")
  public HikariDataSource writeDataSource(){
    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl(writeUrl);
    ds.setUsername(writeUsername);
    ds.setPassword(writePassword);
    ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
    return ds;
  }
}
