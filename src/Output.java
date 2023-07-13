import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class Output {

    static String outputTxt;
    static {
        outputTxt = Main.outputTxt;
    }

    Output(String outputTxt){
        this.outputTxt = outputTxt;
        try {new BufferedWriter(new FileWriter(outputTxt, false));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * It prints the desired text to the output file.
     * @param text Text to be written to output file
     */
    public static void writeFile(String text) {
        try {BufferedWriter writer = new BufferedWriter(new FileWriter(outputTxt, true));
            writer.write(text + "\n");
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * When the information of the devices will be printed, it calls the required sentence according to the device type.
     * @param smartDevice
     */
    public static void smartDeviceSentence(SmartDevice smartDevice){
        if(smartDevice instanceof SmartPlug){
            Output.smartPlugSentence((SmartPlug) smartDevice);
        }else if (smartDevice instanceof SmartCamera){
            Output.smartCameraSentence((SmartCamera) smartDevice);
        }else if (smartDevice instanceof SmartLampWhite){
            Output.smartWhiteLampSentence((SmartLampWhite) smartDevice);
        }else if(smartDevice instanceof SmartLampColor){
            Output.smartColorLampSentence((SmartLampColor) smartDevice);
        }
    }

    /**
     * Plug-specific info clause
     * @param smartPlug
     */
    public static void smartPlugSentence(SmartPlug smartPlug){
        String second = smartPlug.switchTime != null && smartPlug.switchTime.getSecond() == 0 ? ":00" : "";
        writeFile("Smart Plug " + smartPlug.name + " is o" + smartPlug.status.substring(1) + " and consumed " +
                String.format("%.2f", smartPlug.consumption).replace(".", ",") + "W so far (excluding current device),"
                + " and its time to switch its status is " + Management.stringTime(smartPlug.switchTime) + second + ".");
    }

    /**
     * Camera-specific info clause
     * @param smartCamera
     */
    public static void smartCameraSentence(SmartCamera smartCamera){
        String second = smartCamera.switchTime != null && smartCamera.switchTime.getSecond() == 0 ? ":00" : "" ;
            writeFile("Smart Camera " + smartCamera.name + " is o" + smartCamera.status.substring(1) + " and used "
                    + String.format("%.2f", smartCamera.storage).replace(".", ",")
                    + " MB of storage so far (excluding current status), and its time to switch its status is "
                            + Management.stringTime(smartCamera.switchTime) + second + ".");
    }

    /**
     * White Lamp-specific info clause
     * @param smartLambWhite
     */
    public static void smartWhiteLampSentence(SmartLampWhite smartLambWhite){
        String second = smartLambWhite.switchTime != null && smartLambWhite.switchTime.getSecond() == 0 ? ":00" : "" ;
        int kelvin = (int) smartLambWhite.kelvin;
        if (smartLambWhite.kelvin % 1 == 0){
            kelvin = (int) smartLambWhite.kelvin;
        }
        writeFile("Smart Lamp " + smartLambWhite.name + " is o" + smartLambWhite.status.substring(1)
                    + " and its kelvin value is " + kelvin + "K with " + smartLambWhite.brightness
                    + "% brightness, and its time to switch its status is " + Management.stringTime(smartLambWhite.switchTime) + second
                    + ".");
    }

    /**
     * Color Lamp-specific info clause
     * @param smartLambColor
     */
    public static void smartColorLampSentence(SmartLampColor smartLambColor){
        String second = smartLambColor.switchTime != null && smartLambColor.switchTime.getSecond() == 0 ? ":00" : "" ;
        if (smartLambColor.colorMode){
            String colorCode = smartLambColor.colorCode;
            writeFile("Smart Color Lamp " + smartLambColor.name + " is o" + smartLambColor.status.substring(1)
                    + " and its color value is " + colorCode + " with " + smartLambColor.brightness
                    + "% brightness, and its time to switch its status is " + Management.stringTime(smartLambColor.switchTime) + second
                    + ".");
        }else{
            int kelvin = (int) smartLambColor.kelvin;
            if (smartLambColor.kelvin % 1 == 0){
                kelvin = (int) smartLambColor.kelvin;
            }
            writeFile("Smart Color Lamp " + smartLambColor.name + " is o" + smartLambColor.status.substring(1)
                    + " and its color value is " + kelvin + "K with " + smartLambColor.brightness
                    + "% brightness, and its time to switch its status is " + Management.stringTime(smartLambColor.switchTime) + second + ".");
        }
    }

    /**
     * When a device is to be deleted, the command to be written prints the text successful.
     */
    public static void removeOutput(){
        Output.writeFile("SUCCESS: Information about removed smart device is as follows:");
    }
}