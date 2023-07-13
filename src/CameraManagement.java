import java.time.Duration;
import java.time.LocalDateTime;

public class CameraManagement extends ErrorManagement{
    /**
     * If the command is in the desired format, it adds a camera to the system with its positive megabytes spent per minute.
     * @param command
     */
    public static void add(String[] command){
        if (!command(command,4,5)){
            return;
        }
        String name = command[2];
        String megabytePerMin = command[3];
        String status = command.length >= 5 ? command[4] : "Off";
        LocalDateTime onStartTime = command.length >= 5 && command[4].equals("On") ? systemCenter.time : null;

        if (!invalidStatus(status) && isNumeric(megabytePerMin) && isNotExist(name)
                && isPositive(megabytePerMin, "Megabyte") && isDouble(megabytePerMin, "Megabyte")){
            systemCenter.devicesNull.put(name, new SmartCamera(name, megabytePerMin, status, onStartTime));
        }
    }

    /**
     * When the smart camera is turned off, it updates the total storage it has used during its open time.
     * @param smartCamera related device
     * @param duration the time from the previous opening to the current time.
     */
    public static void calculateStorage(SmartCamera smartCamera, long duration){
        smartCamera.storage += smartCamera.megabytePerMin *  duration;
    }

    /**
     * Controls whether the camera is turned on or off when it's time to switch.
     * @param smartCamera related device
     */
    public static void updateCamera(SmartCamera smartCamera) {
        switch (smartCamera.status) {
            case "Off":
                smartCamera.onStartTime = systemCenter.time;
                break;
            case "On":
                smartCamera.offStartTime = systemCenter.time;
                calculateStorage(smartCamera, Duration.between(smartCamera.onStartTime, smartCamera.offStartTime).toMinutes());
                break;
        }
    }
}