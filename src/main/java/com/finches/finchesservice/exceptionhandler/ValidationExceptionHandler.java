package com.finches.finchesservice.exceptionhandler;

import com.finches.finchesservice.constents.messages.MappedError;
import com.finches.finchesservice.exceptions.apiexceptions.DuplicateException;
import com.finches.finchesservice.models.response.ErrorResponse;
import com.finches.finchesservice.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ValidationExceptionHandler {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<ErrorResponse> constraintViolationHandler(ConstraintViolationException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        exception.getConstraintViolations().forEach(constraintViolation -> {
            String[] path = constraintViolation.getPropertyPath().toString().split("\\.");
            errorResponse.getMappedErrors()
                    .add(new MappedError(path[path.length - 1], Utility.getMessageFromResourceBundle(httpServletRequest, constraintViolation.getMessage())));
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> methodArgumentNotValidHandler(MethodArgumentNotValidException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
                errorResponse.getMappedErrors()
                        .add(new MappedError(fieldError.getField(), Utility.getMessageFromResourceBundle(httpServletRequest, fieldError.getDefaultMessage()))));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> commonException(Exception exception) {
        exception.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.getMappedErrors().add(new MappedError("unknown", "Unexpected happened!"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
