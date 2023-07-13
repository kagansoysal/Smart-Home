import java.time.LocalDateTime;

public class SmartCamera extends SmartDevice{
    double storage;
    double megabytePerMin;

    SmartCamera(String name, String megabytePerMin, String status, LocalDateTime onStartTime){
        this.name = name;
        this.megabytePerMin = Double.parseDouble(megabytePerMin);
        this.status = status;
        this.onStartTime = onStartTime;
    }
}