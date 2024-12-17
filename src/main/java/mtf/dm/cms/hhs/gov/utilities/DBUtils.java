package mtf.dm.cms.hhs.gov.utilities;

import java.sql.*;
import java.util.*;

public class DBUtils {

        private static Connection connection;
        public static Statement stm;
        public static ResultSet resultSet;
        private static ResultSetMetaData resultSetMetaData;


        /**
         * Create Connection by JDBC url and username, password
         *
         * @param url      jdbc url
         * @param username username for db
         * @param password password for db
         */
        public static void createConnection(String url, String username, String password) {
            try {
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("CONNECTION SUCCESSFUL");
            } catch (Exception e) {
                System.out.println("CONNECTION HAS FAILED " + e.getMessage());
                throw new RuntimeException(e);
            }
        }


        /**
         * Run the sql provided query and return ResultSet object
         *
         * @param query the query to run
         * @return ResultSet object with data
         */
        public static ResultSet runQuery(String query) {
            try {
                stm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                resultSet = stm.executeQuery(query);
                resultSetMetaData = resultSet.getMetaData();
            } catch (Exception e) {
                System.out.println("ERROR OCCURRED WHILE RUNNING QUERY  " + e.getMessage());
                throw new RuntimeException(e);
            }
            return resultSet;
        }

        /**
         * destroy method to clean up all the resources after being used
         */
        public static void destroy() {
            try {
                if (Objects.nonNull(resultSet)) resultSet.close();
                if (Objects.nonNull(stm)) stm.close();
                if (Objects.nonNull(connection)) connection.close();
            } catch (Exception e) {
                System.out.println("ERROR OCCURRED WHILE CLOSING RESOURCES  " + e.getMessage());
                throw new RuntimeException(e);
            }
        }


        /**
         * Reset the cursor to before first location
         */
        private static void resetCursor() {
            try {
                resultSet.beforeFirst();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        /**
         * Get Row Count
         *
         * @return row count
         */
        public static int getRowCount() {
            int rowCount = 0;
            try {
                resultSet.last();
                rowCount = resultSet.getRow();
            } catch (Exception e) {
                System.out.println("ERROR OCCURRED WHILE GETTING ROW COUNT  " + e.getMessage());
                throw new RuntimeException(e);
            } finally {
                resetCursor();
            }
            return rowCount;
        }


        /**
         * Get Column count
         *
         * @return
         */
        public static int getColumnCount() {
            int columnCount = 0;
            try {
                columnCount = resultSetMetaData.getColumnCount();
            } catch (Exception e) {
                System.out.println("ERROR OCCURRED WHILE GETTING COLUMN COUNT  " + e.getMessage());
                throw new RuntimeException(e);
            }
            return columnCount;
        }


        /**
         * Get all Column names as List
         *
         * @return List<String>
         */
        public static List<String> getAllColumnNamesAsList() {
            List<String> columnNameList = new ArrayList<>();
            try {
                for (int colIndex = 1; colIndex <= getColumnCount(); colIndex++) {
                    String columnName = resultSetMetaData.getColumnName(colIndex);
                    columnNameList.add(columnName);
                }
            } catch (Exception e) {
                System.out.println("ERROR OCCURRED WHILE getAllColumnNameAsList()  " + e.getMessage());
                throw new RuntimeException(e);
            }
            return columnNameList;
        }


        /**
         * Get row data as List
         *
         * @param rowNum int row number
         * @return List<String>
         */
        public static List<String> getRowDataAsList(int rowNum) {
            List<String> rowDataList = new ArrayList<>();
            int colCount = getColumnCount();
            try {
                resultSet.absolute(rowNum);
                for (int i = 1; i <= colCount; i++) {
                    String cellValue = resultSet.getString(i);
                    rowDataList.add(cellValue);
                }
            } catch (SQLException e) {
                System.out.println("ERROR OCCURRED WHILE RUNNING getRowDataAsList()  " + e.getMessage());
                throw new RuntimeException(e);
            } finally {
                resetCursor();
            }
            return rowDataList;
        }


        /**
         * Get Cell Value based on Row Number and Column Index
         *
         * @param rowNum      int row number
         * @param columnIndex int column index
         * @return location String value
         */
        public static String getCellValue(int rowNum, int columnIndex) {
            String cellValue = "";
            try {
                resultSet.absolute(rowNum);
                cellValue = resultSet.getString(columnIndex);
            } catch (Exception e) {
                System.out.println("ERROR OCCURRED WHILE RUNNING getCellValue()  " + e.getMessage());
                throw new RuntimeException(e);
            } finally {
                resetCursor();
            }
            return cellValue;
        }


        /**
         * Get Cell Value based on row number and column name
         *
         * @param rowNum     row number
         * @param columnName column name
         * @return location String value
         */
        public static String getCellValue(int rowNum, String columnName) {
            String cellValue = "";
            try {
                resultSet.absolute(rowNum);
                cellValue = resultSet.getString(columnName);
            } catch (Exception e) {
                System.out.println("ERROR OCCURRED WHILE RUNNING getCellValue()  " + e.getMessage());
                throw new RuntimeException(e);
            } finally {
                resetCursor();
            }
            return cellValue;
        }


        /**
         * Get First Cell Value at First Row First Column
         *
         * @return Cell Value
         */
        public static String getFirstRowFirstColumn() {
            return getCellValue(1, 1);
        }


        /**
         * Get Column Data as List based on Column Number
         *
         * @param columnNum column number
         * @return List<String>
         */
        public static List<String> getColumnDataAsList(int columnNum) {
            List<String> columnDataList = new ArrayList<>();
            try {
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    String cellValue = resultSet.getString(columnNum);
                    columnDataList.add(cellValue);
                }
            } catch (Exception e) {
                System.out.println("ERROR OCCURRED WHILE RUNNING getColumnDataAsList()  " + e.getMessage());
                throw new RuntimeException(e);
            } finally {
                resetCursor();
            }
            return columnDataList;
        }


        /**
         * Get entire column data as List based on column name
         *
         * @param columnName column name
         * @return List<String>
         */
        public static List<String> getColumnDataAsList(String columnName) {
            List<String> columnDataList = new ArrayList<>();
            try {
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    String cellValue = resultSet.getString(columnName);
                    columnDataList.add(cellValue);
                }
            } catch (Exception e) {
                System.out.println("ERROR OCCURRED WHILE RUNNING getColumnDataAsList()  " + e.getMessage());
                throw new RuntimeException(e);
            } finally {
                resetCursor();
            }
            return columnDataList;
        }


        /**
         * Display all Data from ResultSet Object
         */
        public static void displayAllData() {
            int columnCount = getColumnCount();
            resetCursor();
            try {
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.printf("%-25s%n", resultSet.getString(i));
                    }
                }
            } catch (SQLException e) {
                System.out.println("ERROR OCCURRED WHILE RUNNING displayAllData()  " + e.getMessage());
                throw new RuntimeException(e);
            }
        }


        /**
         * Save entire row data as MAP
         *
         * @param rowNum row number
         * @return Map<String, String>
         */
        public static Map<String, String> getRowMap(int rowNum) {
            Map<String, String> rowMap = new LinkedHashMap<>();
            try {
                resultSet.absolute(rowNum);
                for (int i = 1; i <= getColumnCount(); i++) {
                    String columnName = resultSetMetaData.getColumnName(i);
                    String cellValue = resultSet.getString(i);
                    rowMap.put(columnName, cellValue);
                }
            } catch (Exception e) {
                System.out.println("ERROR OCCURRED WHILE RUNNING getRowMap()  " + e.getMessage());
                throw new RuntimeException(e);
            } finally {
                resetCursor();
            }
            return rowMap;
        }


        /**
         * Store all rows as List of Map Object
         *
         * @return List<Map < String, String>>
         */
        public static List<Map<String, String>> getAllRowAsListOfMap() {
            List<Map<String, String>> allRowListOfMap = new ArrayList<>();
            for (int i = 1; i <= getRowCount(); i++) {
                Map<String, String> rowMap = getRowMap(i);
                allRowListOfMap.add(rowMap);
            }
            resetCursor();
            return allRowListOfMap;
        }
}
