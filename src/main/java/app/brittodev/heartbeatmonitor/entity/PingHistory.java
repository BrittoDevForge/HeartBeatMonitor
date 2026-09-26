package app.brittodev.heartbeatmonitor.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "ping_history")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long checkId;
    private Instant receivedAt;
}
