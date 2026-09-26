package app.brittodev.heartbeatmonitor.service;


import app.brittodev.heartbeatmonitor.dto.CheckListResponse;
import app.brittodev.heartbeatmonitor.dto.CheckResponse;
import app.brittodev.heartbeatmonitor.dto.CreateCheckRequest;
import app.brittodev.heartbeatmonitor.entity.Check;
import app.brittodev.heartbeatmonitor.entity.CheckStatus;
import app.brittodev.heartbeatmonitor.repository.CheckRepository;
import app.brittodev.heartbeatmonitor.util.PingTokenGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
public class CheckService {

    private final CheckRepository checkRepository;
    private final PingTokenGenerator pingTokenGenerator;
    private final Clock clock;

    public CheckService(
            CheckRepository checkRepository,
            PingTokenGenerator pingTokenGenerator, Clock clock
    ) {
        this.checkRepository = checkRepository;
        this.pingTokenGenerator = pingTokenGenerator;
        this.clock = clock;
    }

    public CheckResponse registerCheck(CreateCheckRequest request) {

        String token = pingTokenGenerator.generate();
        Instant now = Instant.now(clock);
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

        Check savedCheck = checkRepository.save(check);

        String pingUrl = "/api/v1/ping/" + savedCheck.getPingToken();

        return new CheckResponse(
                check.getId(),
                check.getName(),
                check.getIntervalSeconds(),
                check.getStatus().name(),
                check.getLastPingAt(),
                check.getNextExpectedAt(),
                pingUrl
        );
    }

    public Page<CheckListResponse> listChecks(Pageable pageable) {
        return checkRepository.findAll(pageable)
                .map( check -> new CheckListResponse(
                        check.getId(),
                        check.getName(),
                        check.getIntervalSeconds(),
                        check.getStatus().name(),
                        check.getLastPingAt(),
                        check.getNextExpectedAt()
                        )
                );
    }





}
