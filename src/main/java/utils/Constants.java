package utils;

import java.time.format.DateTimeFormatter;

public final class Constants {

    public static final String SQL_LAST_NAME = "SELECT firstName, lastName FROM Customers WHERE lastName= ?;";
    public static final String SQL_PRODUCT_TIMES = "SELECT firstName, lastName FROM(Customers LEFT JOIN  Purchases ON Customers.id = Purchases.Customer  ) GROUP BY Customers.id HAVING count(case WHEN product= ? then 1 else null end ) >= ?;";
    public static final String SQL_EXPENSES_RANGE = "SELECT firstName, lastName FROM Customers   LEFT JOIN Purchases ON Customers.id = Purchases.Customer LEFT JOIN Products ON Purchases.product = Products.productname GROUP BY Customers.id HAVING sum(COALESCE(price, 0))>= ? and sum(COALESCE(price, 0))<= ?;";
    public static final String SQL_BAD_CUSTOMERS = "(SELECT firstName, lastName, sum(price) AS totalExpenses FROM Customers LEFT JOIN Purchases ON Customers.id = Purchases.Customer LEFT JOIN Products ON Purchases.product = Products.productname GROUP BY Customers.id) ORDER BY totalExpenses ASC  LIMIT ? ;";

    public static final String SQL_STAT_PURCHASES = "SELECT  productName,  SUM(Price)  as sum  from Customers inner join Purchases ON Customers.id= Purchases.Customer INNER JOIN Products ON Purchases.Product=Products.productName   WHERE Customers.id= ? and Date BETWEEN ? and ? GROUP BY Products.productName ORDER By sum DESC;";
    public static final String SQL_STAT_TOTAL_EXPENSES = "SELECT SUM(Price)  FROM Products INNER JOIN Purchases ON Products.productName=Purchases.Product where Purchases.Customer= ? and Date BETWEEN ? and ?;";
    public static final String SQL_STAT_CUSTOMERS_INFO = "SELECT DISTINCT id, firstName,lastName  FROM Customers INNER  JOIN  Purchases ON Customers.id = Purchases.Customer where Date BETWEEN  ?  and  ? ;";

    public static final String PROPERTY_URL = "url";
    public static final String PROPERTY_DRIVER = "driver";
    public static final String PROPERTY_USERNAME = "username";
    public static final String PROPERTY_PASSWORD = "password";

    public static final String SEARCH_TYPE = "search";
    public static final String STAT_TYPE = "stat";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

}
