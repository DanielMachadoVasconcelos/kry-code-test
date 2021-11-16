package com.kry.codetest.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

@ControllerAdvice
@AllArgsConstructor
public class ValidationHandler {

    public static final String UNHANDLED_EXCEPTIONS_METRIC_NAME = "exception.unhandled";

    ObjectMapper objectMapper;
    MeterRegistry meterRegistry;

    @ResponseBody
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<List<ErrorMessage.ErrorDetails>> handleException(WebExchangeBindException exception) {
        var errors = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> new ErrorMessage.ErrorDetails(((FieldError) error).getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    @ResponseBody
    @ExceptionHandler(OptimisticLockingFailureException.class)
    protected ResponseEntity<?> catchAllException(final OptimisticLockingFailureException exception) {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<?> catchAllException(final Throwable exception) {
        var nameTage = Tag.of("name", exception.getClass().getSimpleName().toLowerCase());
        var causeTag = Tag.of("cause", getRootCause(exception).getClass().getSimpleName().toLowerCase());
        meterRegistry.counter(UNHANDLED_EXCEPTIONS_METRIC_NAME, List.of(nameTage, causeTag)).increment();
        return ResponseEntity.status(SC_INTERNAL_SERVER_ERROR).build();
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorMessage {
        List<ErrorDetails> errors;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ErrorDetails {
            String field;
            String message;
        }
    }
}