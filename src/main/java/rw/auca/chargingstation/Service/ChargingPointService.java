
package rw.auca.chargingstation.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.auca.chargingstation.ChargingPoint;
import rw.auca.chargingstation.repository.ChargingPointRepository;

@Service
public class ChargingPointService {

    private final ChargingPointRepository chargingPointRepository;

    public ChargingPointService(ChargingPointRepository chargingPointRepository) {
        this.chargingPointRepository = chargingPointRepository;
    }

    public String save(ChargingPoint chargingPoint) {
        chargingPointRepository.save(chargingPoint);
        return "Charging point saved successfully";
    }

    public List<ChargingPoint> findAll() {
        return chargingPointRepository.findAll();
    }

    public Optional<ChargingPoint> findById(UUID id) {
        return chargingPointRepository.findById(id);
    }

    public String update(UUID id, ChargingPoint updatedPoint) {
        Optional<ChargingPoint> existing = chargingPointRepository.findById(id);

        if (existing.isEmpty()) {
            return "The charging point with that id does not exist";
        }

        ChargingPoint chargingPoint = existing.get();
        chargingPoint.setPointNumber(updatedPoint.getPointNumber());
        chargingPoint.setName(updatedPoint.getName());

        chargingPointRepository.save(chargingPoint);
        return "Charging point updated successfully";
    }

    public String delete(UUID id) {
        Optional<ChargingPoint> existing = chargingPointRepository.findById(id);

        if (existing.isEmpty()) {
            return "The charging point with that id does not exist";
        }

        chargingPointRepository.delete(existing.get());
        return "Charging point deleted successfully";
    }

    public List<ChargingPoint> findFreeChargingPoints() {
        return chargingPointRepository.findFreeChargingPoints();
    }
}
