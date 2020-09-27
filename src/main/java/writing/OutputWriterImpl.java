package writing;

import org.json.simple.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutputWriterImpl implements OutputWriter {


    private static Logger log = Logger.getLogger(OutputWriterImpl.class.getName());
    private String filePath;

    public OutputWriterImpl(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void writeOutputJson(JSONObject outputJson) {
        try (OutputStreamWriter osw =
                     new OutputStreamWriter(new FileOutputStream(filePath), "utf-8")) {
            osw.write(outputJson.toJSONString());
            log.log(Level.FINEST, "file has been written successfully");
        } catch (IOException e) {
            log.log(Level.SEVERE, "exception during outputJson writing", e);
        }
    }
}
