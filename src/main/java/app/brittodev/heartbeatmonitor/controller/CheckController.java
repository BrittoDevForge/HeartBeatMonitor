package app.brittodev.heartbeatmonitor.controller;


import app.brittodev.heartbeatmonitor.dto.CheckResponse;
import app.brittodev.heartbeatmonitor.dto.CreateCheckRequest;
import app.brittodev.heartbeatmonitor.service.CheckService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestBody CreateCheckRequest request
    ) {
        log.atInfo().log("Current time : " + Instant.now()); // checking current server time
        return new ResponseEntity<>(
                service.registerCheck(request),
                HttpStatus.CREATED
        );
    }
}
