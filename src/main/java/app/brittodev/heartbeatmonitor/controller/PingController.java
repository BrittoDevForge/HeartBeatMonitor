package app.brittodev.heartbeatmonitor.controller;


import app.brittodev.heartbeatmonitor.service.PingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/ping")
public class PingController {

    private final PingService pingService;

    public PingController(PingService pingService) {
        this.pingService = pingService;
    }

    @PostMapping("/{pingToken}")
    public ResponseEntity<Void> ping(
            @PathVariable("pingToken")
            String token
    ) {
        pingService.ping(token);
        return ResponseEntity.noContent().build();
    }

}
