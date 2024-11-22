package com.flashcard.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;
    
    // Oracle DB connection details
    private static final String SERVER = "localhost";  // Oracle server
    private static final String PORT = "1521";         // Oracle default port
    private static final String SID = "orcl";          // Oracle SID or Service Name
    private static final String DATABASE = "FlashCard";
    private static final String USERNAME = "system";   // Oracle database username
    private static final String PASSWORD = "1234$";    // Oracle database password

    // Oracle connection URL using SID
    private static final String CONNECTION_URL = "jdbc:oracle:thin:@" + SERVER + ":" + PORT + ":" + DATABASE;
    
    // If you're using Service Name instead of SID, the URL changes like this:
    // private static final String CONNECTION_URL = "jdbc:oracle:thin:@" + SERVER + ":" + PORT + "/"+ DATABASE;

    private DatabaseConnection() {
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public void connectToDatabase() throws SQLException {
        try {
            // Load Oracle JDBC driver
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
            System.out.println("Kết nối Oracle thành công.");
        } catch (ClassNotFoundException e) {
            System.out.println("Lỗi nạp thư viện Oracle JDBC");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối Oracle: " + e.getMessage());
            throw e;
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connectToDatabase();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thiết lập lại kết nối: " + e.getMessage());
        }
        return connection;
    }


    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}