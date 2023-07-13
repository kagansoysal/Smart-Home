import java.lang.reflect.InvocationTargetException;
/**
 * @author Gazi Kağan Soysal - 2210356050
 * @version 1.0
 */
public class Main {
    static String outputTxt;
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String inputTxt = args[0];
        outputTxt = args[1];
        new Output(outputTxt);
        new Execute(inputTxt);
    }
}