package com.finance.tracker.exception;

public class GlobalExceptionHandler {

    public static String handle(Exception e) {

        if (e instanceof BadRequestException) {
            return e.getMessage();
        }

        if (e instanceof ResourceNotFoundException) {
            return e.getMessage();
        }

        if (e instanceof AuthenticationException) {
            return e.getMessage();
        }

        if (e instanceof DatabaseException) {
            return "System error. Please try again later.";
        }

        return "Unexpected error occurred.";
    }
}
