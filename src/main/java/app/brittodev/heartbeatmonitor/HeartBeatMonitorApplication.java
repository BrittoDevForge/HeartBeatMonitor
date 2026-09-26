package app.brittodev.heartbeatmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HeartBeatMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeartBeatMonitorApplication.class, args);
    }

}
