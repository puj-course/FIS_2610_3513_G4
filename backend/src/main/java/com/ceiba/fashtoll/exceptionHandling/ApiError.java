package com.ceiba.fashtoll.exceptionHandling;

import java.time.LocalDateTime;

public record ApiError(
        int statusCode,
        String message,
        LocalDateTime timestamp
) {}