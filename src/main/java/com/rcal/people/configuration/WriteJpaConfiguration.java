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
@EnableJpaRepositories(basePackages = "com.rcal.people.repository.write", entityManagerFactoryRef = "writeEntityManagerFactory", transactionManagerRef = "writeTransactionManager")
public class WriteJpaConfiguration{

  private final DataSource writeDataSource;

  // References the write datasource bean created in DataSourceConfiguration
  public WriteJpaConfiguration(
      @Qualifier("writeDataSource") DataSource writeDataSource) {
    this.writeDataSource = writeDataSource;
  }

  // Each custom JPA repository needs a entityManagerFactory
  @Bean(name = "writeEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean writeEntityManagerFactory(){
    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(writeDataSource);
    emf.setPackagesToScan("com.rcal.people.entity");
    emf.setPersistenceUnitName("write");
    emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    return emf;
  }

  // Each custom JPA repository needs a platformTransactionManager
  @Bean(name = "writeTransactionManager")
  public PlatformTransactionManager writeTransactionManager(
      @Qualifier("writeEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf){
    return new JpaTransactionManager(emf.getObject());
  }
}
