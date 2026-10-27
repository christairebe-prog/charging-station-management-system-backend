
package rw.auca.chargingstation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.auca.chargingstation.ChargingSession;
import rw.auca.chargingstation.SessionStatus;

public interface ChargingSessionRepository extends JpaRepository<ChargingSession, UUID> {

    List<ChargingSession> findByStatusOrderBySessionDateAsc(SessionStatus status);

    List<ChargingSession> findBySessionDateBetweenOrderBySessionDateAsc(
            LocalDate start,
            LocalDate end);
}
