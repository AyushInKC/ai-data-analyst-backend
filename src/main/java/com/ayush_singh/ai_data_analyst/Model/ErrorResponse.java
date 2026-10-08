package com.ayush_singh.ai_data_analyst.Model;

import java.time.LocalDateTime;

public record ErrorResponse(String error,
                            String message,
                            LocalDateTime timestamp) {
}
