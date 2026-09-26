package app.brittodev.heartbeatmonitor.repository;

import app.brittodev.heartbeatmonitor.entity.PingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PingRepository extends JpaRepository<PingHistory,Long> {
}
