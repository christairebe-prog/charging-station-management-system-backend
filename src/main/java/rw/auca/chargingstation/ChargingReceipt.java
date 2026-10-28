
package rw.auca.chargingstation;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "charging_receipt")
public class ChargingReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String tariffName;

    private String remarks;

    private int kwhDelivered;

    private LocalDate issuedDate;

    @ManyToOne
    @JoinColumn(name = "charging_session_id")
    private ChargingSession chargingSession;

    public ChargingReceipt() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTariffName() {
        return tariffName;
    }

    public void setTariffName(String tariffName) {
        this.tariffName = tariffName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getKwhDelivered() {
        return kwhDelivered;
    }

    public void setKwhDelivered(int kwhDelivered) {
        this.kwhDelivered = kwhDelivered;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public ChargingSession getChargingSession() {
        return chargingSession;
    }

    public void setChargingSession(ChargingSession chargingSession) {
        this.chargingSession = chargingSession;
    }
}
