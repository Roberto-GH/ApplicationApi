package co.com.pragma.api.jwt;

import co.com.pragma.api.exception.AuthenticationApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationManagerTest {

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private JwtAuthenticationManager jwtAuthenticationManager;

    private UsernamePasswordAuthenticationToken authenticationToken;

    @BeforeEach
    void setUp() {
        authenticationToken = new UsernamePasswordAuthenticationToken("test_token", null);
    }

    // @Test
    // void authenticate_shouldReturnAuthenticationForValidToken() {
    //     Claims claims = mock(Claims.class);
    //     when(claims.getSubject()).thenReturn("testUser");
    //     List<Map<String, Object>> roles = new ArrayList<>();
    //     Map<String, Object> roleMap = new HashMap<>();
    //     roleMap.put("authority", "ROLE_USER");
    //     roles.add(roleMap);
    //     when(claims.get("roles")).thenReturn(roles); // Changed to concrete List and Map

    //     when(jwtProvider.getClaims(anyString())).thenReturn(claims);

    //     Mono<Authentication> result = jwtAuthenticationManager.authenticate(authenticationToken);

    //     StepVerifier.create(result)
    //             .expectNextMatches(auth -> {
    //                 assertNotNull(auth);
    //                 assertEquals("testUser", auth.getPrincipal());
    //                 assertEquals(1, auth.getAuthorities().size());
    //                 assertEquals(new SimpleGrantedAuthority("ROLE_USER"), auth.getAuthorities().iterator().next());
    //                 return true;
    //             })
    //             .verifyComplete();
    // }

    // @Test
    // void authenticate_shouldThrowAuthenticationApiExceptionForInvalidToken() {
    //     when(jwtProvider.getClaims(anyString())).thenThrow(new ExpiredJwtException(null, null, "Expired"));

    //     Mono<Authentication> result = jwtAuthenticationManager.authenticate(authenticationToken);

    //     StepVerifier.create(result)
    //             .expectErrorMatches(throwable -> throwable instanceof AuthenticationApiException &&
    //                     throwable.getMessage().equals("Bad token"))
    //             .verify();
    // }

    // @Test
    // void authenticate_shouldHandleTokenWithoutRoles() {
    //     Claims claims = mock(Claims.class);
    //     when(claims.getSubject()).thenReturn("testUser");
    //     when(claims.get("roles")).thenReturn(null); // No roles claim

    //     when(jwtProvider.getClaims(anyString())).thenReturn(claims);

    //     Mono<Authentication> result = jwtAuthenticationManager.authenticate(authenticationToken);

    //     StepVerifier.create(result)
    //             .expectNextMatches(auth -> {
    //                 assertNotNull(auth);
    //                 assertEquals("testUser", auth.getPrincipal());
    //                 assertEquals(0, auth.getAuthorities().size());
    //                 return true;
    //             })
    //             .verifyComplete();
    // }

    // @Test
    // void authenticate_shouldHandleMalformedRoles() {
    //     Claims claims = mock(Claims.class);
    //     when(claims.getSubject()).thenReturn("testUser");
    //     when(claims.get("roles")).thenReturn(List.of("ROLE_USER")); // Malformed roles: not a list of maps

    //     when(jwtProvider.getClaims(anyString())).thenReturn(claims);

    //     Mono<Authentication> result = jwtAuthenticationManager.authenticate(authenticationToken);

    //     StepVerifier.create(result)
    //             .expectNextMatches(auth -> {
    //                 assertNotNull(auth);
    //                 assertEquals("testUser", auth.getPrincipal());
    //                 assertEquals(0, auth.getAuthorities().size()); // Malformed roles should result in no authorities
    //                 return true;
    //             })
    //             .verifyComplete();
    // }
}
