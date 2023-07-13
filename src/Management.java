import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

public class Management{
    static SystemCenter systemCenter = new SystemCenter();

    /**
     * Deletes the desired device. Turns it off if it was open before deleting it. Calculates the consumption and storage values of the camera and plug.
     * @param command
     */
    public static void remove(String[] command) {
        if (!ErrorManagement.command(command, 2,2)){
            return;
        }
        String name = command[1];
        if (ErrorManagement.isExist(name)){
            if (whichDataBase(name).get(name) instanceof SmartPlug){
                PlugManagement.isPluggedControl((SmartPlug) whichDataBase(name).get(name));
            }else if(whichDataBase(name).get(name) instanceof SmartCamera){
                CameraManagement.updateCamera((SmartCamera) whichDataBase(name).get(name));
            }
            whichDataBase(name).get(name).status = "Off";
            Output.removeOutput();
            Output.smartDeviceSentence(whichDataBase(name).get(name));
            whichDataBase(name).remove(name);
        }
    }

    /**
     * Changes the name of the desired device.
     * @param command
     */
    public static void changeName(String[] command) {
        if (!ErrorManagement.command(command, 3,3)){
            return;
        }
        String oldName = command[1];
        String newName = command[2];

        if (!ErrorManagement.sameName(oldName, newName) && ErrorManagement.isExist(oldName)
                && ErrorManagement.isNotExist(newName)){
            LinkedHashMap<String, SmartDevice> whichDataBase = whichDataBase(oldName);
            ArrayList<String> keyList = new ArrayList<>(whichDataBase(oldName).keySet());
            ArrayList<SmartDevice> valueList = new ArrayList<>(whichDataBase(oldName).values());
            int index = keyList.indexOf(oldName);
            keyList.remove(oldName);
            keyList.add(index, newName);
            valueList.get(index).name = newName;
            whichDataBase(oldName).clear();
            for (int i = 0; i < keyList.size(); i++) {
                whichDataBase.put(keyList.get(i), valueList.get(i));
            }
        }
    }

    /**
     * At first it specifies what time it is and afterwards this command cannot be used.
     * @param command
     */
    public static void setInitialTime(String[] command) {
        if (!ErrorManagement.command(command, 2,2)){
            return;
        }
        String time = command[1];
        if (ErrorManagement.isTime(time)){
            systemCenter.time = convertDate(time);
        }
    }

    /**
     * Sets the time to the desired time.
     * @param command
     */
    public static void setTime(String[] command) {
        if (!ErrorManagement.command(command, 2,2)){
            return;
        }
        String time = command[1];
        if (ErrorManagement.isTime(time) && !ErrorManagement.isBeforeTime(convertDate(time)) && !ErrorManagement.sameTime(convertDate(time))){
            systemCenter.time = convertDate(time);
        }
    }

    /**
     * Advances the time by the minute value in the incoming command.
     * @param command
     */
    public static void skipMinutes(String[] command) {
        if (!ErrorManagement.command(command, 2,2)){
            return;
        }
        String minute = command[1];
        if (ErrorManagement.isNumeric(minute) && !ErrorManagement.isBeforeTime(systemCenter.time.plusMinutes(Integer.parseInt(minute)))
                && !ErrorManagement.zeroMinutes(minute) && ErrorManagement.isPositive(minute, "Minute") && ErrorManagement.isInteger(minute, "Minute")){
            systemCenter.time = systemCenter.time.plusMinutes(Integer.parseInt(minute));
        }
    }

    /**
     * If any of the devices has a switch time, it advances the time to the time of the nearest switch time.
     */
    public static void nop() {
        if (ErrorManagement.isExistSwitchTime()){
            systemCenter.time = systemCenter.switchTimes.get(0);
        }
    }

    /**
     * Sets a time for a device to turn off if it's on, or turn on if it's off.
     * @param command
     */
    public static void setSwitchTime(String[] command) {
        if (!ErrorManagement.command(command, 3,3)){
            return;
        }
        String name = command[1];
        String time = command[2];
        if (ErrorManagement.isTime(time) && ErrorManagement.isExist(name) && !ErrorManagement.sameSwitchTime(name,time)
                && !ErrorManagement.pastSwitchTime(convertDate(time))) {
            if (systemCenter.devicesNull.containsKey(name)){
                systemCenter.devicesNull.get(name).switchTime = convertDate(time);
                addDevicesSwitchTimes(name, systemCenter.devicesNull.get(name));
                systemCenter.devicesNull.remove(name);
            }else{
                systemCenter.switchTimes.remove(systemCenter.devicesSwitchTime.get(name).switchTime);
                systemCenter.devicesSwitchTime.get(name).switchTime = convertDate(time);
            }
            systemCenter.switchTimes.add(convertDate(time));
            Collections.sort(systemCenter.switchTimes);

        }
    }

    /**
     * It enables a device to turn off if it is currently open, and to open if it is closed.
     * @param command
     */
    public static void switching(String[] command) {
        if (!ErrorManagement.command(command, 3,3)){
            return;
        }
        String name = command[1];
        String status = command[2];
        if (ErrorManagement.isExist(name) && ErrorManagement.sameStatus(name, status)){
            SmartDevice smartDevice = whichDataBase(name).get(name);
            if (smartDevice instanceof SmartPlug){
                PlugManagement.isPluggedControl((SmartPlug) smartDevice);
            }else if(smartDevice instanceof SmartCamera){
                CameraManagement.updateCamera((SmartCamera) smartDevice);
            }
            whichDataBase(name).get(name).status = status;
            if (smartDevice.switchTime != null){
                systemCenter.switchTimes.remove(smartDevice.switchTime);
                smartDevice.switchTime = null;
                systemCenter.devicesSwitchTime.remove(smartDevice.name);
                devicesNullPutFirst(smartDevice.name, smartDevice);
            }

        }
    }

    /**
     * It writes all the information of existing devices in a report.
     */
    public static void zReport() {
        String second = systemCenter.time.getSecond() == 0 ? ":00" : "";
        Output.writeFile("Time is:\t" + stringTime(systemCenter.time) + second);

        for (SmartDevice smartDevice : systemCenter.devicesSwitchTime.values()){
            Output.smartDeviceSentence(smartDevice);
        }
        for (SmartDevice smartDevice : systemCenter.devicesNull.values()){
            Output.smartDeviceSentence(smartDevice);
        }
    }

    /**
     * It is necessary to pay attention to the sorting criteria when printing the report of the devices. Two data structures were used for this.
     * @param name
     * @return which of data structure we should use.
     * @see SystemCenter#devicesSwitchTime
     * @see SystemCenter#devicesNull
     */
    public static LinkedHashMap<String, SmartDevice> whichDataBase(String name) {
        LinkedHashMap <String, SmartDevice> dataStructure = null;
        if (systemCenter.devicesNull.containsKey(name)) {
            dataStructure = systemCenter.devicesNull;
        } else if (systemCenter.devicesSwitchTime.containsKey(name)) {
            dataStructure = systemCenter.devicesSwitchTime;
        }
        return dataStructure;
    }

    /**
     * When the time passes, it applies the necessary switch operations to the devices with switch time in between.
     * (also the necessary calculations for the camera and plug)
     */
    public static void finishSwitchTime() {
        List<LocalDateTime> switchTimes = new ArrayList<>(systemCenter.switchTimes);
        ArrayList<SmartDevice> devicesSwitchTime = new ArrayList<>(systemCenter.devicesSwitchTime.values());
        Collections.reverse(devicesSwitchTime);

        for (LocalDateTime time : switchTimes) {
            for (SmartDevice smartDevice : devicesSwitchTime) {
                if (smartDevice.switchTime == null){
                }else if (smartDevice.switchTime.equals(time) && !smartDevice.switchTime.isAfter(systemCenter.time)){
                    if (smartDevice instanceof SmartPlug){
                        PlugManagement.isPluggedControl((SmartPlug) smartDevice);
                    }else if(smartDevice instanceof SmartCamera){
                        CameraManagement.updateCamera((SmartCamera) smartDevice);
                    }
                    smartDevice.status = smartDevice.status.equals("Off") ? "On" : "Off";
                    smartDevice.switchTime = null;
                    systemCenter.devicesSwitchTime.remove(smartDevice.name);
                    devicesNullPutFirst(smartDevice.name, smartDevice);
                    systemCenter.switchTimes.remove(time);
                }
            }
        }
    }

    /**
     * Sorts devices according to their switch times from early to late.
     */
    public static void sortSwitchTimes() {
        ArrayList<SmartDevice> deviceList = new ArrayList<>(systemCenter.devicesSwitchTime.values());
        Collections.sort(deviceList);
        systemCenter.devicesSwitchTime.clear();

        for (SmartDevice smartDevice : deviceList) {
            systemCenter.devicesSwitchTime.put(smartDevice.name, smartDevice);
        }
    }

    /**
     * When adding devices to devicesnull, it implements a necessary algorithm for us to add them to the first order.
     * @see Management#devicesNullPutFirst(String, SmartDevice) 
     */
    public static void reverseHashMap() {
        ArrayList<String> keyList = new ArrayList<>(systemCenter.devicesNull.keySet());
        Collections.reverse(keyList);

        ArrayList<SmartDevice> valueList = new ArrayList<>(systemCenter.devicesNull.values());
        Collections.reverse(valueList);

        systemCenter.devicesNull.clear();
        for (int i = 0; i < keyList.size(); i++) {
            systemCenter.devicesNull.put(keyList.get(i), valueList.get(i));
        }
    }

    /**
     * According to the sorting criteria, a device that has run out of switch time should come to the top of the devices that do not have a switch time.
     * For this, it adds to the beginning of devicenull.
     * @param name
     * @param smartDevice
     * @see Management#reverseHashMap()
     */
    public static void devicesNullPutFirst(String name, SmartDevice smartDevice) {
        reverseHashMap();
        systemCenter.devicesNull.put(name, smartDevice);
        reverseHashMap();
    }

    /**
     * Adds devices with switch time set to devicesSwitchTime.
     * @param name
     * @param smartDevice
     */
    public static void addDevicesSwitchTimes(String name, SmartDevice smartDevice) {
        ArrayList<String> keyList = new ArrayList<>(systemCenter.devicesSwitchTime.keySet());
        ArrayList<SmartDevice> valueList = new ArrayList<>(systemCenter.devicesSwitchTime.values());

        int index = keyList.size();
        for (SmartDevice smartDevice1 : valueList) {
            if (smartDevice1.switchTime.equals(smartDevice.switchTime)) {
                index = valueList.indexOf(smartDevice1) + 1;
            }
        }

        keyList.add(index, name);
        valueList.add(index, smartDevice);

        for (int i = 0; i < keyList.size(); i++) {
            systemCenter.devicesSwitchTime.put(keyList.get(i), valueList.get(i));
        }
    }

    /**
     * @param date Dates as string
     * @return LocalDateTime type of date
     */
    public static LocalDateTime convertDate(String date){
        String month = date.substring(date.indexOf("-")+1, date.lastIndexOf("-")).length() == 1 ? "M" : "MM";
        String day = date.substring(date.lastIndexOf("-")+1, date.indexOf("_")).length() == 1 ? "d" : "dd";
        String hour = date.substring(date.indexOf("_")+1, date.indexOf(":")).length() == 1 ? "H" : "HH";
        String minute = date.substring(date.indexOf(":")+1, date.lastIndexOf(":")).length() == 1 ? "m" : "mm";
        String second = date.substring(date.lastIndexOf(":")+1).length() == 1 ? "s" : "ss";
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern(String.format("yyyy-%s-%s_%s:%s:%s", month, day, hour, minute, second))
                .toFormatter();
        return LocalDateTime.parse(date, formatter);
    }

    /**
     * @param time
     * @return Smooth display of dates in output
     */
    public static String stringTime(LocalDateTime time){
        return String.valueOf(time).replace("T","_");
    }
}