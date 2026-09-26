package app.brittodev.heartbeatmonitor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCheckRequest(
        @NotBlank
        String name,
        @NotNull
        @Positive
        Long intervalSeconds
) {
}
