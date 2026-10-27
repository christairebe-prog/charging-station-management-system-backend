
package rw.auca.chargingstation.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rw.auca.chargingstation.ChargingPoint;

public interface ChargingPointRepository extends JpaRepository<ChargingPoint, UUID> {

    @Query("""
            SELECT cp
            FROM ChargingPoint cp
            WHERE NOT EXISTS (
                SELECT t
                FROM Technician t
                WHERE t.chargingPoint = cp
            )
            ORDER BY cp.pointNumber ASC
            """)
    List<ChargingPoint> findFreeChargingPoints();
}
