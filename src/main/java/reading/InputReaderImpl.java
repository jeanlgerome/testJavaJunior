package reading;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputReaderImpl implements InputReader {

    private static Logger log = Logger.getLogger(InputReader.class.getName());
    private JSONParser parser = new JSONParser();
    private String filePath;

    public InputReaderImpl(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @throws IOException    If something fails during reading json file.
     * @throws ParseException If something fails during parsing json file.
     */
    @Override
    public JSONObject readInput() throws IOException, ParseException {
        JSONObject inputJson;
        try (FileInputStream fis = new FileInputStream(filePath);
             BufferedReader bufferedReader =
                     new BufferedReader(new InputStreamReader(fis, "windows-1251"))
        ) {
            Object obj = parser.parse(bufferedReader);
            inputJson = (JSONObject) obj;
            log.log(Level.FINEST, " input.json has been successfully read ");
        } catch (IOException e) {
            log.log(Level.SEVERE, "IOException in readInput", e);
            throw e;
        } catch (ParseException e) {
            log.log(Level.SEVERE, " Exception during input.json parsing", e);
            throw e;
        }
        return inputJson;
    }

}
