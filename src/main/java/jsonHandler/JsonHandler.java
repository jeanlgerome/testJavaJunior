package jsonHandler;

import org.json.simple.JSONObject;

public interface JsonHandler {

    JSONObject createSearchOutput(JSONObject jsonInput);

    JSONObject createStatOutput(JSONObject jsonInput);

}
