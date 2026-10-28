
package rw.auca.chargingstation.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
        LocalDate sessionDate = chargingSession.getSessionDate();

        if (sessionDate == null || sessionDate.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Charging session date cannot be in the past");
        }

        UUID technicianId = chargingSession.getTechnician() == null
                ? null
                : chargingSession.getTechnician().getId();

        if (technicianId != null) {
            boolean technicianAlreadyBooked = chargingSessionRepository.findAll().stream()
                    .anyMatch(existingSession ->
                            existingSession.getSessionDate().equals(sessionDate)
                                    && existingSession.getTechnician() != null
                                    && technicianId.equals(existingSession.getTechnician().getId())
                                    && existingSession.getStatus() != SessionStatus.CANCELLED);

            if (technicianAlreadyBooked) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Technician already has a charging session on that date");
            }
        }

        chargingSession.setStatus(SessionStatus.RESERVED);
        return chargingSessionRepository.save(chargingSession);
    }

    public List<ChargingSession> findAll() {
        return chargingSessionRepository.findAll();
    }

    public Page<ChargingSession> findAll(Pageable pageable) {
        return chargingSessionRepository.findAll(pageable);
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
        return chargingSessionRepository.findBySessionDateBetweenOrderBySessionDateAsc(start, end);
    }

    public String updateStatus(UUID id, SessionStatus newStatus) {
        Optional<ChargingSession> optionalSession = chargingSessionRepository.findById(id);

        if (optionalSession.isEmpty()) {
            return "The charging session with that id does not exist";
        }

        ChargingSession session = optionalSession.get();
        SessionStatus currentStatus = session.getStatus();

        boolean allowedTransition =
                (currentStatus == SessionStatus.RESERVED
                        && (newStatus == SessionStatus.CHARGING
                        || newStatus == SessionStatus.CANCELLED))
                || (currentStatus == SessionStatus.CHARGING
                        && (newStatus == SessionStatus.ENDED
                        || newStatus == SessionStatus.CANCELLED));

        if (!allowedTransition) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot change status from " + currentStatus + " to " + newStatus);
        }

        session.setStatus(newStatus);
        chargingSessionRepository.save(session);

        return "Charging session status updated successfully";
    }

    public List<Object[]> countSessionsByStatus() {
        return chargingSessionRepository.countSessionsByStatus();
    }

    @Transactional
    public int cancelSessionsForTechnicianOnDate(UUID technicianId, LocalDate date) {
        return chargingSessionRepository.cancelSessionsForTechnicianOnDate(technicianId, date);
    }
}
