package co.com.pragma.api.jwt;

import co.com.bancolombia.secretsmanager.api.GenericManagerAsync;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import co.com.pragma.api.constants.ApplicationWebKeys;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;


@Component
public class JwtProvider {

  private static final Logger LOG = LoggerFactory.getLogger(JwtProvider.class);
  private static final Gson gson = new Gson();

  private final GenericManagerAsync genericManager;
  private final String jwtSecretName;

  public JwtProvider(GenericManagerAsync genericManager, @Value("${aws.jwtSecretName}") String jwtSecretName) {
  this.genericManager = genericManager;
   this.jwtSecretName = jwtSecretName;
  }

  private Mono<JwtSecretModel> getJwtSecretModelReactive() {
    try {
      return genericManager
        .getSecret(jwtSecretName, JwtSecretModel.class)
        .doOnNext(jwtSecret -> LOG.info("{} secret jwt obtenido => {}", jwtSecretName, gson.toJson(jwtSecret)))
        .onErrorResume(SecretException.class, e -> Mono.error(new RuntimeException("Error retrieving JWT secret", e)));
    } catch (SecretException e) {
      LOG.error("Synchronous error retrieving secret: {}", e.getMessage());
      return Mono.error(new RuntimeException("Synchronous error retrieving JWT secret", e));
    }
  }

  public Mono<Claims> getClaims(String token) {
    return getJwtSecretModelReactive().flatMap(jwtSecretModel -> {
      String currentSecret = jwtSecretModel.getJwtSecretKey();
      return validateReactive(token, currentSecret).
        thenReturn(Jwts.parser().verifyWith(getKey(currentSecret)).build().parseSignedClaims(token).getPayload());
    });
  }

  public Mono<Void> validateReactive(String token, String secret) {
    return Mono.fromRunnable(() -> {
      try {
        Jwts.parser().verifyWith(getKey(secret)).build().parseSignedClaims(token).getPayload().getSubject();
      } catch (ExpiredJwtException e) {
        LOG.error(ApplicationWebKeys.TOKEN_EXPIRED);
        throw e;
      } catch (MalformedJwtException e) {
        LOG.error(ApplicationWebKeys.TOKEN_MALFORMED);
        throw e;
      } catch (SignatureException e) {
        LOG.error(ApplicationWebKeys.BAD_SIGNATURE);
        throw e;
      } catch (IllegalArgumentException e) {
        LOG.error(ApplicationWebKeys.ILLEGAL_ARGS);
        throw e;
      }
    }).then();
  }

  private SecretKey getKey(String secret) {
    byte[] secretBytes = Decoders.BASE64URL.decode(secret);
    return Keys.hmacShaKeyFor(secretBytes);
  }

}
