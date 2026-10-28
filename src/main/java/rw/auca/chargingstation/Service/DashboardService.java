
package rw.auca.chargingstation.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import rw.auca.chargingstation.SessionStatus;
import rw.auca.chargingstation.repository.ChargingPointRepository;
import rw.auca.chargingstation.repository.ChargingReceiptRepository;
import rw.auca.chargingstation.repository.ChargingSessionRepository;
import rw.auca.chargingstation.repository.CustomerRepository;
import rw.auca.chargingstation.repository.TechnicianRepository;

@Service
public class DashboardService {

    private final CustomerRepository customerRepository;
    private final TechnicianRepository technicianRepository;
    private final ChargingPointRepository chargingPointRepository;
    private final ChargingSessionRepository chargingSessionRepository;
    private final ChargingReceiptRepository chargingReceiptRepository;

    public DashboardService(
            CustomerRepository customerRepository,
            TechnicianRepository technicianRepository,
            ChargingPointRepository chargingPointRepository,
            ChargingSessionRepository chargingSessionRepository,
            ChargingReceiptRepository chargingReceiptRepository) {
        this.customerRepository = customerRepository;
        this.technicianRepository = technicianRepository;
        this.chargingPointRepository = chargingPointRepository;
        this.chargingSessionRepository = chargingSessionRepository;
        this.chargingReceiptRepository = chargingReceiptRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();

        summary.put("customers", customerRepository.count());
        summary.put("technicians", technicianRepository.count());
        summary.put("freeChargingPoints",
                (long) chargingPointRepository.findFreeChargingPoints().size());
        summary.put("sessionsToday",
                chargingSessionRepository.countBySessionDate(LocalDate.now()));
        summary.put("reserved",
                chargingSessionRepository.countByStatus(SessionStatus.RESERVED));
        summary.put("receipts", chargingReceiptRepository.count());

        return summary;
    }
}
