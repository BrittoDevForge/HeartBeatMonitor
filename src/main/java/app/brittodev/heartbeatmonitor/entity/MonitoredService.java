package app.brittodev.heartbeatmonitor.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "monitored_services")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonitoredService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String heartbeatUrl;

    @Column(nullable = false)
    private Instant lastHeartbeat;

    @Column(nullable = false)
    private boolean active;

}
