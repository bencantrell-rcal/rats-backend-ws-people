package com.rcal.people.sop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.PrivateKey;

@Component
public class GithubInstallationTest{

  @Value("${github.app.appId}")
  private String appId;

  @Value("${github.app.privateKey}")
  private String pem;

  private final RestTemplate restTemplate = new RestTemplate();

  /**
   * Fetches all installations for this GitHub App
   */
  public ResponseEntity<String> listInstallations() throws Exception{

    // 1️⃣ Load private key
    PrivateKey privateKey = GithubKeyLoader.loadPrivateKey(pem);

    // 2️⃣ Generate GitHub App JWT
    String jwt = GithubJwtService.generateJwt(appId,privateKey);

    // 3️⃣ Build headers
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(jwt);
    headers.setAccept(MediaType.parseMediaTypes("application/vnd.github+json"));

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    // 4️⃣ Call GitHub API
    return restTemplate.exchange("https://api.github.com/app/installations",
        HttpMethod.GET,entity,String.class);
  }
}
