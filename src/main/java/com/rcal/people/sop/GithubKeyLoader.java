package com.rcal.people.sop;

import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.ASN1Sequence;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Base64;

public class GithubKeyLoader{

  public static PrivateKey loadPrivateKey(String pem) throws Exception{
    // Remove PEM headers/footers and whitespace
    String cleaned = pem.replace("-----BEGIN RSA PRIVATE KEY-----","")
        .replace("-----END RSA PRIVATE KEY-----","").replaceAll("\\s","");

    byte[] decoded = Base64.getDecoder().decode(cleaned);

    // Parse PKCS#1 structure
    RSAPrivateKey rsaPrivateKey = RSAPrivateKey
        .getInstance(ASN1Sequence.getInstance(decoded));

    RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(
        rsaPrivateKey.getModulus(), rsaPrivateKey.getPublicExponent(),
        rsaPrivateKey.getPrivateExponent(), rsaPrivateKey.getPrime1(),
        rsaPrivateKey.getPrime2(), rsaPrivateKey.getExponent1(),
        rsaPrivateKey.getExponent2(), rsaPrivateKey.getCoefficient());

    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePrivate(keySpec);
  }
}
