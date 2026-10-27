
package rw.auca.chargingstation.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.auca.chargingstation.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    List<Customer> findByLastNameIgnoreCaseOrderByFirstNameAsc(
            String lastName);

    @Query("""
            SELECT DISTINCT cs.customer
            FROM ChargingSession cs
            WHERE cs.technician.id = :technicianId
            ORDER BY cs.customer.firstName ASC
            """)
    List<Customer> findCustomersByTechnician(
            @Param("technicianId") UUID technicianId);
}
