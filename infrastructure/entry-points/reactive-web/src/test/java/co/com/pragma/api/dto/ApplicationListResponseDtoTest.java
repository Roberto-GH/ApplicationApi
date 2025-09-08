package co.com.pragma.api.dto;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationListResponseDtoTest {

    @Test
    void builderShouldCreateInstance() {
        ApplicationListResponseDto dto = ApplicationListResponseDto.builder()
                .pageNumber(1)
                .pageSize(10)
                .totalRecords(100)
                .totalPages(10)
                .data(Collections.emptyList())
                .build();

        assertNotNull(dto);
        assertEquals(1, dto.pageNumber());
        assertEquals(10, dto.pageSize());
        assertEquals(100, dto.totalRecords());
        assertEquals(10, dto.totalPages());
        assertEquals(Collections.emptyList(), dto.data());
    }

    @Test
    void equalsAndHashCodeShouldWorkCorrectly() {
        // Create ApplicationDataDto instances using their canonical constructor
        ApplicationDataDto dataDto1 = new ApplicationDataDto(null, null, null, null, null, null, null, null, null, null);
        ApplicationDataDto dataDto2 = new ApplicationDataDto(null, null, null, null, null, null, null, null, null, null);

        List<ApplicationDataDto> data1 = Collections.singletonList(dataDto1);
        List<ApplicationDataDto> data2 = Collections.singletonList(dataDto2);

        ApplicationListResponseDto dto1 = ApplicationListResponseDto.builder()
                .pageNumber(1)
                .pageSize(10)
                .totalRecords(100)
                .totalPages(10)
                .data(data1)
                .build();

        ApplicationListResponseDto dto2 = ApplicationListResponseDto.builder()
                .pageNumber(1)
                .pageSize(10)
                .totalRecords(100)
                .totalPages(10)
                .data(data1)
                .build();

        ApplicationListResponseDto dto3 = ApplicationListResponseDto.builder()
                .pageNumber(2)
                .pageSize(10)
                .totalRecords(100)
                .totalPages(10)
                .data(data2)
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void toStringShouldContainAllFields() {
        ApplicationListResponseDto dto = ApplicationListResponseDto.builder()
                .pageNumber(1)
                .pageSize(10)
                .totalRecords(100)
                .totalPages(10)
                .data(Collections.emptyList())
                .build();

        String toString = dto.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("pageNumber=1"));
        assertTrue(toString.contains("pageSize=10"));
        assertTrue(toString.contains("totalRecords=100"));
        assertTrue(toString.contains("totalPages=10"));
        assertTrue(toString.contains("data=[]"));
    }
}