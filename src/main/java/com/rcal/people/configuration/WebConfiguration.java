package com.rcal.people.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer{

  static final String PROD_FRONTEND_WEB_ADDRESS = "http://34.149.233.159";
  static final String TEST_FRONTEND_WEB_ADDRESS = "http://34.128.169.253";
  static final String LOCAL_FRONTEND_WEB_ADDRESS = "http://localhost:5173";

  static final String TEST_SCHEDULER_DOMAIN = "http://scheduler.test.rcal.local";
  static final String TEST_LOGIN_DOMAIN = "http://login.test.rcal.local";
  static final String PROD_SCHEDULER_DOMAIN = "http://scheduler.rcal.local";
  static final String PROD_LOGIN_DOMAIN = "http://login.rcal.local";

  static final String NEW_TEST_SCHEDULER_DOMAIN = "http://scheduler.test.rcal.internal";
  static final String NEW_TEST_LOGIN_DOMAIN = "http://login.test.rcal.internal";
  static final String NEW_PROD_SCHEDULER_DOMAIN = "http://scheduler.rcal.internal";
  static final String NEW_PROD_LOGIN_DOMAIN = "http://login.rcal.internal";

  static final String TEST_INGRESS_CONTROLLER = "http://api.test.rcal.internal";
  static final String PROD_INGRESS_CONTROLLER = "http://api.rcal.internal";

  static final String HTTPS_TEST_SCHEDULER_DOMAIN = "https://scheduler.test.rcal.internal";
  static final String HTTPS_TEST_LOGIN_DOMAIN = "https://login.test.rcal.internal";
  static final String HTTPS_TEST_API_DOMAIN = "https://api.test.rcal.internal";
  static final String HTTPS_PROD_SCHEDULER_DOMAIN = "https://scheduler.rcal.internal";
  static final String HTTPS_PROD_LOGIN_DOMAIN = "https://login.rcal.internal";
  static final String HTTPS_PROD_API_DOMAIN = "https://api.rcal.internal";
  static final String HTTPS_TEST_PEOPLE_DOMAIN = "https://people.rcal.internal";
  static final String HTTPS_PROD_PEOPLE_DOMAIN = "https://people.test.rcal.internal";

  // ---------------------------------------------------------------------------
  // Purpose: Approve http request origins to prevent CORS errors in the
  // frontend
  // ---------------------------------------------------------------------------
  @Override
  public void addCorsMappings(CorsRegistry registry){
    registry.addMapping("/**").allowedOrigins(PROD_FRONTEND_WEB_ADDRESS,
        TEST_FRONTEND_WEB_ADDRESS,LOCAL_FRONTEND_WEB_ADDRESS,
        TEST_SCHEDULER_DOMAIN,TEST_LOGIN_DOMAIN,PROD_SCHEDULER_DOMAIN,
        PROD_LOGIN_DOMAIN,NEW_TEST_SCHEDULER_DOMAIN,NEW_TEST_LOGIN_DOMAIN,
        NEW_PROD_SCHEDULER_DOMAIN,NEW_PROD_LOGIN_DOMAIN,TEST_INGRESS_CONTROLLER,
        PROD_INGRESS_CONTROLLER,HTTPS_TEST_SCHEDULER_DOMAIN,
        HTTPS_TEST_LOGIN_DOMAIN,HTTPS_TEST_API_DOMAIN,
        HTTPS_PROD_SCHEDULER_DOMAIN,HTTPS_PROD_LOGIN_DOMAIN,
        HTTPS_PROD_API_DOMAIN,HTTPS_PROD_PEOPLE_DOMAIN,HTTPS_TEST_PEOPLE_DOMAIN)
        .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
        .allowedHeaders("*").allowCredentials(true);
  }
}
