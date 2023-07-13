public class WhiteLampManagement extends ErrorManagement{
    /**
     * If the entered command is in the correct format, it adds the white lamp to the system.
     * If kelvin and brightness values are not given, by default kelvin is set to 4000K and brightness is set to 100%.
     * @param commands
     */
    public static void add(String[] commands){
        if (!command(commands, 3,6)){
            return;
        }
        String name = commands[2];
        String status = commands.length >= 4 ? commands[3] : "Off";
        String kelvin = commands.length >= 5 ? commands[4] : "4000";
        String brightness = commands.length >= 6 ? commands[5] : "100";

        if (!invalidStatus(status) && isNotExist(name) && isDouble(kelvin, "Kelvin")
                && isInteger(brightness, "Brightness") && kelvinRange(kelvin) && brightnessRange(brightness)){
            systemCenter.devicesNull.put(name, new SmartLampWhite(name, status, kelvin, brightness));
        }
    }
}