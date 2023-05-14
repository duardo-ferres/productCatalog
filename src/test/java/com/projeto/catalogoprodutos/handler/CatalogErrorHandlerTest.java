package com.projeto.catalogoprodutos.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogErrorHandlerTest {
    @Mock
    RuntimeException runtimeException;
    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    Logger logger;
    @Mock
    LogManager logManager;

    @BeforeEach
    void initialize()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void catalogErrorHandler() throws JsonProcessingException {
        MockedStatic<LogManager> logManagerMockedStatic = mockStatic(LogManager.class);
        logManagerMockedStatic.when(() -> LogManager.getLogManager()).thenReturn(logManager);
        when(logManager.getLogger(anyString())).thenReturn(logger);
        doNothing().when(logger).log(any(Level.class), anyString());

        StackTraceElement[] stackTraceElements = new StackTraceElement[1];
        stackTraceElements[0] = new StackTraceElement("class", "method", "file", 120);

        when(runtimeException.getStackTrace()).thenReturn(stackTraceElements);
        when(runtimeException.getMessage()).thenReturn("Error");

        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://endpoint.end.point"));

        CatalogErrorHandler catalogErrorHandler = new CatalogErrorHandler();
        ResponseEntity<String> response = catalogErrorHandler.catalogErrorHandler(httpServletRequest, runtimeException);

        assertEquals(response.getStatusCode(), HttpStatus.valueOf(500));
        logManagerMockedStatic.close();
    }
}
