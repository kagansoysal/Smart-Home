public class LampManagement extends ErrorManagement {
    /**
     * Assigns a new kelvin and brightness value to a lamp. If it is a color lamp, it turns off the color mode.
     * @param commands
     */
    public static void setWhite(String[] commands) {
        if (!ErrorManagement.command(commands, 4,4)){
            return;
        }
        String name = commands[1];
        String kelvin = commands[2];
        String brightness = commands[3];

        if (isNumeric(kelvin) && isNumeric(brightness) && isExist(name) && isLamb(name) && isDouble(kelvin, "Kelvin")
                && isInteger(brightness, "Brightness") && kelvinRange(kelvin) && brightnessRange(brightness)
                && !sameKelvin(name, kelvin) && !sameBrightness(name, brightness)){
            SmartLamp smartLamb = (SmartLamp) whichDataBase(name).get(name);
            smartLamb.kelvin = Double.parseDouble(kelvin);
            smartLamb.brightness = Integer.parseInt(brightness);

            if (smartLamb instanceof SmartLampColor) {
                SmartLampColor smartLampColor = (SmartLampColor) smartLamb;
                smartLampColor.colorMode = false;
                whichDataBase(name).replace(name, smartLampColor);
                return;
            }
            whichDataBase(name).replace(name, smartLamb);
        }
    }

    /**
     * Assigns a new kelvin value to a lamp. If it is a color lamp, it turns off the color mode.
     * @param commands
     */
    public static void setKelvin(String[] commands) {
        if (!command(commands, 3, 3)){
            return;
        }
        String name = commands[1];
        String kelvin = commands[2];

        if (isNumeric(kelvin) && isExist(name) && isLamb(name) && isDouble(kelvin, "Kelvin") && kelvinRange(kelvin)
                && !sameKelvin(name, kelvin)){
            SmartLamp smartLamb = (SmartLamp) whichDataBase(name).get(name);
            smartLamb.kelvin = Integer.parseInt(kelvin);
            if (smartLamb instanceof SmartLampColor) {
                SmartLampColor smartLampColor = (SmartLampColor) smartLamb;
                smartLampColor.colorMode = false;
                whichDataBase(name).replace(name, smartLampColor);
                return;
            }
            whichDataBase(name).replace(name, smartLamb);
        }
    }

    /**
     * Assigns brightness value to a lamp.
     * @param commands
     */
    public static void setBrightness(String[] commands) {
        if (!command(commands, 3, 3)){
            return;
        }
        String name = commands[1];
        String brightness = commands[2];
        if (isNumeric(brightness) && isExist(name) && isLamb(name) && isInteger(brightness, "Brightness")
                && brightnessRange(brightness) && sameBrightness(name, brightness)){
            SmartLamp smartLamb = (SmartLamp) whichDataBase(name).get(name);
            smartLamb.brightness = Integer.parseInt(brightness);
            whichDataBase(name).replace(name, smartLamb);
        }
    }
}