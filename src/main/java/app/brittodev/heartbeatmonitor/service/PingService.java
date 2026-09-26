package app.brittodev.heartbeatmonitor.service;


import app.brittodev.heartbeatmonitor.dto.CheckResponse;
import app.brittodev.heartbeatmonitor.entity.Check;
import app.brittodev.heartbeatmonitor.entity.CheckStatus;
import app.brittodev.heartbeatmonitor.entity.PingHistory;
import app.brittodev.heartbeatmonitor.repository.CheckRepository;
import app.brittodev.heartbeatmonitor.repository.PingRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
public class PingService {

    private final PingRepository pingRepository;
    private final CheckRepository checkRepository;
    private final Clock clock;

    public PingService(PingRepository pingRepository, CheckRepository checkRepository, Clock clock) {
        this.pingRepository = pingRepository;
        this.checkRepository = checkRepository;
        this.clock = clock;
    }

    public void ping(String pingToken) {

        Check check = checkRepository
                .findByPingToken(pingToken)
                .orElseThrow( () -> new RuntimeException("Invalid Token"));
        Instant receivedAt = Instant.now(clock);

        PingHistory ping = new PingHistory();

        ping.setCheckId(check.getId());
        ping.setReceivedAt(receivedAt);

        pingRepository.save(ping);

        check.setLastPingAt(receivedAt); // changing last ping after the ping
        check.setNextExpectedAt(
                receivedAt.plusSeconds(check.getIntervalSeconds())
        ); // calculate and set next expected at

        if (check.getStatus() == CheckStatus.DOWN) {
            check.setStatus(CheckStatus.UP);
        }

        check.setUpdatedAt(receivedAt);

        checkRepository.save(check);
    }

}
