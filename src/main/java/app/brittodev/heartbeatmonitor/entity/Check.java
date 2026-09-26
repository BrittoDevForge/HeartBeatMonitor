package app.brittodev.heartbeatmonitor.entity;


import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "checks",
        indexes = {
                @Index(
                        name = "idx_status_next",
                        columnList = "status, next_expected_at"
                )
        }
)
public class Check {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long intervalSeconds;

    @Column(nullable = false,unique = true)
    private String pingToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CheckStatus status;

    private Instant lastPingAt;
    @Column(name = "next_expected_at")
    private Instant nextExpectedAt;
    private Instant createdAt;
    private Instant updatedAt;
}
