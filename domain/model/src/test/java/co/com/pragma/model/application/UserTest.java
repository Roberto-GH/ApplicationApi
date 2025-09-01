package co.com.pragma.model.application;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {

    @Test
    void builderShouldCreateUserInstance() {
        UUID userId = UUID.randomUUID();
        LocalDate birthdate = LocalDate.of(1990, 1, 1);

        User user = User.builder()
                .userId(userId)
                .firstName("John")
                .middleName("Doe")
                .lastName("Smith")
                .secondLastName("Jr.")
                .email("john.doe@example.com")
                .password("password123")
                .identityDocument(123456789L)
                .birthdate(birthdate)
                .address("123 Main St")
                .numberPhone(1234567890L)
                .baseSalary(50000L)
                .rolId(1L)
                .build();

        assertNotNull(user);
        assertEquals(userId, user.getUserId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getMiddleName());
        assertEquals("Smith", user.getLastName());
        assertEquals("Jr.", user.getSecondLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(123456789L, user.getIdentityDocument());
        assertEquals(birthdate, user.getBirthdate());
        assertEquals("123 Main St", user.getAddress());
        assertEquals(1234567890L, user.getNumberPhone());
        assertEquals(50000L, user.getBaseSalary());
        assertEquals(1L, user.getRolId());
    }

    @Test
    void builderShouldHandleNullValues() {
        User user = User.builder().build();

        assertNotNull(user);
        assertEquals(null, user.getUserId());
        assertEquals(null, user.getFirstName());
        assertEquals(null, user.getMiddleName());
        assertEquals(null, user.getLastName());
        assertEquals(null, user.getSecondLastName());
        assertEquals(null, user.getEmail());
        assertEquals(null, user.getPassword());
        assertEquals(null, user.getIdentityDocument());
        assertEquals(null, user.getBirthdate());
        assertEquals(null, user.getAddress());
        assertEquals(null, user.getNumberPhone());
        assertEquals(null, user.getBaseSalary());
        assertEquals(null, user.getRolId());
    }
}
