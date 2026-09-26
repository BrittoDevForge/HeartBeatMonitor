package app.brittodev.heartbeatmonitor.service;


import app.brittodev.heartbeatmonitor.dto.CheckResponse;
import app.brittodev.heartbeatmonitor.entity.Check;
import app.brittodev.heartbeatmonitor.entity.CheckStatus;
import app.brittodev.heartbeatmonitor.entity.PingHistory;
import app.brittodev.heartbeatmonitor.repository.CheckRepository;
import app.brittodev.heartbeatmonitor.repository.PingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
public class PingService {

    private final PingRepository pingRepository;
    private final CheckRepository checkRepository;
    private final Clock clock;
    private final AlertService alertService;

    public PingService(PingRepository pingRepository, CheckRepository checkRepository, Clock clock, AlertService alertService) {
        this.pingRepository = pingRepository;
        this.checkRepository = checkRepository;
        this.clock = clock;
        this.alertService = alertService;
    }

    @Transactional
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

        boolean recovered = check.getStatus() == CheckStatus.DOWN;

        if (recovered) {
            check.setStatus(CheckStatus.UP);
        }

        check.setUpdatedAt(receivedAt);

        checkRepository.save(check);

        if (recovered) {
            alertService.alertRecovery(check);
        }

    }

}
