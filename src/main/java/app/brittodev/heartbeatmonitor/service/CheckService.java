package app.brittodev.heartbeatmonitor.service;


import app.brittodev.heartbeatmonitor.dto.CheckListResponse;
import app.brittodev.heartbeatmonitor.dto.CheckResponse;
import app.brittodev.heartbeatmonitor.dto.CreateCheckRequest;
import app.brittodev.heartbeatmonitor.dto.PingHistoryResponse;
import app.brittodev.heartbeatmonitor.entity.Check;
import app.brittodev.heartbeatmonitor.entity.CheckStatus;
import app.brittodev.heartbeatmonitor.entity.PingHistory;
import app.brittodev.heartbeatmonitor.exception.customExceptions.ResourceNotFoundException;
import app.brittodev.heartbeatmonitor.mapper.CheckMapper;
import app.brittodev.heartbeatmonitor.repository.CheckRepository;
import app.brittodev.heartbeatmonitor.repository.PingRepository;
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
    private final PingRepository pingRepository;

    public CheckService(
            CheckRepository checkRepository,
            PingTokenGenerator pingTokenGenerator, Clock clock, PingRepository pingRepository
    ) {
        this.checkRepository = checkRepository;
        this.pingTokenGenerator = pingTokenGenerator;
        this.clock = clock;
        this.pingRepository = pingRepository;
    }

    public CheckResponse registerCheck(CreateCheckRequest request) {

        String token = pingTokenGenerator.generate();
        Instant now = Instant.now(clock);

        Check check = CheckMapper.toCheckEntity(request,token,now);
        Check savedCheck = checkRepository.save(check);

        return CheckMapper.toCheckResponse(savedCheck);
    }

    public Page<CheckListResponse> listChecks(Pageable pageable) {
        Page<Check> checks = checkRepository.findAll(pageable);
        return CheckMapper.toCheckListResponse(checks);
    }

    public Page<PingHistoryResponse> listPingHistory(
            Long checkId,
            Pageable pageable
    ) {
        checkRepository.findById(checkId).orElseThrow( () ->
                new ResourceNotFoundException("Check is not found"));
        Page<PingHistory> pings = pingRepository
                .findByCheckIdOrderByReceivedAtDesc(checkId,pageable);
        return CheckMapper.toPingHistoryResponse(pings);
    }

}
