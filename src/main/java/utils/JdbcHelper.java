/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Admin
 */
public class JdbcHelper {
    private static final String DB_NAME = "NetCafeManagerDB";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123";
    private static final String LOCALHOST = getIpLocalHost();
    private static final String URL = "jdbc:sqlserver://" + LOCALHOST + ":1433;databaseName=" + DB_NAME + ";encrypt=false";
    
    // Static block to load the driver class
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    // Method to get the IP address of the local host
    public static String getIpLocalHost() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (Exception e) {
            System.err.println("Không tìm thấy địa chỉ IP của máy SERVER: " + e.getMessage());
        }
        return "localhost"; // fallback to localhost if there's an error
    }

    // Method to get a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
    // Method to execute an INSERT and return generated keys
    public static int executeInsertAndGetGeneratedKey(String sql, Object... args) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(pstmt, args);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the generated key
                }
            }
        }
        return -1; // Return -1 if no key was generated
    }

    // Method to execute an update (UPDATE, DELETE)
    public static void executeUpdate(String sql, Object... args) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setParameters(pstmt, args);
            pstmt.executeUpdate();
        }
    }

    // Method to execute a query
    public static ResultSet executeQuery(String sql, Object... args) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        setParameters(pstmt, args);
        return pstmt.executeQuery();
    }

    // Method to set parameters for PreparedStatement
    private static void setParameters(PreparedStatement pstmt, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            pstmt.setObject(i + 1, args[i]);
        }
    }

    // Method to close resources
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}



