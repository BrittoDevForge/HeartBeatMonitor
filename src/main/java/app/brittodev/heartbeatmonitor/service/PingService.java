package app.brittodev.heartbeatmonitor.service;


import app.brittodev.heartbeatmonitor.dto.CheckResponse;
import app.brittodev.heartbeatmonitor.entity.Check;
import app.brittodev.heartbeatmonitor.entity.CheckStatus;
import app.brittodev.heartbeatmonitor.entity.PingHistory;
import app.brittodev.heartbeatmonitor.exception.customExceptions.ResourceNotFoundException;
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
    private final AfterCommitExecuter executer;

    public PingService(PingRepository pingRepository, CheckRepository checkRepository, Clock clock, AlertService alertService, AfterCommitExecuter executer) {
        this.pingRepository = pingRepository;
        this.checkRepository = checkRepository;
        this.clock = clock;
        this.alertService = alertService;
        this.executer = executer;
    }

    @Transactional
    public void ping(String pingToken) {

        Instant receivedAt = Instant.now(clock);

        Check check = checkRepository.findByPingTokenForUpdate(pingToken)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid ping token"));

        boolean recovered = check.getStatus() == CheckStatus.DOWN;

        PingHistory ping = new PingHistory();

        ping.setCheckId(check.getId());
        ping.setReceivedAt(receivedAt);

        pingRepository.save(ping);

        check.setLastPingAt(receivedAt); // changing last ping after the ping
        check.setNextExpectedAt(
                receivedAt.plusSeconds(check.getIntervalSeconds())
        ); // calculate and set next expected at


        if (recovered) {
            check.setStatus(CheckStatus.UP);
        }


        check.setUpdatedAt(receivedAt);

        checkRepository.save(check);



        if (recovered) {
            executer.execute(
                    () -> alertService.alertRecovery(check)
            );
        }

    }

}
