public class SmartPlug extends SmartDevice{
    double consumption;
    double ampere;
    String isPlugged = "Off";
    int voltage = 220;

    SmartPlug(String name, String status, String ampere){
        this.name = name;
        this.status = status;
        this.ampere = Double.parseDouble(ampere);
    }
}