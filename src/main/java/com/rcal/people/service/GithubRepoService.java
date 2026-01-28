package com.rcal.people.service;

import com.rcal.people.sop.GithubJwtService;
import com.rcal.people.sop.GithubKeyLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.PrivateKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GithubRepoService{

  @Value("${github.app.appId}")
  private String appId;

  @Value("${github.app.privateKey}")
  private String pem;

  private final RestTemplate restTemplate = new RestTemplate();

  /**
   * Fetches a file from a GitHub repo on a specific branch using a GitHub App.
   *
   * @param installationId
   *          Installation ID from /app/installations
   * @param repo
   *          Owner/repo name, e.g., "Rcal-Products/my-repo"
   * @param path
   *          File path, e.g., "skills.md"
   * @param branch
   *          Branch name, e.g., "dev-branch"
   * @return File content as a String
   */
  public String fetchMarkdownFile(long installationId,String repo,String path,
      String branch) throws Exception{
    // Generate JWT
    PrivateKey privateKey = GithubKeyLoader
        .loadPrivateKey(pem.replace("\\n","\n"));
    String jwt = GithubJwtService.generateJwt(appId,privateKey);

    // Exchange JWT for installation token
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(jwt);
    headers.setAccept(MediaType.parseMediaTypes("application/vnd.github+json"));
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<Map> tokenResponse = restTemplate
        .exchange("https://api.github.com/app/installations/" + installationId
            + "/access_tokens",HttpMethod.POST,entity,Map.class);

    String installationToken = (String) tokenResponse.getBody().get("token");

    // Fetch file from repo using installation token
    HttpHeaders fileHeaders = new HttpHeaders();
    fileHeaders.setBearerAuth(installationToken);
    fileHeaders
        .setAccept(MediaType.parseMediaTypes("application/vnd.github+json"));
    HttpEntity<Void> fileEntity = new HttpEntity<>(fileHeaders);

    ResponseEntity<Map> fileResponse = restTemplate
        .exchange("https://api.github.com/repos/" + repo + "/contents/" + path
            + "?ref=" + branch,HttpMethod.GET,fileEntity,Map.class);

    String encodedContent = (String) fileResponse.getBody().get("content");
    String cleanedContent = encodedContent.replaceAll("\\s",""); // remove
                                                                 // whitespace/newlines
                                                                 // in Base64
    byte[] decoded = Base64.getDecoder().decode(cleanedContent);

    return new String(decoded);
  }

  public List<String> listMarkdownFiles(long installationId,String repo,
      String directoryPath,String branch) throws Exception{

    // Generate JWT
    PrivateKey privateKey = GithubKeyLoader
        .loadPrivateKey(pem.replace("\\n","\n"));
    String jwt = GithubJwtService.generateJwt(appId,privateKey);

    // Exchange JWT for installation token
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(jwt);
    headers.setAccept(MediaType.parseMediaTypes("application/vnd.github+json"));

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<Map> tokenResponse = restTemplate
        .exchange("https://api.github.com/app/installations/" + installationId
            + "/access_tokens",HttpMethod.POST,entity,Map.class);

    String installationToken = (String) tokenResponse.getBody().get("token");

    // List directory contents
    HttpHeaders fileHeaders = new HttpHeaders();
    fileHeaders.setBearerAuth(installationToken);
    fileHeaders
        .setAccept(MediaType.parseMediaTypes("application/vnd.github+json"));

    HttpEntity<Void> fileEntity = new HttpEntity<>(fileHeaders);

    ResponseEntity<List> response = restTemplate
        .exchange(
            "https://api.github.com/repos/" + repo + "/contents/"
                + directoryPath + "?ref=" + branch,
            HttpMethod.GET,fileEntity,List.class);

    List<String> skills = new ArrayList<>();

    for (Object item : response.getBody()){
      Map<String, Object> file = (Map<String, Object>) item;

      if ("file".equals(file.get("type"))){
        String name = (String) file.get("name");

        if (name.endsWith(".md")){
          // "skill-one.md" -> "Skill One"
          String skillName = name.replace(".md","").replace("-"," ").trim();

          skillName = Arrays.stream(skillName.split(" "))
              .map(s -> s.substring(0,1).toUpperCase() + s.substring(1))
              .collect(Collectors.joining(" "));

          skills.add(skillName);
        }
      }
    }

    return skills;
  }
}
