
package rw.auca.chargingstation.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.auca.chargingstation.ChargingReceipt;
import rw.auca.chargingstation.ChargingSession;
import rw.auca.chargingstation.SessionStatus;
import rw.auca.chargingstation.repository.ChargingReceiptRepository;
import rw.auca.chargingstation.repository.ChargingSessionRepository;

@Service
public class ChargingReceiptService {

    private final ChargingReceiptRepository chargingReceiptRepository;
    private final ChargingSessionRepository chargingSessionRepository;

    public ChargingReceiptService(
            ChargingReceiptRepository chargingReceiptRepository,
            ChargingSessionRepository chargingSessionRepository) {
        this.chargingReceiptRepository = chargingReceiptRepository;
        this.chargingSessionRepository = chargingSessionRepository;
    }

    public String saveReceipt(UUID sessionId, ChargingReceipt receipt) {
        Optional<ChargingSession> sessionOptional =
                chargingSessionRepository.findById(sessionId);

        if (sessionOptional.isEmpty()) {
            return "The charging session with that id does not exist";
        }

        ChargingSession session = sessionOptional.get();

        if (session.getStatus() != SessionStatus.ENDED) {
            return "A receipt can only be issued for an ended charging session";
        }

        receipt.setChargingSession(session);
        receipt.setIssuedDate(LocalDate.now());

        chargingReceiptRepository.save(receipt);

        return "Charging receipt saved successfully";
    }

    public List<ChargingReceipt> getReceiptsByCustomerPlateNumber(
            String plateNumber) {
        return chargingReceiptRepository.findByCustomerPlateNumber(plateNumber);
    }

    public List<ChargingReceipt> getAllReceipts() {
        return chargingReceiptRepository.findAll();
    }

    public Optional<ChargingReceipt> getReceiptById(UUID id) {
        return chargingReceiptRepository.findById(id);
    }

    public String deleteReceipt(UUID id) {
        if (!chargingReceiptRepository.existsById(id)) {
            return "The charging receipt with that id does not exist";
        }

        chargingReceiptRepository.deleteById(id);
        return "Charging receipt deleted successfully";
    }
}
