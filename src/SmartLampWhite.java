public class SmartLampWhite extends SmartLamp {
    SmartLampWhite(String name, String status, String kelvin, String brigthness){
        this.name = name;
        this.status = status;
        this.kelvin = Double.parseDouble(kelvin);
        this.brightness = Integer.parseInt(brigthness);
    }
}