package ru.pas_zhukov.eventmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@ControllerAdvice
public class ControllerGlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ControllerGlobalExceptionHandler.class);

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ServerErrorDto> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage());

        String detailedMessage = e.getMessage();
        ServerErrorDto serverErrorDto = new ServerErrorDto(
                "Validation error",
                detailedMessage,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serverErrorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerErrorDto> handleValidationException(
            MethodArgumentNotValidException e
    ) {
        log.error("Got validation exception", e);

        String detailedMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ServerErrorDto errorDto = new ServerErrorDto(
                "Validation error",
                detailedMessage,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ServerErrorDto> handleGenerisException(
            Exception e
    ) {
        log.error("Server error", e);
        ServerErrorDto errorDto = new ServerErrorDto(
                "Server error",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDto> handleNotFoundException(
            EntityNotFoundException e
    ) {
        log.error("Got exception", e);
        ServerErrorDto errorDto = new ServerErrorDto(
                "Entity not found",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ServerErrorDto> handleAccessDeniedException(IllegalStateException e) {
        log.error("Got exception", e);
        ServerErrorDto errorDto = new ServerErrorDto(
                "Bad request",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ServerErrorDto> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Got exception", e);
        ServerErrorDto errorDto = new ServerErrorDto(
                "Forbidden",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }
}
