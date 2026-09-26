package app.brittodev.heartbeatmonitor.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
public class TimeConfig {
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    // Clock.fixed(Instant.parse("2025-09-26T12:00:00.00Z"), ZoneId.of("UTC"));
}
