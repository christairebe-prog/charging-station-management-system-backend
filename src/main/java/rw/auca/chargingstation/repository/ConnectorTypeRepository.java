
package rw.auca.chargingstation.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rw.auca.chargingstation.ConnectorType;

public interface ConnectorTypeRepository extends JpaRepository<ConnectorType, UUID> {

    @Query("""
            SELECT ct
            FROM ConnectorType ct
            WHERE NOT EXISTS (
                SELECT t
                FROM Technician t
                JOIN t.connectorTypes technicianType
                WHERE technicianType = ct
            )
            ORDER BY ct.name ASC
            """)
    List<ConnectorType> findUnusedConnectorTypes();
}
