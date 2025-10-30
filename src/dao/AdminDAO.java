package dao;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Admin;

public class AdminDAO {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/motor?useSSL=false";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "12345";
    
    private static final String LOGIN_QUERY = "SELECT id, username, full_name FROM admin_users WHERE username = ? AND password = SHA2(?, 256)";
    
    public Admin authenticate(String username, String password) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             PreparedStatement stmt = conn.prepareStatement(LOGIN_QUERY)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            System.out.println("Querying for username: " + username); // Debug
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Login successful for: " + username); // Debug
                    Admin admin = new Admin();
                    admin.setId(rs.getInt("id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setFullName(rs.getString("full_name"));
                    return admin;
                } else {
 System.out.println("No match found for username or password"); // Debug
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}