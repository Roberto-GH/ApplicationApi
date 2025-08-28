package co.com.pragma.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityHeadersConfigTest {

    @InjectMocks
    private SecurityHeadersConfig securityHeadersConfig;

    @Mock
    private ServerWebExchange exchange;
    @Mock
    private WebFilterChain chain;
    @Mock
    private ServerWebExchange.Builder exchangeBuilder;
    @Mock
    private org.springframework.http.server.reactive.ServerHttpResponse response;
    @Mock
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        when(exchange.getResponse()).thenReturn(response);
        when(response.getHeaders()).thenReturn(headers);
        when(chain.filter(exchange)).thenReturn(Mono.empty()); // Mock the chain to complete
    }

    @Test
    void filter_shouldAddSecurityHeaders() {
        securityHeadersConfig.filter(exchange, chain).block(); // Block to ensure Mono completes

        verify(headers).set("Content-Security-Policy", "default-src 'self'; frame-ancestors 'self'; form-action 'self'");
        verify(headers).set("Strict-Transport-Security", "max-age=31536000;");
        verify(headers).set("X-Content-Type-Options", "nosniff");
        verify(headers).set("Server", "");
        verify(headers).set("Cache-Control", "no-store");
        verify(headers).set("Pragma", "no-cache");
        verify(headers).set("Referrer-Policy", "strict-origin-when-cross-origin");
        verify(chain).filter(exchange);
    }
}
