import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>In this class, incoming commands are edited and it is checked whether these commands are in the correct format,
 * and if they are in the correct format, the relevant method of the relevant class is called.</p>
 */
public class Execute {
    Execute(String inputTxt) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Input input = new Input(inputTxt);

        ArrayList<String> commandList = new ArrayList<>(Arrays.asList("remove", "changeName", "setTime",
                "skipMinutes", "setSwitchTime", "switching"));
        ArrayList<String> commandList2 = new ArrayList<>(Arrays.asList("plugIn", "plugOut"));
        ArrayList<String> commandList3 = new ArrayList<>(Arrays.asList("setWhite", "setKelvin", "setBrightness"));
        ArrayList<String> commandList4 = new ArrayList<>(Arrays.asList("setColor", "setColorCode"));

        if (input.inputTxt.size() == 0){
            Output.writeFile("There are no commands!");
        }
        /**
         * Empty commands are deleted
         */
        input.inputTxt.removeIf(s -> s.trim().isEmpty());

        /**
         * As the first command, it is checked whether the time is set or not.
         * If not set, the remaining commands are not read
         */
        String[] firstCommand = input.inputTxt.get(0).split("\t");
        if (firstCommand[0].equals("SetInitialTime") && firstCommand.length == 2) {
            try {
                Management.convertDate(firstCommand[1]);
            }catch (Exception e){
                Output.writeFile("COMMAND: " + firstCommand[0] + "\t" + firstCommand[1]);
                Output.writeFile("ERROR: Format of the initial date is wrong! Program is going to terminate!");
                System.exit(0);
            }
            Management.setInitialTime(firstCommand);
            String second = Management.convertDate(firstCommand[1]).getSecond() == 0 ? ":00" : "";
            Output.writeFile("COMMAND: " + firstCommand[0] + "\t" + firstCommand[1]);
            Output.writeFile("SUCCESS: Time has been set to " + Management.stringTime(Management.convertDate(firstCommand[1])) + second + "!");
            input.inputTxt.remove(0);
        } else {
            Output.writeFile("COMMAND: " + input.inputTxt.get(0));
            Output.writeFile("ERROR: First command must be set initial time! Program is going to terminate!");
            System.exit(0);
        }

        for (String line : input.inputTxt){
            Output.writeFile("COMMAND: " + line);
            String[] commands = line.split("\t");
            String methodName = commands[0].equals("Switch") ? "switching" : commands[0].substring(0, 1).toLowerCase() + commands[0].substring(1);
            Class<?> clas;

            if (commandList.contains(methodName)) {
                clas = Management.class;
            } else if (commandList2.contains(methodName) || (methodName.equals("add") && commands[1].equals("SmartPlug"))) {
                    clas = PlugManagement.class;
            } else if (commandList3.contains(methodName)) {
                clas = LampManagement.class;
            } else if (methodName.equals("add") && commands[1].equals("SmartLamp")){
                clas = WhiteLampManagement.class;
            } else if (commandList4.contains(methodName) || (methodName.equals("add") && commands[1].equals("SmartColorLamp"))) {
                clas = ColorLampManagement.class;
            } else if (methodName.equals("add") && commands[1].equals("SmartCamera")){
                clas = CameraManagement.class;
            } else if (methodName.equals("zReport") && ErrorManagement.command(commands,1,1)){
                Management.zReport();
                continue;
            } else if (methodName.equals("nop") && ErrorManagement.command(commands,1,1)){
                Management.nop();
                Management.finishSwitchTime();
                Management.sortSwitchTimes();
                continue;
            }else{
                Output.writeFile("ERROR: Erroneous command!");
                continue;
            }
            Method method = clas.getMethod(methodName, String[].class);
            method.invoke(null, (Object) commands);
            Management.finishSwitchTime();
            Management.sortSwitchTimes();
        }
        /**
         * If the last command is not report printing, the last report is printed.
         */
        if (!input.inputTxt.get(input.inputTxt.size()-1).equals("ZReport")){
            Output.writeFile("ZReport:");
            Management.zReport();
        }
    }
}