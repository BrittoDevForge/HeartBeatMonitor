package app.brittodev.heartbeatmonitor.dto;

public record CreateCheckRequest(
        String name,
        Long intervalSeconds
) {
}
