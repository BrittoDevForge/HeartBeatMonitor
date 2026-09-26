package app.brittodev.heartbeatmonitor.dto;

import java.time.Instant;

public record CheckResponse(
        Long id,
        String name,
        Long intervalSeconds,
        String status,
        Instant lastPingAt,
        Instant nextExpectedAt,
        String pingUrl
) {
}
