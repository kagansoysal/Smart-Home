import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * <p>The basic information of the entire system is kept here. Time information and registered devices are here.
 * Two data structures, devicesSwitchTime and devicesNull, are used to set the order correctly in the reports.
 * And for the nop command to work, the switch times are kept.</p>
 * @see Management#nop()
 */
public class SystemCenter {
    LocalDateTime time;
    LinkedHashMap<String,SmartDevice> devicesNull = new LinkedHashMap<>();
    LinkedHashMap<String,SmartDevice> devicesSwitchTime = new LinkedHashMap<>();
    ArrayList<LocalDateTime> switchTimes = new ArrayList<>();
}