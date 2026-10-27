
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

import rw.auca.chargingstation.ConnectorType;
import rw.auca.chargingstation.Service.ConnectorTypeService;

@RestController
@RequestMapping("/api/connector-types")
public class ConnectorTypeController {

    private final ConnectorTypeService connectorTypeService;

    public ConnectorTypeController(
            ConnectorTypeService connectorTypeService) {
        this.connectorTypeService = connectorTypeService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(
            @RequestBody ConnectorType connectorType) {

        String message = connectorTypeService.save(connectorType);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ConnectorType>> findAll() {
        return ResponseEntity.ok(connectorTypeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        var connectorType = connectorTypeService.findById(id);

        if (connectorType.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The connector type with that id does not exist");
        } else {
            return ResponseEntity.ok(connectorType.get());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(
            @PathVariable UUID id,
            @RequestBody ConnectorType connectorType) {

        String message = connectorTypeService.update(id, connectorType);

        if (message.equals(
                "The connector type with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = connectorTypeService.delete(id);

        if (message.equals(
                "The connector type with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.ok(message);
        }
    }
}
