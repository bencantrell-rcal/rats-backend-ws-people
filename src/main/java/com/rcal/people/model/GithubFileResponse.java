package com.rcal.people.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubFileResponse{
  private String name;
  private String path;
  private String content; // base64-encoded
  private String encoding;
}