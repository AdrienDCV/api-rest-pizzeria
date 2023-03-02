package dao;
// code pompé ici : https://developer.okta.com/blog/2018/10/31/jwts-with-java
// lui-même inspiré par : https://www.baeldung.com/java-json-web-tokens-jjwt
// et sinon la doc : https://github.com/jwtk/jjwt/blob/master/README.md

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtManager {
    // pour SHA256 : 256 bits mini
    //private static final String SECRET_KEY = "bachibouzoukbachibouzoukbachibouzoukbachibouzouk";
	private String secretKey;
	
	public JwtManager(String secretKey) {
		this.secretKey=secretKey;
	}
	
    public  String createJWT() {
        // The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        // Let's set the JWT Claims
        JwtBuilder token = Jwts.builder()
                .setId(UUID.randomUUID().toString().replace("-", ""))
                .setIssuedAt(now)
                .setSubject("Authentification pour tp333")
                .setIssuer("philippe.mathieu@univ-lille.fr")
                .signWith(signingKey, signatureAlgorithm);

        // if it has been specified, let's add the expiration
        long ttlMillis = 1000 * 60 * 4; // 4mn
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            token.setExpiration(exp); // 20mn par defaut
        }
        // Builds the JWT and serializes it to a compact, URL-safe string
        return token.compact();
    }

    public Claims decodeJWT(String jwt) throws Exception {
        // This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(secretKey))
                .build()
                // verifie la signature et l'iat
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}
