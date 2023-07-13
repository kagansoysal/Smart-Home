import java.time.Duration;

public class PlugManagement extends ErrorManagement{
    /**
     * If the entered command is in the correct format, it adds the plug to the system.
     * If the amperage value is entered and it is positive, energy is added by starting to consume.
     * @param commands
     */
    public static void add(String[] commands){
        if (!command(commands, 3, 5)){
            return;
        }
        String name = commands[2];
        String status = commands.length >= 4 ? commands[3] : "Off";
        String ampere = commands.length >= 5 ? commands[4] : "1";

        if (!invalidStatus(status) && isNumeric(ampere) && isNotExist(name) && isPositive(ampere, "Ampere")
                && isDouble(ampere, "Ampere")){
            systemCenter.devicesNull.put(name, new SmartPlug(name, status, ampere));
            if (commands.length >= 5){
                plugIn(new String[] {"PlugIn", name, ampere});
            }
        }
    }

    /**
     * Allows a device to be plugged into the plug if it is not.
     * @param commands
     */
    public static void plugIn(String[] commands){
        if (!command(commands, 3, 3)){
            return;
        }
        String name = commands[1];
        String ampere = commands[2];
        if (isNumeric(ampere) && isExist(name) && isPlug(name) && !alreadyPlugIn(name) && isPositive(ampere, "Ampere")) {
            SmartPlug smartPlug = (SmartPlug) whichDataBase(name).get(name);
            smartPlug.isPlugged = "On";
            smartPlug.ampere = Double.parseDouble(ampere);
            whichDataBase(name).replace(name, smartPlug);
            isOnControl(smartPlug);
        }
    }

    /**
     * If a device is attached to the plug, it allows it to be removed.
     * @param commands
     */
    public static void plugOut(String[] commands){
        if (!command(commands, 2,2)){
            return;
        }
        String name = commands[1];
        if (isExist(name) && isPlug(name) && !alreadyPlugOut(name)){
            SmartPlug smartPlug = (SmartPlug) whichDataBase(name).get(name);
            smartPlug.isPlugged = "Off";
            whichDataBase(name).replace(name, smartPlug);
            isOnControl(smartPlug);
        }
    }

    /**
     * Plug calculates the energy it has consumed so far if it is both turned on and disconnected from a device plugged in.
     * @param smartPlug
     * @param duration The time from the last time the plug started to be plugged into both the switch on and a device to the current time
     */
    public static void calculateConsumption(SmartPlug smartPlug, long duration){
        smartPlug.consumption += smartPlug.voltage * smartPlug.ampere *  duration / 60;
    }

    /**
     * When a plug is inserted or removed from the socket,
     * it is checked whether the plug is turned on and the times when it starts to consume energy and ends are determined.
     * Then the consumed energy is calculated.
     * @param smartPlug
     */
    public static void isOnControl(SmartPlug smartPlug){
        if (smartPlug.isPlugged.equals("On") && smartPlug.status.equals("On")){
            smartPlug.onStartTime = systemCenter.time;
        }else if (smartPlug.isPlugged.equals("Off") && smartPlug.status.equals("On")){
            smartPlug.offStartTime = systemCenter.time;
            calculateConsumption(smartPlug, Duration.between(smartPlug.onStartTime, smartPlug.offStartTime).toMinutes());
        }
    }

    /**
     * When a plug is turned on or off, it is checked whether the plug is plugged into the socket,
     * and the times when it starts to consume energy and ends are determined.
     * Then the consumed energy is calculated.
     * @param smartPlug
     */
    public static void isPluggedControl(SmartPlug smartPlug){
        if(smartPlug.status.equals("Off") && smartPlug.isPlugged.equals("On")){
            smartPlug.onStartTime = systemCenter.time;
        }else if (smartPlug.status.equals("On") && smartPlug.isPlugged.equals("On")){
            smartPlug.offStartTime = systemCenter.time;
            calculateConsumption(smartPlug, Duration.between(smartPlug.onStartTime, smartPlug.offStartTime).toMinutes());
        }
    }
}