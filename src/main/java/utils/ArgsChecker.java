package utils;

import exceptions.NullArgException;
import exceptions.WrongDateException;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArgsChecker {

    private static Logger log = Logger.getLogger(LoggerUtil.class.getName());

    /**
     * Returns the given parameter if its not null and not empty
     *
     * @param arg The parameter values will be checked
     * @throws Exception If parameter didnt pass the verification
     */
    public static String verify(String arg) throws NullArgException {
        if (arg == null || arg.isEmpty()) {
            log.log(Level.WARNING, "empty or null String argument");
            throw new NullArgException("empty or null String argument");
        }
        return arg;
    }

    public static Long verify(Long arg) throws NullArgException {
        if (arg == null) {
            log.log(Level.WARNING, "empty or null Long argument");
            throw new NullArgException("null Long argument");
        }
        return arg;
    }

    public static void verify(LocalDate startDate, LocalDate endDate) throws WrongDateException {
        if (endDate.isBefore(startDate)) {
            log.log(Level.WARNING, "endDate should not be earlier than startDate");
            throw new WrongDateException(" endDate should not be earlier than startDate");
        }
    }

    public static boolean verifyCmdArgs(String[] args) {
        if (args.length != 3) {
            return false;
        }
        if (!args[0].equals("search") && !args[0].equals("stat")) {
            return false;
        }
        if (!(args[1].endsWith(".json") || args[2].endsWith(".json"))) {
            return false;
        }
        return true;
    }
}
