package dao;

import exceptions.NullArgException;
import jsonHandler.JsonHandlerImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.ArgsChecker;
import utils.Constants;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoImpl implements Dao {

    private static Logger log = Logger.getLogger(DaoImpl.class.getName());
    private DaoFactory daoFactory;

    public DaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // выбирает необходимый метод поиска в зависимости от критерия
    @Override
    public JSONObject searchByCriteria(JSONObject criteria) throws SQLException {
        JSONObject result = new JSONObject();

        for (Object key : criteria.keySet()) {
            String keyStr = key.toString();
            try {
                switch (keyStr) {
                    case "lastName":
                        result = searchByLastName(ArgsChecker.verify(criteria.get("lastName").toString()));
                        break;
                    case "productName":
                    case "minTimes":
                        result = searchByProductTimes(ArgsChecker.verify(criteria.get("productName").toString()),
                                ArgsChecker.verify((Long) criteria.get("minTimes")));
                        break;
                    case "minExpenses":
                    case "maxExpenses":
                        result = searchByExpensesRange(ArgsChecker.verify((Long) criteria.get("minExpenses")),
                                ArgsChecker.verify((Long) criteria.get("maxExpenses")));
                        break;
                    case "badCustomers":
                        result = searchBadCustomers(ArgsChecker.verify((Long) criteria.get("badCustomers")));
                        break;
                    default:
                        result.put("error", "Неизвестный критерий");
                }
                break;
            } catch (SQLException e) {
                log.log(Level.WARNING, "SQLException occurred during searchByCriteria search");
                throw e;
            } catch (NullArgException e) {
                log.log(Level.WARNING, "empty argument in inputJson criteria");
                JSONObject errorResult = new JSONObject();
                errorResult.put("error", "illegal criteria");
                return errorResult;
            }
        }
        return result;
    }

  //------------------------------------
    //ниже находятся 4 метода поиска для операции search
    private JSONObject searchByLastName(String lastName) throws SQLException {
        JSONObject result;
        JSONArray customersJson = getCustomers(Constants.SQL_LAST_NAME, lastName);
        result = JsonHandlerImpl.createLastNameResult(lastName, customersJson);
        return result;
    }

    private JSONObject searchByProductTimes(String productName, long minTimes) throws SQLException {
        JSONObject result;
        JSONArray customersJson = getCustomers(Constants.SQL_PRODUCT_TIMES, productName, minTimes);
        result = JsonHandlerImpl.createProductTimeResult(productName, minTimes, customersJson);
        return result;
    }

    private JSONObject searchByExpensesRange(long minExpenses, long maxExpenses) throws SQLException {
        JSONObject result;
        JSONArray customersJson = getCustomers(Constants.SQL_EXPENSES_RANGE, minExpenses, maxExpenses);
        result = JsonHandlerImpl.createExpensesRangeResult(minExpenses, maxExpenses, customersJson);
        return result;
    }

    private JSONObject searchBadCustomers(long customersNumber) throws SQLException {
        JSONArray customersJson = getCustomers(Constants.SQL_BAD_CUSTOMERS, customersNumber);
        return JsonHandlerImpl.createBadCustomersResult(customersNumber, customersJson);
    }

    // выше находятся 4 метода поиска для операции search
    //-------------------------------------------

    //-------------------------------------------
    // ниже находятся 3 метода поиска для операции stat
    @Override
    public JSONArray findCustomersInfo(LocalDate startDate, LocalDate endDate) throws SQLException {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);
        JSONArray customersInfo = getCustomersInfo(sqlStartDate, sqlEndDate);
        return customersInfo;
    }

    @Override
    public long findCustomerTotalExpenses(long userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);
        return getTotalExpenses(userId, sqlStartDate, sqlEndDate);
    }

    @Override
    public JSONArray findCustomerPurchases(long userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);
        return getPurchases(userId, sqlStartDate, sqlEndDate);
    }
    // выше находятся 3 метода поиска для операции stat
    //-------------------------------------------


    private JSONArray getCustomersInfo(Object... values) throws SQLException {
        JSONArray customersInfoJson;
        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = DaoUtil.prepareStatement(connection, Constants.SQL_STAT_CUSTOMERS_INFO, values);
                ResultSet resultSet = statement.executeQuery();
        ) {
            customersInfoJson = ResultSetParser.parseCustomersInfo(resultSet);
        } catch (SQLException e) {
            log.log(Level.WARNING, "exception occurred during getCustomersInfo", e);
            throw e;
        }
        return customersInfoJson;
    }

    private JSONArray getPurchases(Object... values) throws SQLException {
        JSONArray customerPurchases;
        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = DaoUtil.prepareStatement(connection, Constants.SQL_STAT_PURCHASES, values);
                ResultSet resultSet = statement.executeQuery();
        ) {
            customerPurchases = ResultSetParser.parseCustomersPurchases(resultSet);
        } catch (SQLException e) {
            log.log(Level.WARNING, "exception occurred during getCustomerPurchases", e);
            throw e;
        }
        return customerPurchases;
    }

    private long getTotalExpenses(Object... values) throws SQLException {
        long totalExpenses;
        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = DaoUtil.prepareStatement(connection, Constants.SQL_STAT_TOTAL_EXPENSES, values);
                ResultSet resultSet = statement.executeQuery();
        ) {
            totalExpenses = ResultSetParser.parseTotalExpenses(resultSet);
        } catch (SQLException e) {
            log.log(Level.WARNING, "exception occurred during getTotalExpenses", e);
            throw e;
        }
        return totalExpenses;
    }

    // общий метод для запросов, которые возврращают имя и фамилию покупателей
    private JSONArray getCustomers(String sqlQuery, Object... values) throws SQLException {
        JSONArray customersJson;
        try (
                Connection connection = daoFactory.getConnection();
                PreparedStatement statement = DaoUtil.prepareStatement(connection, sqlQuery, values);
                ResultSet resultSet = statement.executeQuery();
        ) {
            customersJson = ResultSetParser.parseCustomers(resultSet);
        } catch (SQLException e) {
            log.log(Level.WARNING, "exception occurred during getCustomers", e);
            throw e;
        }
        return customersJson;
    }
}
