
package rw.auca.chargingstation.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.auca.chargingstation.ChargingSession;
import rw.auca.chargingstation.SessionStatus;
import rw.auca.chargingstation.repository.ChargingSessionRepository;

@Service
public class ChargingSessionService {

    private final ChargingSessionRepository chargingSessionRepository;

    public ChargingSessionService(ChargingSessionRepository chargingSessionRepository) {
        this.chargingSessionRepository = chargingSessionRepository;
    }

    public ChargingSession save(ChargingSession chargingSession) {
        return chargingSessionRepository.save(chargingSession);
    }

    public List<ChargingSession> findAll() {
        return chargingSessionRepository.findAll();
    }

    public Optional<ChargingSession> findById(UUID id) {
        return chargingSessionRepository.findById(id);
    }

    public Optional<ChargingSession> update(UUID id, ChargingSession updatedSession) {
        return chargingSessionRepository.findById(id).map(existingSession -> {
            existingSession.setSessionDate(updatedSession.getSessionDate());
            existingSession.setVehicleModel(updatedSession.getVehicleModel());
            existingSession.setKwhRequested(updatedSession.getKwhRequested());
            existingSession.setCustomer(updatedSession.getCustomer());
            existingSession.setTechnician(updatedSession.getTechnician());
            return chargingSessionRepository.save(existingSession);
        });
    }

    public boolean delete(UUID id) {
        if (!chargingSessionRepository.existsById(id)) {
            return false;
        }

        chargingSessionRepository.deleteById(id);
        return true;
    }

    public List<ChargingSession> findByStatus(SessionStatus status) {
        return chargingSessionRepository.findByStatusOrderBySessionDateAsc(status);
    }

    public List<ChargingSession> findBetweenDates(LocalDate start, LocalDate end) {
        return chargingSessionRepository
                .findBySessionDateBetweenOrderBySessionDateAsc(start, end);
    }
}
