import java.time.LocalDateTime;

public class SmartDevice implements Comparable<SmartDevice>{
    String name;
    String status;
    LocalDateTime switchTime = null;
    LocalDateTime onStartTime;
    LocalDateTime offStartTime;

    /**
     * This method is written to sort the LinkedHashMaps according to the switch time.
     * @param otherDevice
     * @return
     */
    public int compareTo(SmartDevice otherDevice) {
        return switchTime.compareTo(otherDevice.switchTime);
    }
}