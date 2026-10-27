
package rw.auca.chargingstation.Controller;

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
import org.springframework.web.bind.annotation.RestController;

import rw.auca.chargingstation.ChargingPoint;
import rw.auca.chargingstation.Service.ChargingPointService;

@RestController
@RequestMapping("/api/charging-points")
public class ChargingPointController {

    private final ChargingPointService chargingPointService;

    public ChargingPointController(ChargingPointService chargingPointService) {
        this.chargingPointService = chargingPointService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ChargingPoint chargingPoint) {
        String message = chargingPointService.save(chargingPoint);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ChargingPoint>> findAll() {
        return ResponseEntity.ok(chargingPointService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        var chargingPoint = chargingPointService.findById(id);

        if (chargingPoint.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The charging point with that id does not exist");
        } else {
            return ResponseEntity.ok(chargingPoint.get());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(
            @PathVariable UUID id,
            @RequestBody ChargingPoint chargingPoint) {

        String message = chargingPointService.update(id, chargingPoint);

        if (message.equals("The charging point with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = chargingPointService.delete(id);

        if (message.equals("The charging point with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }
}
