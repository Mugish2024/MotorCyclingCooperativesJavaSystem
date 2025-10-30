
package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Cooperatives;
import model.Motorcyclist;


public class CooperativesDao {
    





    private String jdbcurl = "jdbc:mysql://localhost:3306/motor";
    private String dbUsername = "root";
    private String dbPassword = "12345";

    // 1. Add a new cooperative
    public int add(Cooperatives coop) {
    int result = 0;
    try {
        Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
        String sql = "INSERT INTO Cooperatives (name, location, interest_rate) VALUES (?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        
        stmt.setString(1, coop.getName());
        stmt.setString(2, coop.getLocation());
        stmt.setDouble(3, coop.getInterestRate());
        
        result = stmt.executeUpdate(); // ✅ number of rows affected
        con.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return result; // ✅ return it
}


    // 2. Update cooperative
    public int update(Cooperatives coop) {
    int result = 0;
    try {
        Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
        String sql = "UPDATE Cooperatives SET name=?, location=?, interest_rate=? WHERE cooperative_id=?";
        PreparedStatement stmt = con.prepareStatement(sql);

        stmt.setString(1, coop.getName());
        stmt.setString(2, coop.getLocation());
        stmt.setDouble(3, coop.getInterestRate());
        stmt.setInt(4, coop.getId());

        result = stmt.executeUpdate();
        con.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return result;
}

    // 3. Delete cooperative
    public int delete(int cooperativeId) {
        try {
            Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
            String sql = "DELETE FROM Cooperatives WHERE cooperative_id=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setInt(1, cooperativeId);
            stmt.executeUpdate();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    // 4. Find cooperative by ID
    public Cooperatives findById(int cooperativeId) {
        try {
            Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
            String sql = "SELECT * FROM Cooperatives WHERE cooperative_id=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, cooperativeId);
            
            ResultSet rs = stmt.executeQuery();
            Cooperatives coop = null;
            
            if (rs.next()) {
                coop = new Cooperatives();
                coop.setId(rs.getInt("cooperative_id"));
                coop.setName(rs.getString("name"));
                coop.setLocation(rs.getString("location"));
                coop.setInterestRate(rs.getDouble("interest_rate"));
            }
            
            con.close();
            return coop;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // 5. Get all cooperatives
    public List<Cooperatives> findAll() {
        List<Cooperatives> cooperatives = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
            String sql = "SELECT * FROM Cooperatives";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cooperatives coop = new Cooperatives();
                coop.setId(rs.getInt("cooperative_id"));
                coop.setName(rs.getString("name"));
                coop.setLocation(rs.getString("location"));
                coop.setInterestRate(rs.getDouble("interest_rate"));
                cooperatives.add(coop);
            }
            
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cooperatives;
    }

public List<Motorcyclist> getRidersByCooperative(int cooperativeId) {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<Motorcyclist> riders = new ArrayList<>();

    try {
        con = DriverManager.getConnection(jdbcurl, dbUsername, dbPassword);
        String sql = "SELECT m.rider_id, m.name, m.license_number, m.phone " +
                    "FROM Motorcyclists m " +
                    "WHERE m.cooperative_id = ?";
        
        stmt = con.prepareStatement(sql);
        stmt.setInt(1, cooperativeId);
        rs = stmt.executeQuery();

        while (rs.next()) {
            Motorcyclist rider = new Motorcyclist();
            rider.setId(rs.getInt("rider_id"));
            rider.setName(rs.getString("name"));
            rider.setLicenseNumber(rs.getString("license_number"));
            rider.setPhone(rs.getString("phone"));
            riders.add(rider);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        // Close resources
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
    return riders;
}
}
