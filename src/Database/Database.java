/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

/**
 *
 * @author Lenovo
 */

//import Connections.JDBCConnection;
//import DataModels.Customer;
//import DataModels.Room;
//import MainMenu.SignIn;
//import Sessions.SessionManager;
//import DataModels.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;

public class Database {
    private static Connection connect;
    
    //Melakukan koneksi ke database
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/Unsika_Book_Store";
            String user = "root"; 
            String password = "";
            connect = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {  
            e.printStackTrace();
        }
        return connect;
    }
    
    public String getUser(String username, String password) {
        String role = null;
        String queryAdmin = "SELECT * FROM admin WHERE Username=? AND Password=?";
        String queryPembeli = "SELECT * FROM pembeli WHERE Username=? AND Password=?";
        
        try {
            
            try (PreparedStatement pst = connect.prepareStatement(queryAdmin)) {
                pst.setString(1, username);
                pst.setString(2, password);
                
                try (ResultSet rsAdmin = pst.executeQuery()) {
                    if(rsAdmin.next()) {
                        //Jika ditemukan di Admin, ambil data Admin
                        String Username = rsAdmin.getString("Username");
                        String Password = rsAdmin.getString("Password");
                        role = "admin"; // Tetapkan role 
                        return role;
                    }
                }
            }
            try (PreparedStatement pstPembeli = connect.prepareStatement(queryPembeli)) {
                pstPembeli.setString(1, username);
                pstPembeli.setString(2, password);
                
                try (ResultSet rsPembeli = pstPembeli.executeQuery()) {
                    if (rsPembeli.next()) {
                        // Jika ditemukan di Pembeli, ambil data Pembeli
                        int idPembeli = rsPembeli.getInt("IdPembeli");
                        String Username = rsPembeli.getString("Username");
                        String Password = rsPembeli.getString("Password");
                        String nomorHp = rsPembeli.getString("NomorHp");
                        String alamat = rsPembeli.getString("Alamat");
                        String pesan = rsPembeli.getString("Pesan");
                        
                        role = "pembeli"; // Tetapkan role 
                        return role;
                    }
                }
            }
            // Jika tidak ditemukan di kedua tabel
            JOptionPane.showMessageDialog(null, "Username or Password Is Incorrect!");
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        
    }
}
