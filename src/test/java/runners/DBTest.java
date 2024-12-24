package runners;

import mtf.dm.cms.hhs.gov.utilities.DBUtils;

import java.sql.ResultSet;

public class DBTest {
    public static void main(String[] args) {
        String url = "jdbc:ucanaccess://src/test.dataExchange/resources/database/dataExchange.accdb";

        // No username/password for Access databases by default
        String username = "";
        String password = "";

        try {
            // Create connection
            DBUtils.createConnection(url, username, password);

            // Run a sample query
            ResultSet resultSet = DBUtils.runQuery("SELECT * FROM price_eff_dt");

            // Display results
            DBUtils.displayAllData();

            // Close resources
            DBUtils.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
