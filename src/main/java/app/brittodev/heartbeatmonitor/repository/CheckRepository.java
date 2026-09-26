package app.brittodev.heartbeatmonitor.repository;

import app.brittodev.heartbeatmonitor.entity.Check;
import app.brittodev.heartbeatmonitor.entity.CheckStatus;
import jakarta.persistence.Column;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Repository
public interface CheckRepository extends JpaRepository<Check,Long> {
    Optional<Check> findByPingToken(String pingToken);

    List<Check> findByStatusAndNextExpectedAtLessThanEqual(
            CheckStatus status,
            Instant now
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Check c where c.id = :id")
    Optional<Check> findByIdForUpdate(
            @Param("id")
            Long id
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Check c WHERE c.pingToken = :pingToken")
    Optional<Check> findByPingTokenForUpdate(
            @Param("pingToken") String pingToken
    );

}
