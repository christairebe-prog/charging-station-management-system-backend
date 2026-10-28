
package rw.auca.chargingstation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.auca.chargingstation.ChargingSession;
import rw.auca.chargingstation.SessionStatus;

public interface ChargingSessionRepository extends JpaRepository<ChargingSession, UUID> {

    List<ChargingSession> findByStatusOrderBySessionDateAsc(SessionStatus status);

    List<ChargingSession> findBySessionDateBetweenOrderBySessionDateAsc(
            LocalDate start,
            LocalDate end);

    @Query("""
            SELECT s.status, COUNT(s)
            FROM ChargingSession s
            GROUP BY s.status
            """)
    List<Object[]> countSessionsByStatus();

    @Modifying
    @Query("""
            UPDATE ChargingSession s
            SET s.status = rw.auca.chargingstation.SessionStatus.CANCELLED
            WHERE s.technician.id = :technicianId
              AND s.sessionDate = :date
              AND s.status <> rw.auca.chargingstation.SessionStatus.ENDED
            """)
    int cancelSessionsForTechnicianOnDate(
            @Param("technicianId") UUID technicianId,
            @Param("date") LocalDate date);

    long countBySessionDate(LocalDate sessionDate);

    long countByStatus(SessionStatus status);
}
