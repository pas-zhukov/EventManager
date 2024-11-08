package ru.pas_zhukov.eventmanager.exception;

import java.time.LocalDateTime;

public record ServerErrorDto(String message,
                             String detailedMessage,
                             LocalDateTime dateTime) {
}
