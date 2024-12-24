package runners;

import mtf.dm.cms.hhs.gov.utilities.DBUtils;

import java.sql.ResultSet;

public class DBTest {
    public static void main(String[] args) {
        // Define the database URL (Adjust the path as necessary)
        // String url = "jdbc:ucanaccess://src/test/dataExchange/resources/database/dataExchange.accdb";
        String url = "jdbc:ucanaccess://C:/Users/rachowdhury/Documents/Software/mtf-dm-automation-test-api/src/test.dataExchange/resources/database/dataExchange.accdb";
        //String url = "jdbc:ucanaccess://C:/Users/rachowdhury/Documents/Software/mtf-dm-automation-test-api/src/test.dataExchange/resources/database/dataExchange.accdb";


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
