package com.rcal.people.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GithubContentService{

  private final WebClient webClient;

  public GithubContentService() {
    this.webClient = WebClient.builder().build();
  }

  public String fetchPublicMarkdown(String fileName){
    // Ensure the caller provides the full filename like "Engineering.md"
    String rawUrl = "https://raw.githubusercontent.com/Rcal-Products/SOP-Documentation/main/docs/"
        + fileName;

    return webClient.get().uri(rawUrl).retrieve().bodyToMono(String.class)
        .block();
  }
}
