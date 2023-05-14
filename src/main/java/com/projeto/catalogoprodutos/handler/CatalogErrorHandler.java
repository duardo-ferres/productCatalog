package com.projeto.catalogoprodutos.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@ControllerAdvice
public class CatalogErrorHandler {
    private static Logger logger;
    @ExceptionHandler
    public ResponseEntity<String> catalogErrorHandler(HttpServletRequest req, Exception e) throws JsonProcessingException {
        logger = LogManager.getLogManager().getLogger(CatalogErrorHandler.class.getName());

        Map<String, Object> retMessage = new HashMap<>();
        retMessage.put("response_code", 500);
        retMessage.put("description", "Internal Error Found");

        String url = req.getRequestURL().toString();
        int lineNumber = e.getStackTrace()[0].getLineNumber();

        logger.log(Level.SEVERE, "Internal server error");
        logger.log(Level.WARNING, "Requested endpoint");
        logger.log(Level.WARNING, url);
        logger.log(Level.WARNING, "Error message");
        logger.log(Level.WARNING, e.getMessage());
        logger.log(Level.WARNING, "Error Class");
        logger.log(Level.WARNING, e.getStackTrace()[0].getClass().getPackage().getName());
        logger.log(Level.WARNING, e.getStackTrace()[0].getClass().getName());
        logger.log(Level.WARNING, "Error Line");
        logger.log(Level.WARNING, String.valueOf(lineNumber));

        return new ResponseEntity<>(new ObjectMapper().writeValueAsString(retMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
