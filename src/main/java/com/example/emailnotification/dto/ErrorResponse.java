package com.example.emailnotification.dto;

public record ErrorResponse(
        String error,
        Integer code) {
}
