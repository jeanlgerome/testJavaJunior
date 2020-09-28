import dao.Dao;
import dao.DaoFactory;
import dao.DaoImpl;
import jsonHandler.JsonHandler;
import jsonHandler.JsonHandlerImpl;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import reading.InputReader;
import reading.InputReaderImpl;
import utils.ArgsChecker;
import utils.Constants;
import utils.LoggerUtil;
import utils.ProgramPath;
import writing.OutputWriter;
import writing.OutputWriterImpl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    private static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        final String programPath = ProgramPath.getPath();
        LoggerUtil loggerUtil = new LoggerUtil();
        loggerUtil.init(programPath);

        if (ArgsChecker.verifyCmdArgs(args)) {
            final String inputJsonName = args[1];
            final String outputJsonName = args[2];
            OutputWriter outputWriter = new OutputWriterImpl(programPath + "//json//" + outputJsonName);
            final String operationType = args[0];
            InputReader inputReader = new InputReaderImpl(programPath + "//json//" + inputJsonName);
            JSONObject inputJson;
            try {
                inputJson = inputReader.readInput();
                try {
                    DaoFactory daoFactory = new DaoFactory(programPath);
                    Dao dao = new DaoImpl(daoFactory);
                    JsonHandler jsonHandler = new JsonHandlerImpl(dao);
                    JSONObject outputJson = new JSONObject();

                    switch (operationType) {
                        case Constants.SEARCH_TYPE:
                            outputJson = jsonHandler.createSearchOutput(inputJson);
                            break;
                        case Constants.STAT_TYPE:
                            outputJson = jsonHandler.createStatOutput(inputJson);
                            break;
                    }
                    outputWriter.writeOutputJson(outputJson);
                    log.log(Level.FINEST, "Process finished");
                    log.info("Process finished");
                } catch (IOException e) {
                    outputWriter.writeOutputJson(JsonHandlerImpl.
                            createErrorOutput("Ошибка установки properties базы данных"));
                } catch (ClassNotFoundException e) {
                    outputWriter.writeOutputJson(JsonHandlerImpl.
                            createErrorOutput("Не найден драйвер базы данных"));
                }
            } catch (IOException e) {
                outputWriter.writeOutputJson(JsonHandlerImpl.
                        createErrorOutput("Не удалось прочитатать входной json файл"));
            } catch (ParseException e) {
                outputWriter.writeOutputJson(JsonHandlerImpl.
                        createErrorOutput("Некорректный входной json файл. Ошибка парсинга"));
            }
        } else {
            OutputWriter outputWriter = new OutputWriterImpl(programPath + "//json//output.json");
            outputWriter.writeOutputJson(JsonHandlerImpl.
                    createErrorOutput("Некорректные аргументы командной строки"));
        }
        System.out.println("-/-/-/-/- Process finished -/-/-/-/-/-/");
    }

}
