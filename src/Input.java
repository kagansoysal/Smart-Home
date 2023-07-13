import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Input {
    ArrayList<String> inputTxt;

    Input(String inputTxt){
        this.inputTxt = readFile(inputTxt);
    }

    public static ArrayList<String> readFile(String path) {
        try {return new ArrayList<>(Files.readAllLines(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
