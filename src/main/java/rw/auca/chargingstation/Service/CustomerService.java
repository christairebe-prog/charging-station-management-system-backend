
package rw.auca.chargingstation.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.auca.chargingstation.Customer;
import rw.auca.chargingstation.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public String save(Customer customer) {
        customerRepository.save(customer);
        return "Customer saved successfully";
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(UUID id) {
        return customerRepository.findById(id);
    }

    public String update(UUID id, Customer updatedCustomer) {
        Optional<Customer> existing = customerRepository.findById(id);

        if (existing.isEmpty()) {
            return "The customer with that id does not exist";
        }

        Customer customer = existing.get();
        customer.setFirstName(updatedCustomer.getFirstName());
        customer.setLastName(updatedCustomer.getLastName());
        customer.setPlateNumber(updatedCustomer.getPlateNumber());
        customer.setPhone(updatedCustomer.getPhone());
        customer.setEmail(updatedCustomer.getEmail());

        customerRepository.save(customer);
        return "Customer updated successfully";
    }

    public String delete(UUID id) {
        Optional<Customer> existing = customerRepository.findById(id);

        if (existing.isEmpty()) {
            return "The customer with that id does not exist";
        }

        customerRepository.delete(existing.get());
        return "Customer deleted successfully";
    }

    public List<Customer> findByLastName(String lastName) {
        return customerRepository
                .findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Customer> findCustomersByTechnician(UUID technicianId) {
        return customerRepository.findCustomersByTechnician(technicianId);
    }
}
