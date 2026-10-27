
package rw.auca.chargingstation.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.auca.chargingstation.Technician;
import rw.auca.chargingstation.repository.TechnicianRepository;

@Service
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    public String save(Technician technician) {
        technicianRepository.save(technician);
        return "Technician saved successfully";
    }

    public List<Technician> findAll() {
        return technicianRepository.findAll();
    }

    public Optional<Technician> findById(UUID id) {
        return technicianRepository.findById(id);
    }

    public String update(UUID id, Technician updatedTechnician) {
        Optional<Technician> existing = technicianRepository.findById(id);

        if (existing.isEmpty()) {
            return "The technician with that id does not exist";
        }

        Technician technician = existing.get();
        technician.setFirstName(updatedTechnician.getFirstName());
        technician.setLastName(updatedTechnician.getLastName());
        technician.setPhone(updatedTechnician.getPhone());
        technician.setChargingPoint(updatedTechnician.getChargingPoint());
        technician.setConnectorTypes(updatedTechnician.getConnectorTypes());

        technicianRepository.save(technician);
        return "Technician updated successfully";
    }

    public String delete(UUID id) {
        Optional<Technician> existing = technicianRepository.findById(id);

        if (existing.isEmpty()) {
            return "The technician with that id does not exist";
        }

        technicianRepository.delete(existing.get());
        return "Technician deleted successfully";
    }
}
