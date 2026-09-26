package app.brittodev.heartbeatmonitor.service;


import app.brittodev.heartbeatmonitor.entity.Check;
import app.brittodev.heartbeatmonitor.entity.CheckStatus;
import app.brittodev.heartbeatmonitor.repository.CheckRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
public class SweeperService {

    private final CheckRepository checkRepository;
    private final AlertService alertService;
    private final Clock clock;

    public SweeperService(CheckRepository checkRepository, AlertService alertService, Clock clock) {
        this.checkRepository = checkRepository;
        this.alertService = alertService;
        this.clock = clock;
    }

    public void sweep() {
        Instant now = Instant.now(clock);
        List<Check> overdueChecks =
                checkRepository.findByStatusAndNextExpectedAtLessThanEqual(
                        CheckStatus.UP,
                        now
                );

        for (Check check : overdueChecks) {
            check.setStatus(CheckStatus.DOWN);
            check.setUpdatedAt(now);

            checkRepository.save(check);
            alertService.alertDown(check);
        }
    }

    @Scheduled(fixedDelayString = "${heartbeat.sweeper.interval-ms:10000}")
    public void scheduledSweep() {
        sweep();
    }


}
