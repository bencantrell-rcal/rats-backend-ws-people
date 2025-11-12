package com.rcal.people.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("default")
public class OpenApiConfiguration{
  // ---------------------------------------------------------------------------
  // Purpose: Prepends the path to the app's endpoints in the Swagger
  // configuration to match the ingress's prefix
  // ---------------------------------------------------------------------------
  @Bean
  public OpenAPI customOpenAPI(){
    return new OpenAPI().servers(List.of(new Server().url("/auth")));
  }
}
