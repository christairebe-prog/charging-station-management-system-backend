
package rw.auca.chargingstation.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rw.auca.chargingstation.Service.TechnicianService;
import rw.auca.chargingstation.Technician;

@RestController
@RequestMapping("/api/technicians")
public class TechnicianController {

    private final TechnicianService technicianService;

    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Technician technician) {
        String message = technicianService.save(technician);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Technician>> findAll() {
        return ResponseEntity.ok(technicianService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        var technician = technicianService.findById(id);

        if (technician.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The technician with that id does not exist");
        }

        return ResponseEntity.ok(technician.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(
            @PathVariable UUID id,
            @RequestBody Technician technician) {

        String message = technicianService.update(id, technician);

        if (message.equals("The technician with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = technicianService.delete(id);

        if (message.equals("The technician with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        return ResponseEntity.ok(message);
    }

    @GetMapping("/by-connector-type")
    public ResponseEntity<List<Technician>> findByConnectorType(
            @RequestParam String name) {

        return ResponseEntity.ok(
                technicianService.findByConnectorTypeName(name));
    }

    @PutMapping("/{technicianId}/connector-types/{connectorTypeId}")
    public ResponseEntity<String> addConnectorType(
            @PathVariable UUID technicianId,
            @PathVariable UUID connectorTypeId) {

        String message = technicianService.addConnectorType(
                technicianId, connectorTypeId);

        if (message.equals("The technician with that id does not exist")
                || message.equals("That connector type does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        if (message.equals("The technician already has that connector type")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }

        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{technicianId}/charging-point")
    public ResponseEntity<String> changeChargingPoint(
            @PathVariable UUID technicianId,
            @RequestParam int pointNumber) {

        String message = technicianService.changeChargingPoint(
                technicianId, pointNumber);

        if (message.equals("The technician with that id does not exist")
                || message.equals("The charging point with that pointNumber does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        if (message.equals("That charging point is taken by another technician")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }

        return ResponseEntity.ok(message);
    }
}
