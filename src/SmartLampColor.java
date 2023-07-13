public class SmartLampColor extends SmartLamp {
    String colorCode;
    boolean colorMode;

    SmartLampColor(String name, String status, String kelvin, String colorCode, String brigthness){
        this.name = name;
        this.status = status;
        this.brightness = Integer.parseInt(brigthness);
        if (!colorCode.equals("0x0000000")){
            this.colorCode = colorCode;
            this.colorMode = true;
        }else{
            this.kelvin = Double.parseDouble(kelvin);
            this.colorMode = false;
        }
    }
}