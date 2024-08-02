/*
 * Copyright (c) 2021. Cornerstone OnDemand.
 * All Rights Reserved.
 * Company Confidential. 
 */

package com.supplyhouse.assignment.config;

import com.supplyhouse.assignment.model.DTO.ErrorDTO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;

/**
 * This class handles exceptions thrown from rest controllers.
 */
@RestControllerAdvice
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    /**
     * This exception advice handles IllegalStateException and IllegalArgumentException types of exceptions
     * */
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    @ResponseBody
    public ResponseEntity<ErrorDTO> processBadRequest(Exception exception) {
        logger.error("Exception occurred ", exception);
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, exception.getMessage(), ExceptionUtils.getStackTrace(exception)),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * This exception advice handles authentication related exceptions.
     * */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<ErrorDTO> processAuthenticationError(Exception exception){
        logger.error("Exception occurred ", exception);
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.UNAUTHORIZED, exception.getMessage(), ExceptionUtils.getStackTrace(exception)),
                HttpStatus.UNAUTHORIZED);
    }

    /**
     * This exception advice handles all other generic exceptions.
     * */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorDTO> processAllError(Exception exception) {
        logger.error("Exception occurred ", exception);
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), ExceptionUtils.getStackTrace(exception)),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }





}
