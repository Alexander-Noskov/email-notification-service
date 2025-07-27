package com.example.emailnotification.exception;

import com.example.emailnotification.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(Exception e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleMethodNotAllowed(MethodNotAllowedException e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorResponse(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(RuntimeException e) {
        log.error("{} : {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorResponse("Упс! Что-то пошло не так. Мы уже работаем над устранением проблемы. Пожалуйста, попробуйте зайти позже", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
