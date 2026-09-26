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
    private final AfterCommitExecuter executer;

    public SweeperService(CheckRepository checkRepository, AlertService alertService, Clock clock, AfterCommitExecuter executer) {
        this.checkRepository = checkRepository;
        this.alertService = alertService;
        this.clock = clock;
        this.executer = executer;
    }

    public void sweep() {
        Instant now = Instant.now(clock);
        List<Check> overdueChecks =
                checkRepository.findByStatusAndNextExpectedAtLessThanEqual(
                        CheckStatus.UP,
                        now
                );

        for (Check check : overdueChecks) {

            check = checkRepository.findByIdForUpdate(check.getId())
                            .orElseThrow( () -> new RuntimeException("check not found"));
            check.setStatus(CheckStatus.DOWN);
            check.setUpdatedAt(now);

            checkRepository.save(check);
            Check finalCheck = check;
            executer.execute(
                    () -> alertService.alertDown(finalCheck)
            );
        }
    }

    @Scheduled(fixedDelayString = "${heartbeat.sweeper.interval-ms:10000}")
    public void scheduledSweep() {
        sweep();
    }


}
