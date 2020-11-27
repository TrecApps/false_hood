package com.trecapps.false_hood.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ErrorControllerAdvce {
	@ExceptionHandler
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<String> requestHandlingNoHandlerFound(final NoHandlerFoundException ex) {
        System.out.println("Error 404: " + ex.getRequestURL());
        return new ResponseEntity<String>("Could not find pathway " + ex.getRequestURL() + " !", HttpStatus.NOT_FOUND);
    }
}
