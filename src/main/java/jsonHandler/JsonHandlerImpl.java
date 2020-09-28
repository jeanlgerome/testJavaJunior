package jsonHandler;

import dao.Dao;
import exceptions.WrongDateException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.ArgsChecker;
import utils.Constants;
import utils.WeekdaysCounter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

//класс обрабатывает входной json файл, создает выходной json
public class JsonHandlerImpl implements JsonHandler {

    private static Logger log = Logger.getLogger(JsonHandlerImpl.class.getName());
    private Dao dao;

    public JsonHandlerImpl(Dao dao) {
        this.dao = dao;
    }

    //достает из входного файла критерии, проверяет их и передает их в DaoImpl, из результата создает выходной json
    @Override
    public JSONObject createSearchOutput(JSONObject jsonInput) {
        JSONObject searchOutput = new JSONObject();
        JSONArray results = new JSONArray();
        JSONArray criterias = (JSONArray) jsonInput.get("criterias");

        if (criterias == null) {
            return createErrorOutput("Критерии не найдены");
        }
        for (Object elem : criterias) {
            JSONObject criteria = (JSONObject) elem;
            try {
                results.add(dao.searchByCriteria(criteria));
            } catch (SQLException e) {
                log.log(Level.WARNING, "SQLException occurred during OutputJson creation", e);
                log.log(Level.WARNING, "sql exception occurred during StatOutput creating", e);
                return createErrorOutput("Ошибка при чтении из базы данных");
            }
        }
        searchOutput.put("type", "search");
        searchOutput.put("results", results);
        log.log(Level.FINEST, "SearchOutput has been created");
        return searchOutput;
    }

    // -----------------------------
    // 4 метода ниже создают result на основе критерия и списка пользователей, полученного по критерию

    public static JSONObject createLastNameResult(String lastName, JSONArray customersJson) {
        JSONObject result = new JSONObject();
        JSONObject criteria = new JSONObject();
        criteria.put("lastName", lastName);
        result.put("criteria", criteria);
        result.put("results", customersJson);
        log.log(Level.FINEST, "LastName result has been created");
        return result;
    }

    public static JSONObject createProductTimeResult(String productName, long minTimes, JSONArray customersJson) {
        JSONObject result = new JSONObject();
        JSONObject criteria = new JSONObject();
        criteria.put("productName", productName);
        criteria.put("minTimes", minTimes);
        result.put("criteria", criteria);
        result.put("results", customersJson);
        log.log(Level.FINEST, "ProductTime result has been created");
        return result;
    }

    public static JSONObject createExpensesRangeResult(long minExpenses, long maxExpenses, JSONArray customersJson) {
        JSONObject result = new JSONObject();
        JSONObject criteria = new JSONObject();
        criteria.put("minExpenses", minExpenses);
        criteria.put("maxExpenses", maxExpenses);
        result.put("criteria", criteria);
        result.put("results", customersJson);
        log.log(Level.FINEST, " ExpensesRange result has been created");
        return result;
    }

    public static JSONObject createBadCustomersResult(long badCustomers, JSONArray customersJson) {
        JSONObject result = new JSONObject();
        JSONObject criteria = new JSONObject();
        criteria.put("badCustomers", badCustomers);
        result.put("criteria", criteria);
        result.put("results", customersJson);
        log.log(Level.FINEST, "BadCustomers result has been created");
        return result;
    }
    // ^^^^ 4 метода выше создают result на основе критерия и списка пользователей, полученного по критерию
    //--------------------------

    // создает json в случае ошибки
    public static JSONObject createErrorOutput(String errorMessage) {
        JSONObject errorOutput = new JSONObject();
        errorOutput.put("type", "error");
        errorOutput.put("message", errorMessage);
        return errorOutput;
    }

    // достает из входного json даты, проверяет их. Получает на основе дат список покупателей( их имена и айди),
    // упорядоченный по сумме, для каждого покупателя из списка получает статистику покупателя по покупкам и полную сумму
    // суммирует суммы, находит среднее, собирает всё в statOutput
    @Override
    public JSONObject createStatOutput(JSONObject jsonInput) {
        JSONObject statOutput = new JSONObject();
        JSONArray customersStat = new JSONArray();
        JSONArray customersInfo;
        LocalDate startDate;
        LocalDate endDate;
        double allCustomersTotalExpenses = 0;
        int numberOfCustomers = 0;

        if (jsonInput.get("startDate") == null || jsonInput.get("endDate") == null) {
            return createErrorOutput("Дата не найдена");
        }
        try {
            startDate = LocalDate.parse(jsonInput.get("startDate").toString(), Constants.FORMATTER);
            endDate = LocalDate.parse(jsonInput.get("endDate").toString(), Constants.FORMATTER);
        } catch (DateTimeParseException e) {
            log.log(Level.WARNING, "wrong input time format", e);
            return createErrorOutput("Неправильный формат даты");
        }
        try {
            ArgsChecker.verify(startDate, endDate);
        } catch (WrongDateException e) {
            log.log(Level.WARNING, " wrong date sequence", e);
            return createErrorOutput("startDate должна быть не позже endDate");
        }

        try {
            customersInfo = dao.findCustomersInfo(startDate, endDate);

            for (Object elem : customersInfo) {
                JSONObject customerInfo = (JSONObject) elem;
                JSONObject customerStat = new JSONObject();
                long customerId = (long) customerInfo.get("id");
                String customerName = customerInfo.get("lastName").toString() + " " + customerInfo.get("firstName").toString();
                JSONArray purchases = dao.findCustomerPurchases(customerId, startDate, endDate);
                double customerTotalExpenses = dao.findCustomerTotalExpenses(customerId, startDate, endDate);
                allCustomersTotalExpenses += customerTotalExpenses;
                numberOfCustomers++;
                customerStat.put("name", customerName);
                customerStat.put("purchases", purchases);
                customerStat.put("totalExpenses", customerTotalExpenses);
                customersStat.add(customerStat);
            }
            statOutput.put("type", "stat");
            statOutput.put("totalDays", WeekdaysCounter.countWeekdays(startDate, endDate));
            statOutput.put("customers", customersStat);
            statOutput.put("totalExpenses", allCustomersTotalExpenses);
            double avgExpenses = (numberOfCustomers == 0) ? 0 : allCustomersTotalExpenses / numberOfCustomers;
            statOutput.put("avgExpenses", avgExpenses);
            log.log(Level.FINEST, "statOutput has been created");
        } catch (SQLException e) {
            log.log(Level.WARNING, "sql exception occurred during StatOutput creating", e);
            return createErrorOutput("Ошибка при чтении из базы данных");
        }
        return statOutput;
    }

}
