package app.brittodev.heartbeatmonitor.repository;

import app.brittodev.heartbeatmonitor.entity.Check;
import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CheckRepository extends JpaRepository<Check,Long> {
    Optional<Check> findByPingToken(String pingToken);
}
