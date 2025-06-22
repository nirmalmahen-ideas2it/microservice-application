package com.ideas2it.training.user.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {
    @Test
    void testHandleRuntimeException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        RuntimeException ex = new RuntimeException("Test error");
        ResponseEntity<String> response = handler.handleRuntimeException(ex);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Test error", response.getBody());
    }
}