package co.com.pragma.consumer;

import co.com.pragma.consumer.TokenResponse;
import co.com.pragma.consumer.UserResponseDto;
import co.com.pragma.model.application.User;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestConsumerTest {

    private RestConsumer restConsumer;
    private static MockWebServer mockBackEnd;

    @Mock
    private DtoMapper dtoMapper;

    @BeforeAll
    static void globalSetUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void globalTearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void setUp() {
        var webClient = WebClient.builder().baseUrl(mockBackEnd.url("/").toString()).build();
        restConsumer = new RestConsumer(webClient, dtoMapper, "test@example.com", "password");
    }

    @Test
    @DisplayName("Validate getToken function")
    void validateGetToken() {
        String mockToken = "mock-jwt-token";
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"token\":\"" + mockToken + "\"}"));

        StepVerifier.create(restConsumer.getToken())
                .expectNextMatches(tokenResponse -> tokenResponse.getToken().equals(mockToken))
                .verifyComplete();
    }

    @Test
    @DisplayName("Validate findUserByEmail function")
    void validateFindUserByEmail() {
        String userEmail = "user@example.com";
        String mockToken = "mock-jwt-token";
        String mockUserId = UUID.randomUUID().toString();
        String mockFirstName = "John";

        // Mock response for getToken
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"token\":\"" + mockToken + "\"}"));

        // Mock response for findUserByEmail
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"userId\":\"" + mockUserId + "\", \"firstName\":\"" + mockFirstName + "\", \"email\":\"" + userEmail + "\"}"));

        // Mock DtoMapper behavior using builder for UserResponseDto
        User expectedUser = User.builder().userId(UUID.fromString(mockUserId)).firstName(mockFirstName).email(userEmail).build();
        when(dtoMapper.toUser(any(UserResponseDto.class))).thenReturn(expectedUser); // Changed to any()

        StepVerifier.create(restConsumer.findUserByEmail(userEmail))
                .expectNextMatches(user -> user.getEmail().equals(userEmail) && user.getFirstName().equals(mockFirstName))
                .verifyComplete();
    }
}