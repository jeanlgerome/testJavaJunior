package dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultSetParser {

    private static Logger log = Logger.getLogger(ResultSetParser.class.getName());

    static JSONArray parseCustomers(ResultSet resultSet) throws SQLException {
        JSONArray customersList = new JSONArray();
        while (resultSet.next()) {
            try {
                JSONObject customerName = new JSONObject();
                customerName.put("lastName", resultSet.getString("lastName"));
                customerName.put("firstName", resultSet.getString("firstName"));
                customersList.add(customerName);
            } catch (SQLException e) {
                log.log(Level.WARNING, "sql exception occurred during customers resultSet parsing", e);
                throw e;
            }
        }
        log.log(Level.FINEST, "Customers resultSet has been parsed ");
        return customersList;
    }

    static JSONArray parseCustomersInfo(ResultSet resultSet) throws SQLException {
        JSONArray customersInfoList = new JSONArray();

        while (resultSet.next()) {
            try {
                JSONObject customerInfo = new JSONObject();
                customerInfo.put("id", resultSet.getLong("id"));
                customerInfo.put("firstName", resultSet.getString("firstName"));
                customerInfo.put("lastName", resultSet.getString("lastName"));
                customersInfoList.add(customerInfo);
            } catch (SQLException e) {
                log.log(Level.WARNING, "sql exception occurred during customersInfo resultSet parsing", e);
                throw e;
            }
        }
        log.log(Level.FINEST, "CustomersInfo resultSet has been parsed ");
        return customersInfoList;
    }

    static JSONArray parseCustomersPurchases(ResultSet resultSet) throws SQLException {
        JSONArray purchasesList = new JSONArray();
        while (resultSet.next()) {
            try {
                JSONObject purchase = new JSONObject();
                purchase.put("name", resultSet.getString("productName"));
                purchase.put("expenses", resultSet.getLong("sum"));
                purchasesList.add(purchase);
            } catch (SQLException e) {
                log.log(Level.WARNING, "sql exception occurred during CustomersPurchases resultSet parsing", e);
                throw e;
            }
        }
        log.log(Level.FINEST, "CustomersPurchases resultSet has been parsed ");
        return purchasesList;
    }

    static long parseTotalExpenses(ResultSet resultSet) throws SQLException {
        long totalExpenses;
        try {
            if (resultSet.next()) {
                totalExpenses = resultSet.getLong("sum");
            } else {
                totalExpenses = 0;
            }
        } catch (SQLException e) {
            log.log(Level.WARNING, "sql exception occurred during CustomersPurchases resultSet parsing", e);
            throw e;
        }
        log.log(Level.FINEST, "TotalExpenses resultSet has been parsed ");
        return totalExpenses;
    }
}
