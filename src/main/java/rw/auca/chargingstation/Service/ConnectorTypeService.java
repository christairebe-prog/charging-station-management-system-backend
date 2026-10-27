
package rw.auca.chargingstation.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.auca.chargingstation.ConnectorType;
import rw.auca.chargingstation.repository.ConnectorTypeRepository;

@Service
public class ConnectorTypeService {

    private final ConnectorTypeRepository connectorTypeRepository;

    public ConnectorTypeService(ConnectorTypeRepository connectorTypeRepository) {
        this.connectorTypeRepository = connectorTypeRepository;
    }

    public String save(ConnectorType connectorType) {
        connectorTypeRepository.save(connectorType);
        return "Connector type saved successfully";
    }

    public List<ConnectorType> findAll() {
        return connectorTypeRepository.findAll();
    }

    public Optional<ConnectorType> findById(UUID id) {
        return connectorTypeRepository.findById(id);
    }

    public String update(UUID id, ConnectorType updatedConnectorType) {
        Optional<ConnectorType> existing =
                connectorTypeRepository.findById(id);

        if (existing.isEmpty()) {
            return "The connector type with that id does not exist";
        }

        ConnectorType connectorType = existing.get();
        connectorType.setName(updatedConnectorType.getName());

        connectorTypeRepository.save(connectorType);
        return "Connector type updated successfully";
    }

    public String delete(UUID id) {
        Optional<ConnectorType> existing =
                connectorTypeRepository.findById(id);

        if (existing.isEmpty()) {
            return "The connector type with that id does not exist";
        }

        connectorTypeRepository.delete(existing.get());
        return "Connector type deleted successfully";
    }

    public List<ConnectorType> findUnusedConnectorTypes() {
        return connectorTypeRepository.findUnusedConnectorTypes();
    }
}
