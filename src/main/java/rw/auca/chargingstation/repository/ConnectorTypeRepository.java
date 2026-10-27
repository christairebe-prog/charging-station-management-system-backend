
package rw.auca.chargingstation.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.auca.chargingstation.ConnectorType;

public interface ConnectorTypeRepository extends JpaRepository<ConnectorType, UUID> {
}
