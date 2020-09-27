package dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.time.LocalDate;

public interface Dao {

    JSONObject searchByCriteria(JSONObject criteria) throws SQLException;

    JSONArray findCustomersInfo(LocalDate startDate, LocalDate endDate) throws SQLException;

    long findCustomerTotalExpenses(long userId, LocalDate startDate, LocalDate endDate) throws SQLException;

    JSONArray findCustomerPurchases(long userId, LocalDate startDate, LocalDate endDate) throws SQLException;


}
