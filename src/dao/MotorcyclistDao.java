package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Motorcyclist;

public class MotorcyclistDao {
    private String jdbcurl = "jdbc:mysql://localhost:3306/motor";
    private String dbUsername = "root";
    private String dbPassword = "12345";

    
   public int add(Motorcyclist rider) {
       if (!isValidPhoneNumber(rider.getPhone())) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits");
        }
        if (!isValidLicenseNumber(rider.getLicenseNumber())) {
            throw new IllegalArgumentException("License number must be exactly 16 digits");
        }
    try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword)) {
        String sql = "INSERT INTO Motorcyclists (name, license_number, phone, date_of_birth, cooperative_id) " +
                     "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        
        stmt.setString(1, rider.getName());
        stmt.setString(2, rider.getLicenseNumber());
        stmt.setString(3, rider.getPhone());
        stmt.setDate(4, new java.sql.Date(rider.getDateOfBirth().getTime())); // Added DOB
        stmt.setInt(5, rider.getCooperativeId());
        
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected; // Return the number of rows affected
    } catch (SQLException ex) {
        ex.printStackTrace();
        return -1; // Indicate failure
    }
}

    // 2. Update rider (now with DOB)
    public boolean update(Motorcyclist rider) {
    try {
        if (!isValidPhoneNumber(rider.getPhone())) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits");
        }
        if (!isValidLicenseNumber(rider.getLicenseNumber())) {
            throw new IllegalArgumentException("License number must be exactly 16 digits");
        }
        Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
        String sql = "UPDATE Motorcyclists SET name=?, license_number=?, phone=?, " +
                     "date_of_birth=?, cooperative_id=? WHERE rider_id=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        
        stmt.setString(1, rider.getName());
        stmt.setString(2, rider.getLicenseNumber());
        stmt.setString(3, rider.getPhone());
        stmt.setDate(4, new java.sql.Date(rider.getDateOfBirth().getTime()));
        stmt.setInt(5, rider.getCooperativeId());
        stmt.setInt(6, rider.getId());
        
        int rowsAffected = stmt.executeUpdate();
        con.close();
        return rowsAffected > 0;  // Return true if at least one row was updated
    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
}

    // 3. Delete rider (unchanged)
   public boolean delete(int riderId) {
    Connection con = null;
    PreparedStatement stmt = null;
    
    try {
        con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
        // Using backticks to handle any case sensitivity issues
        String sql = "DELETE FROM `Motorcyclists` WHERE `rider_id` = ?";
        stmt = con.prepareStatement(sql);
        
        stmt.setInt(1, riderId);
        int rowsAffected = stmt.executeUpdate();
        
        if (rowsAffected > 0) {
            System.out.println("Successfully deleted rider with ID: " + riderId);
            return true;
        } else {
            System.out.println("No rider found with ID: " + riderId);
            return false;
        }
    } catch (SQLException ex) {
        System.err.println("SQL Error deleting rider:");
        System.err.println("SQLState: " + ex.getSQLState());
        System.err.println("Error Code: " + ex.getErrorCode());
        System.err.println("Message: " + ex.getMessage());
        return false;
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        } catch (SQLException ex) {
            System.err.println("Error closing resources: " + ex.getMessage());
        }
    }
}

    // 4. Find rider by ID (now with DOB)
    public Motorcyclist findMotorcyclistById(int riderId) {
        try {
            Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
            String sql = "SELECT m.*, c.name AS cooperative_name FROM Motorcyclists m " +
                         "JOIN Cooperatives c ON m.cooperative_id = c.cooperative_id " +
                         "WHERE m.rider_id=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setInt(1, riderId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Motorcyclist rider = new Motorcyclist();
                rider.setId(rs.getInt("rider_id"));
                rider.setName(rs.getString("name"));
                rider.setLicenseNumber(rs.getString("license_number"));
                rider.setPhone(rs.getString("phone"));
                rider.setDateOfBirth(rs.getDate("date_of_birth")); // Added DOB
                rider.setCooperativeId(rs.getInt("cooperative_id"));
                rider.setCooperativeName(rs.getString("cooperative_name"));
                
                con.close();
                return rider;
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // 5. Get all riders (now with DOB)
    public List<Motorcyclist> findAllMotorcyclist() {
        List<Motorcyclist> riders = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
            String sql = "SELECT m.*, c.name AS cooperative_name FROM Motorcyclists m " +
                         "JOIN Cooperatives c ON m.cooperative_id = c.cooperative_id";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Motorcyclist rider = new Motorcyclist();
                rider.setId(rs.getInt("rider_id"));
                rider.setName(rs.getString("name"));
                rider.setLicenseNumber(rs.getString("license_number"));
                rider.setPhone(rs.getString("phone"));
                rider.setDateOfBirth(rs.getDate("date_of_birth")); // Added DOB
                rider.setCooperativeId(rs.getInt("cooperative_id"));
                rider.setCooperativeName(rs.getString("cooperative_name"));
                
                riders.add(rider);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return riders;
    }
    // In MotorcyclistDao.java
public String getRiderName(int riderId) {
    try (Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
         PreparedStatement stmt = con.prepareStatement(
             "SELECT name FROM Motorcyclists WHERE rider_id = ?")) {
        
        stmt.setInt(1, riderId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return rs.getString("name");
        }
        return "Unknown Rider"; // Default if not found
    } catch (Exception ex) {
        ex.printStackTrace();
        return "Error Loading Name";
    }
}


public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return false;
        // Remove non-digit characters and check for exactly 10 digits
        String digits = phoneNumber.replaceAll("[^0-9]", "");
        return digits.matches("\\d{10}");
    }

    public static boolean isValidLicenseNumber(String licenseNumber) {
        if (licenseNumber == null) return false;
        // Remove non-digit characters and check for exactly 16 digits
        String digits = licenseNumber.replaceAll("[^0-9]", "");
        return digits.matches("\\d{16}");
    }

}