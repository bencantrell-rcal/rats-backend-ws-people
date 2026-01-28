package com.rcal.people.sop;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;

public class GithubJwtService{

  public static String generateJwt(String appId,PrivateKey privateKey){
    Instant now = Instant.now();

    return Jwts.builder().setIssuer(appId)
        .setIssuedAt(Date.from(now.minusSeconds(60)))
        .setExpiration(Date.from(now.plusSeconds(9 * 60)))
        .signWith(privateKey,SignatureAlgorithm.RS256).compact();
  }
}
