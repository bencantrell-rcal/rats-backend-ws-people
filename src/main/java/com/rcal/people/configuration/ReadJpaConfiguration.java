package com.rcal.people.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.rcal.people.repository.read", entityManagerFactoryRef = "readEntityManagerFactory", transactionManagerRef = "readTransactionManager")
public class ReadJpaConfiguration{

  private final DataSource readDataSource;

  // References the read datasource bean created in DataSourceConfiguration
  public ReadJpaConfiguration(
      @Qualifier("readDataSource") DataSource readDataSource) {
    this.readDataSource = readDataSource;
  }

  // Each custom JPA repository needs a entityManagerFactory
  @Bean(name = "readEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean readEntityManagerFactory(){
    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(readDataSource);
    emf.setPackagesToScan("com.rcal.people.entity");
    emf.setPersistenceUnitName("read");
    emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    return emf;
  }

  // Each custom JPA repository needs a platformTransactionManager
  @Bean(name = "readTransactionManager")
  public PlatformTransactionManager readTransactionManager(
      @Qualifier("readEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf){
    return new JpaTransactionManager(emf.getObject());
  }
}
