package reading;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface InputReader {

    JSONObject readInput() throws IOException, ParseException;

}
