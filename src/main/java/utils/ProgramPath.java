package utils;

import jsonHandler.JsonHandlerImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgramPath {

    private static Logger log = Logger.getLogger(JsonHandlerImpl.class.getName());
    // возвращает директорию, в которой находится jar, если что-то идет не так, то возвращает home директорию
    public static String getPath() {

        String programPath = null;
        String rawPath = ProgramPath.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        rawPath = rawPath.substring(0, rawPath.lastIndexOf("/") + 1);

        try {
            programPath = URLDecoder.decode(rawPath, "UTF-8");
            log.log(Level.FINEST, " homePath will be returned");
        } catch (UnsupportedEncodingException e) {
            log.log(Level.WARNING, " UnsupportedEncodingException during getPath");
        }

        if (programPath == null || programPath.isEmpty()) {
            programPath = System.getProperty("user.home");
            log.log(Level.FINE, "homePath will be returned");
        }
        return programPath;
    }
}
