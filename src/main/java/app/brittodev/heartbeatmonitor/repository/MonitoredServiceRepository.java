package app.brittodev.heartbeatmonitor.repository;

import app.brittodev.heartbeatmonitor.entity.MonitoredService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository // for convenient
public interface MonitoredServiceRepository
    extends JpaRepository<MonitoredService, Long> {

}
