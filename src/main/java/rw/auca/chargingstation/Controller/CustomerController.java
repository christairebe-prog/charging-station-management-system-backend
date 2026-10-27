
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rw.auca.chargingstation.Customer;
import rw.auca.chargingstation.Service.CustomerService;
import rw.auca.chargingstation.Service.TechnicianService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final TechnicianService technicianService;

    public CustomerController(
            CustomerService customerService,
            TechnicianService technicianService) {
        this.customerService = customerService;
        this.technicianService = technicianService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Customer customer) {
        String message = customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        var customer = customerService.findById(id);

        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The customer with that id does not exist");
        }

        return ResponseEntity.ok(customer.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(
            @PathVariable UUID id,
            @RequestBody Customer customer) {

        String message = customerService.update(id, customer);

        if (message.equals("The customer with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = customerService.delete(id);

        if (message.equals("The customer with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        return ResponseEntity.ok(message);
    }

    @GetMapping("/by-last-name")
    public ResponseEntity<List<Customer>> findByLastName(
            @RequestParam String lastName) {
        return ResponseEntity.ok(
                customerService.findByLastName(lastName));
    }

    @GetMapping("/of-technician/{technicianId}")
    public ResponseEntity<?> findCustomersByTechnician(
            @PathVariable UUID technicianId) {

        if (technicianService.findById(technicianId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The technician with that id does not exist");
        }

        return ResponseEntity.ok(
                customerService.findCustomersByTechnician(technicianId));
    }
}
