package app.brittodev.heartbeatmonitor.service;


import app.brittodev.heartbeatmonitor.dto.CreateCheckRequest;
import app.brittodev.heartbeatmonitor.entity.Check;
import app.brittodev.heartbeatmonitor.entity.CheckStatus;
import app.brittodev.heartbeatmonitor.repository.CheckRepository;
import app.brittodev.heartbeatmonitor.util.PingTokenGenerator;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CheckService {

    private final CheckRepository checkRepository;
    private final PingTokenGenerator pingTokenGenerator;

    public CheckService(
            CheckRepository checkRepository,
            PingTokenGenerator pingTokenGenerator
    ) {
        this.checkRepository = checkRepository;
        this.pingTokenGenerator = pingTokenGenerator;
    }

    public Check registerCheck(CreateCheckRequest request) {

        String token = pingTokenGenerator.generate();
        Instant now = Instant.now();

        Check check = new Check();

        check.setName(request.name());
        check.setIntervalSeconds(request.intervalSeconds());
        check.setPingToken(token);
        check.setStatus(CheckStatus.UP);
        check.setLastPingAt(null);
        check.setNextExpectedAt(
                now.plusSeconds(request.intervalSeconds())
        );
        check.setCreatedAt(now);
        check.setUpdatedAt(now);

        return checkRepository.save(check);
    }





}
