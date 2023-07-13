import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

public class ErrorManagement extends Management {

    /**
     * It checks if commands are unnecessarily long or short.
     * @param commands
     * @param begin The shortest possible length of the command
     * @param end The longest the command can be
     * @return whether the command is in the range it can be in length
     */
    public static boolean command(String[] commands, int begin, int end){
        if (commands.length >= begin && commands.length <= end){
            return true;
        }else {
            Output.writeFile("ERROR: Erroneous command!");
            return false;
        }
    }

    /**
     * It checks whether the status to be set is anything other than on or off.
     * @param status writen status
     * @return whether it is valid
     */
    public static boolean invalidStatus(String status){
        if (status.equals("On") || status.equals("Off")){
            return false;
        }else {
            Output.writeFile("ERROR: Erroneous command!");
            return true;
        }
    }

    /**
     * Checks if the entered value is a number.
     * @param value
     * @return whether it is valid
     */
    public static boolean isNumeric(String value) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?"); // Regex for numeric values
        if (pattern.matcher(value).matches()){
            return true;
        }else {
            Output.writeFile("ERROR: Erroneous command!");
            return false;
        }
    }

    /**
     * Checks if the entered number value is positive.
     * @param value
     * @param variable to be written if it is wrong
     * @return whether it is valid
     */
    public static boolean isPositive(String value, String variable) {
        if (Double.parseDouble(value) > 0) {
            return true;
        } else {
            Output.writeFile("ERROR: " + variable + " value must be a positive number!");
            return false;
        }
    }

    /**
     * Checks whether the entered number value is an integer.
     * @param value
     * @param variable to be written if it is wrong
     * @return whether it is valid
     */
    public static boolean isInteger(String value, String variable) {
        if (value.matches("\\d+")) {
            return true;
        }else {
            Output.writeFile("ERROR: " + variable + " value must be integer!");
            return false;
        }
    }

    /**
     * Checks if the entered value is double.
     * @param value
     * @param variable to be written if it is wrong
     * @return whether it is valid
     */
    public static boolean isDouble(String value, String variable) {
        if (value.matches("-?\\d+(\\.\\d+)?")) {
            return true;
        }else {
            Output.writeFile("ERROR: " + variable + " value must be decimal!");
            return false;
        }
    }

    /**
     * It checks whether the entered color code value is hexadecimal.
     * @param value
     * @return whether it is valid
     */
    public static boolean isColorCode(String value){
        if (value.matches("^0x[0-9A-Fa-f]+$")) {
            return true;
        } else {
            Output.writeFile("ERROR: Erroneous command!");
            return false;
        }
    }

    /**
     * It checks whether the device name in the commands is in the system.
     * @param name of device
     * @return whether it is valid
     */
    public static boolean isExist(String name){ //YAZISI YAZILCAK.
        if(systemCenter.devicesSwitchTime.containsKey(name) || systemCenter.devicesNull.containsKey(name)){
            return true;
        }else {
            Output.writeFile("ERROR: There is not such a device!");
            return false;
        }
    }

    /**
     * It checks whether the device name in the commands is in the system
     * @param name of device
     * @return whether it is valid
     */
    public static boolean isNotExist(String name){
        if(systemCenter.devicesSwitchTime.containsKey(name) || systemCenter.devicesNull.containsKey(name)){
            Output.writeFile("ERROR: There is already a smart device with same name!");
            return false;
        }else {
            return true;
        }
    }

    /**
     * It is checked whether the entered time value is suitable for the time format.
     * @param value
     * @return whether it is valid
     */
    public static boolean isTime(String value){
        try {
            convertDate(value);
            return true;
        }catch (Exception e){
            Output.writeFile("ERROR: Time format is not correct!");
            return false;
        }
    }

    /**
     * It is checked whether the device in the command is a plug.
     * @param name of device
     * @return whether it is
     */
    public static boolean isPlug(String name){
        if (whichDataBase(name).get(name).getClass().getName().equals("SmartPlug")) {
            return true;
        } else {
            Output.writeFile("ERROR: This device is not a smart plug!");
            return false;
        }
    }

    /**
     * It is checked whether the device in the command is a lamp.
     * @param name of device
     * @return whether it is
     */
    public static boolean isLamb(String name){
        if (whichDataBase(name).get(name).getClass().getSuperclass().getName().equals("SmartLamp")) {
            return true;
        } else {
            Output.writeFile("ERROR: This device is not a smart lamp!");
            return false;
        }
    }

    /**
     * It is checked whether the device in the command is a color lamp.
     * @param name of device
     * @return whether it is
     */
    public static boolean isColorLamb(String name){
        if (whichDataBase(name).get(name).getClass().getName().equals("SmartLampColor")) {
            return true;
        } else {
            Output.writeFile("ERROR: This device is not a smart color lamp!");
            return false;
        }
    }

    /**
     * It is checked whether the entered time value is before the current time.
     * @param time
     * @return whether it is
     */
    public static boolean isBeforeTime(LocalDateTime time){
        if (time.isBefore(systemCenter.time)){
            Output.writeFile("ERROR: Time cannot be reversed!");
            return true;
        } else {
            return false;
        }
    }

    /**
     * For the nop command, it is checked whether any device has a switch time value.
     * @return whether it is
     */
    public static boolean isExistSwitchTime(){
        if (systemCenter.switchTimes.size() != 0){
            return true;
        } else {
            Output.writeFile("ERROR: There is nothing to switch!");
            return false;
        }
    }

    /**
     * It is checked whether the entered temperature value is between 2000K and 6500K.
     * @param kelvin
     * @return whether it is
     */
    public static boolean kelvinRange(String kelvin) {
        if (Integer.parseInt(kelvin) >= 2000 && Integer.parseInt(kelvin) <= 6500) {
            return true;
        } else {
            Output.writeFile("ERROR: Kelvin value must be in range of 2000K-6500K!");
            return false;
        }
    }

    /**
     * It is checked whether the entered brightness value is between %0 and %100.
     * @param brightness
     * @return whether it is
     */
    public static boolean brightnessRange(String brightness){
        if (Integer.parseInt(brightness) >= 0 && Integer.parseInt(brightness) <= 100) {
            return true;
        } else {
            Output.writeFile("ERROR: Brightness must be in range of 0%-100%!");
            return false;
        }
    }

    /**
     * It is checked whether the entered color code value is between 0 and FFFFFF.
     * @param colorCode
     * @return
     */
    public static boolean colorCodeRange(String colorCode){
        if (Integer.parseInt(colorCode.substring(2), 16) >= 0 && Integer.parseInt(colorCode.substring(2), 16) <= 16777215) {
            return true;
        } else {
            Output.writeFile("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
            return false;
        }
    }

    /**
     * When the name of a device is requested to be changed, it is checked whether the new name is the same as the existing name.
     * @param oldName
     * @param newName
     * @return whether they are same or not.
     */
    public static boolean sameName(String oldName, String newName){
        if (oldName.equals(newName)) {
            Output.writeFile("ERROR: Both of the names are the same, nothing changed!");
            return true;
        } else {
            return false;
        }
    }

    /**
     * It checks whether the status value entered on a device is already that status.
     * @param name of device
     * @param status
     * @return whether it is
     */
    public static boolean sameStatus(String name, String status){
        if (!whichDataBase(name).get(name).status.equals(status)) {
            return true;
        } else {
            Output.writeFile("ERROR: This device is already switched o" + status.substring(1) + "!");
            return false;
        }
    }

    /**
     * It checks whether the time to be adjusted is already the current time.
     * @param time
     * @return whether it is
     */
    public static boolean sameTime(LocalDateTime time){
        if (time.isEqual(systemCenter.time)){
            Output.writeFile("ERROR: There is nothing to change!");
            return true;
        } else {
            return false;
        }
    }

    /**
     * It checks whether the switch time to be set to a device is already that time.
     * @param name of device
     * @param time switch
     * @return whether it is
     */
    public static boolean sameSwitchTime(String name, String time){
        if (whichDataBase(name).get(name).switchTime != null
                &&whichDataBase(name).get(name).switchTime.isEqual(convertDate(time))){
            Output.writeFile("ERROR: The switch time of this device is already this time!");
            return true;
        }else{
            return false;
        }
    }

    /**
     * It checks whether the temperature value desired to be set on a device is already that value.
     * @param name of device
     * @param kelvin
     * @return whether it is
     */
    public static boolean sameKelvin(String name, String kelvin){
        SmartLamp smartLamp = (SmartLamp) whichDataBase(name).get(name);
        if (smartLamp.kelvin == Double.parseDouble(kelvin)){
            Output.writeFile("ERROR: The kelvin of this device is already this kelvin!");
            return true;
        }else {
            return false;
        }
    }

    /**
     * It checks whether the brightness value desired to be set on a device is already that value.
     * @param name of device
     * @param brightness
     * @return whether it is
     */
    public static boolean sameBrightness(String name, String brightness){
        SmartLamp smartLamp = (SmartLamp) whichDataBase(name).get(name);
        if (smartLamp.brightness == Integer.parseInt(brightness)){
            Output.writeFile("ERROR: The brightness of this device is already this brightness!");
            return true;
        }else {
            return false;
        }
    }

    /**
     * It checks whether the color code value desired to be set on a device is already that value.
     * @param name of device
     * @param colorCode
     * @return whether it is
     */
    public static boolean sameColorCode(String name, String colorCode){
        SmartLampColor smartLampColor = (SmartLampColor) whichDataBase(name).get(name);
        if (Objects.equals(smartLampColor.colorCode, colorCode)){
            Output.writeFile("ERROR: The color code of this device is already this color code!");
            return true;
        }else {
            return false;
        }
    }

    /**
     * Checks whether a device that is requested to be plugged in is already plugged in.
     * @param name of device
     * @return whether it is
     */
    public static boolean alreadyPlugIn(String name) {
        SmartPlug smartPlug = (SmartPlug) whichDataBase(name).get(name);
        if (smartPlug.isPlugged.equals("On")) {
            Output.writeFile("ERROR: There is already an item plugged in to that plug!");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a device to be unplugged is already plugged out
     * @param name of device
     * @return whether it is
     */
    public static boolean alreadyPlugOut(String name){
        SmartPlug smartPlug = (SmartPlug) whichDataBase(name).get(name);
        if (smartPlug.isPlugged.equals("Off")) {
            Output.writeFile("ERROR: This plug has no item to plug out from that plug!");
            return true;
        } else {
            return false;
        }
    }

    /**
     * It checks whether the desired minute value is 0 or not.
     * @param minute
     * @return whether it is
     */
    public static boolean zeroMinutes(String minute){
        if (Integer.parseInt(minute) == 0){
            Output.writeFile("ERROR: There is nothing to skip!");
            return true;
        } else {
            return false;
        }
    }

    /**
     * It checks whether the switch time value to be adjusted is a time in the past.
     * @param time
     * @return
     */
    public  static boolean pastSwitchTime(LocalDateTime time){
        if (time.isBefore(systemCenter.time)){
            Output.writeFile("ERROR: Switch time cannot be in the past!");
            return true;
        } else {
            return false;
        }
    }
}