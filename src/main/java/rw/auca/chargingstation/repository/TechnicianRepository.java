
package rw.auca.chargingstation.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.auca.chargingstation.Technician;

public interface TechnicianRepository extends JpaRepository<Technician, UUID> {
}
