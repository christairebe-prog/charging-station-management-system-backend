
package rw.auca.chargingstation.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.auca.chargingstation.ChargingPoint;

public interface ChargingPointRepository extends JpaRepository<ChargingPoint, UUID> {
}
