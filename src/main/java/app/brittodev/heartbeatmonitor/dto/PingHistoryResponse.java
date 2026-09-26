package app.brittodev.heartbeatmonitor.dto;

import java.time.Instant;

public record PingHistoryResponse(
        Long id,
        Long checkId,
        Instant receivedAt
) {
}
