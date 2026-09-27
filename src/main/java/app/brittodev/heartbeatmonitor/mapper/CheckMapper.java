package app.brittodev.heartbeatmonitor.mapper;

import app.brittodev.heartbeatmonitor.dto.CheckListResponse;
import app.brittodev.heartbeatmonitor.dto.CheckResponse;
import app.brittodev.heartbeatmonitor.dto.CreateCheckRequest;
import app.brittodev.heartbeatmonitor.dto.PingHistoryResponse;
import app.brittodev.heartbeatmonitor.entity.Check;
import app.brittodev.heartbeatmonitor.entity.CheckStatus;
import app.brittodev.heartbeatmonitor.entity.PingHistory;
import org.springframework.data.domain.Page;

import java.time.Clock;
import java.time.Instant;

public class CheckMapper {

    public static Check toCheckEntity(
            CreateCheckRequest request,
            String token,
            Instant now
    ) {
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

        return check;

    }

    public static CheckResponse toCheckResponse(Check check) {
        String pingUrl = "/api/v1/ping/" + check.getPingToken();
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

    public static Page<CheckListResponse> toCheckListResponse(
            Page<Check> checks
    ) {
        return checks.map( check -> new CheckListResponse(
                check.getId(),
                check.getName(),
                check.getIntervalSeconds(),
                check.getStatus().name(),
                check.getLastPingAt(),
                check.getNextExpectedAt()
                )
        );
    }

    public static Page<PingHistoryResponse> toPingHistoryResponse(
            Page<PingHistory> pings
    ) {
        return pings.map( ping -> new PingHistoryResponse(
                ping.getId(),
                ping.getCheckId(),
                ping.getReceivedAt()
                )
        );
    }
}
