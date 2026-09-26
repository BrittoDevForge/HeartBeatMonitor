package app.brittodev.heartbeatmonitor.controller;


import app.brittodev.heartbeatmonitor.dto.CheckListResponse;
import app.brittodev.heartbeatmonitor.dto.CheckResponse;
import app.brittodev.heartbeatmonitor.dto.CreateCheckRequest;
import app.brittodev.heartbeatmonitor.dto.PingHistoryResponse;
import app.brittodev.heartbeatmonitor.service.CheckService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/api/v1/checks")
public class CheckController {

    private final CheckService service;

    public CheckController(CheckService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<CheckResponse> registerCheck(
            @Valid @RequestBody CreateCheckRequest request
    ) {
        // log.atInfo().log("Current time : " + Instant.now()); // checking current server time
        return new ResponseEntity<>(
                service.registerCheck(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/")
    public ResponseEntity<Page<CheckListResponse>> getAllChecks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(
                service.listChecks(pageable)
        );
    }

    @GetMapping("/pings/{checkId}")
    public ResponseEntity<Page<PingHistoryResponse>> getPingHistory(
            @PathVariable("checkId") Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(
                service.listPingHistory(id,pageable)
        );
    }

}
