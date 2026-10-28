
package rw.auca.chargingstation.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rw.auca.chargingstation.ChargingReceipt;
import rw.auca.chargingstation.Service.ChargingReceiptService;

@RestController
@RequestMapping("/api/charging-receipts")
public class ChargingReceiptController {

    private final ChargingReceiptService chargingReceiptService;

    public ChargingReceiptController(
            ChargingReceiptService chargingReceiptService) {
        this.chargingReceiptService = chargingReceiptService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveReceipt(
            @RequestParam UUID sessionId,
            @RequestBody ChargingReceipt receipt) {

        String result = chargingReceiptService.saveReceipt(sessionId, receipt);

        if (result.equals("The charging session with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        if (result.equals(
                "A receipt can only be issued for an ended charging session")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-customer")
    public List<ChargingReceipt> getReceiptsByCustomerPlateNumber(
            @RequestParam String plateNumber) {
        return chargingReceiptService
                .getReceiptsByCustomerPlateNumber(plateNumber);
    }

    @GetMapping("/all")
    public List<ChargingReceipt> getAllReceipts() {
        return chargingReceiptService.getAllReceipts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargingReceipt> getReceiptById(
            @PathVariable UUID id) {
        Optional<ChargingReceipt> receipt =
                chargingReceiptService.getReceiptById(id);

        return receipt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReceipt(@PathVariable UUID id) {
        String result = chargingReceiptService.deleteReceipt(id);

        if (result.equals("The charging receipt with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.ok(result);
    }
}
