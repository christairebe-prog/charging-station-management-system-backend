
package rw.auca.chargingstation.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.auca.chargingstation.Technician;

public interface TechnicianRepository extends JpaRepository<Technician, UUID> {

    @Query("""
            SELECT DISTINCT t
            FROM Technician t
            JOIN t.connectorTypes ct
            WHERE LOWER(ct.name) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    List<Technician> findByConnectorTypeName(@Param("name") String name);
}
