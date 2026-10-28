
package rw.auca.chargingstation.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.auca.chargingstation.ChargingPoint;
import rw.auca.chargingstation.ConnectorType;
import rw.auca.chargingstation.Technician;
import rw.auca.chargingstation.repository.ChargingPointRepository;
import rw.auca.chargingstation.repository.ConnectorTypeRepository;
import rw.auca.chargingstation.repository.TechnicianRepository;

@Service
public class TechnicianService {

    private final TechnicianRepository technicianRepository;
    private final ConnectorTypeRepository connectorTypeRepository;
    private final ChargingPointRepository chargingPointRepository;

    public TechnicianService(
            TechnicianRepository technicianRepository,
            ConnectorTypeRepository connectorTypeRepository,
            ChargingPointRepository chargingPointRepository) {
        this.technicianRepository = technicianRepository;
        this.connectorTypeRepository = connectorTypeRepository;
        this.chargingPointRepository = chargingPointRepository;
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

    public List<Technician> findByConnectorTypeName(String name) {
        return technicianRepository.findByConnectorTypeName(name);
    }

    public String addConnectorType(
            UUID technicianId, UUID connectorTypeId) {

        Optional<Technician> existingTechnician =
                technicianRepository.findById(technicianId);

        if (existingTechnician.isEmpty()) {
            return "The technician with that id does not exist";
        }

        Optional<ConnectorType> existingConnectorType =
                connectorTypeRepository.findById(connectorTypeId);

        if (existingConnectorType.isEmpty()) {
            return "That connector type does not exist";
        }

        Technician technician = existingTechnician.get();
        ConnectorType connectorType = existingConnectorType.get();

        List<ConnectorType> connectorTypes = technician.getConnectorTypes();

        if (connectorTypes == null) {
            connectorTypes = new ArrayList<>();
            technician.setConnectorTypes(connectorTypes);
        }

        boolean alreadyAssigned = connectorTypes.stream()
                .anyMatch(existingType ->
                        existingType.getId().equals(connectorTypeId));

        if (alreadyAssigned) {
            return "The technician already has that connector type";
        }

        connectorTypes.add(connectorType);
        technicianRepository.save(technician);

        return "Connector type added to the technician";
    }

    public String changeChargingPoint(
            UUID technicianId, int pointNumber) {

        Optional<Technician> existingTechnician =
                technicianRepository.findById(technicianId);

        if (existingTechnician.isEmpty()) {
            return "The technician with that id does not exist";
        }

        Optional<ChargingPoint> requestedPoint =
                chargingPointRepository.findAll().stream()
                        .filter(point -> point.getPointNumber() == pointNumber)
                        .findFirst();

        if (requestedPoint.isEmpty()) {
            return "The charging point with that pointNumber does not exist";
        }

        ChargingPoint chargingPoint = requestedPoint.get();

        boolean takenByAnotherTechnician =
                technicianRepository.findAll().stream()
                        .filter(other -> !other.getId().equals(technicianId))
                        .anyMatch(other ->
                                other.getChargingPoint() != null
                                && other.getChargingPoint().getId()
                                        .equals(chargingPoint.getId()));

        if (takenByAnotherTechnician) {
            return "That charging point is taken by another technician";
        }

        Technician technician = existingTechnician.get();
        technician.setChargingPoint(chargingPoint);
        technicianRepository.save(technician);

        return "Charging point changed successfully";
    }
}
