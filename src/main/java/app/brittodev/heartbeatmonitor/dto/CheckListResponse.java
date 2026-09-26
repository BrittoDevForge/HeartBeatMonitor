package app.brittodev.heartbeatmonitor.dto;

import java.time.Instant;

public record CheckListResponse(
        Long id,
        String name,
        Long intervalSeconds,
        String status,
        Instant lastPingAt,
        Instant nextExpectedAt
) {
}
