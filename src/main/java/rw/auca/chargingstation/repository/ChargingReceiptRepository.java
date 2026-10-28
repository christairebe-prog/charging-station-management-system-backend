
package rw.auca.chargingstation.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.auca.chargingstation.ChargingReceipt;

public interface ChargingReceiptRepository
        extends JpaRepository<ChargingReceipt, UUID> {

    @Query("""
            SELECT r
            FROM ChargingReceipt r
            JOIN r.chargingSession s
            JOIN s.customer c
            WHERE LOWER(c.plateNumber) = LOWER(:plateNumber)
            ORDER BY r.issuedDate DESC
            """)
    List<ChargingReceipt> findByCustomerPlateNumber(
            @Param("plateNumber") String plateNumber);
}
