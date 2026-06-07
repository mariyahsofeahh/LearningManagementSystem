/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lms.service;

/**
 *
 * @author DELL
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LearningMaterialService {

    // Configure your local instance credentials here
    private final String dbUrl = "jdbc:mysql://localhost:3307/lmsdb";
    private final String dbUser = "root";
    private final String dbPassword = "";

    public LearningMaterialService() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    // Combines validation hooks with database operational writes matching your sequence nodes
    public boolean processAndStoreMaterial(String name, String type, String path) {
        // validateMaterial() sequence hook node execution
        if (name == null || name.trim().isEmpty() || path == null) {
            return false; 
        }
        
        // storeMaterial() sequence hook node execution
        String sql = "INSERT INTO learning_materials (file_name, file_type, file_path) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            stmt.setString(2, type);
            stmt.setString(3, path);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Returns uploadStatus() -> success mapping
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, String>> getAllMaterials() {
        List<Map<String, String>> materialsList = new ArrayList<>();
        String sql = "SELECT * FROM learning_materials ORDER BY upload_date DESC";
        
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, String> material = new HashMap<>();
                material.put("id", String.valueOf(rs.getInt("id")));
                material.put("fileName", rs.getString("file_name"));
                material.put("fileType", rs.getString("file_type"));
                material.put("filePath", rs.getString("file_path"));
                material.put("uploadDate", rs.getTimestamp("upload_date").toString());
                materialsList.add(material);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return materialsList;
    }

    public Map<String, String> getMaterialById(int id) {
        String sql = "SELECT * FROM learning_materials WHERE id = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, String> material = new HashMap<>();
                    material.put("id", String.valueOf(rs.getInt("id")));
                    material.put("fileName", rs.getString("file_name"));
                    material.put("fileType", rs.getString("file_type"));
                    material.put("filePath", rs.getString("file_path"));
                    return material;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteMaterial(int id) {
        String sql = "DELETE FROM learning_materials WHERE id = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}