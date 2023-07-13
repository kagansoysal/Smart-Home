/**
 * In this class, smart color lamp-specific operations are performed.
 */
public class ColorLampManagement extends ErrorManagement {
    /**
     * If the entered command is in the correct format, the smart color lamp is added to the system.
     * @param commands
     */
    public static void add(String[] commands){
        if (!command(commands, 3, 6)){
            return;
        }
        String name = commands[2];
        String status = commands.length >= 4 ? commands[3] : "Off";
        String kelvin = commands.length >= 5 && !commands[4].contains("x")? commands[4] : "4000";
        String colorCode = commands.length >= 5 && commands[4].contains("x")? commands[4] : "0x0000000";
        String brightness = commands.length >= 6 ? commands[5] : "100";

        if (!invalidStatus(status) && isNotExist(name) && isDouble(kelvin, "Kelvin")
                && isColorCode(colorCode) && isInteger(brightness, "Brightness") && kelvinRange(kelvin)
                && colorCodeRange(colorCode) && brightnessRange(brightness)){
            systemCenter.devicesNull.put(name, new SmartLampColor(name, status, kelvin, colorCode, brightness));
        }
    }

    /**
     * A color code is defined to the color lamp and the color mode of the lamp is switched on.
     * @param commands
     */
    public static void setColorCode(String[] commands){
        if (!ErrorManagement.command(commands, 3,3)){
            return;
        }
        String name = commands[1];
        String colorCode = commands[2];
        if (isExist(name) && isColorLamb(name) && isColorCode(colorCode)
                && colorCodeRange(colorCode) && !sameColorCode(name, colorCode)){
        SmartLampColor smartLambColor = (SmartLampColor) whichDataBase(name).get(name);
        smartLambColor.colorCode = colorCode;
        smartLambColor.colorMode = true;
        whichDataBase(name).replace(name, smartLambColor);
        }
    }

    /**
     * A color code and brightness are defined to the color lamp and the color mode of the lamp is switched on.
     * @param commands
     */
    public static void setColor(String[] commands){
        if (!ErrorManagement.command(commands, 4,4)){
            return;
        }
        String name = commands[1];
        String colorCode = commands[2];
        String brightness = commands[3];
        if (isExist(name) && isColorLamb(name) && isColorCode(colorCode)
                && isInteger(brightness, "Brightness") && colorCodeRange(colorCode) && brightnessRange(brightness)){
        SmartLampColor smartLambColor = (SmartLampColor) whichDataBase(name).get(name);
        smartLambColor.colorCode = colorCode;
        smartLambColor.brightness = Integer.parseInt(brightness);
        smartLambColor.colorMode = true;
        whichDataBase(name).replace(name, smartLambColor);
        }
    }
}