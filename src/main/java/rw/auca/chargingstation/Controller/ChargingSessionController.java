
package rw.auca.chargingstation.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rw.auca.chargingstation.ChargingSession;
import rw.auca.chargingstation.Service.ChargingSessionService;
import rw.auca.chargingstation.SessionStatus;

@RestController
@RequestMapping("/api/charging-sessions")
public class ChargingSessionController {

    private final ChargingSessionService chargingSessionService;

    public ChargingSessionController(ChargingSessionService chargingSessionService) {
        this.chargingSessionService = chargingSessionService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ChargingSession chargingSession) {
        chargingSessionService.save(chargingSession);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Charging session saved successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<ChargingSession>> findAll() {
        return ResponseEntity.ok(chargingSessionService.findAll());
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<ChargingSession>> findByStatus(
            @RequestParam SessionStatus status) {
        return ResponseEntity.ok(chargingSessionService.findByStatus(status));
    }

    @GetMapping("/between")
    public ResponseEntity<List<ChargingSession>> findBetweenDates(
            @RequestParam String start,
            @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        return ResponseEntity.ok(
                chargingSessionService.findBetweenDates(startDate, endDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        return chargingSessionService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The charging session with that id does not exist"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody ChargingSession chargingSession) {
        return chargingSessionService.update(id, chargingSession)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The charging session with that id does not exist"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        if (chargingSessionService.delete(id)) {
            return ResponseEntity.ok("Charging session deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The charging session with that id does not exist");
    }
}
