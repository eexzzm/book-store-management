/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

/**
 *
 * @author Lenovo
 */

import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Database {
    private static Connection connect;
    
    //Melakukan koneksi ke database
    public static Connection getConnection() {
        if (connect == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/Unsika_Book_Store";
                String user = "root";
                String password = "";
                connect = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connect;
    }
    
    public String getUser(String username, String password) {
        String role = null;
        String queryAdmin = "SELECT * FROM admin WHERE Username=? AND Password=?";
        String queryPembeli = "SELECT * FROM pembeli WHERE Username=? AND Password=?";
        
        try (Connection conn = getConnection()) {
            // Cek di tabel admin
            try (PreparedStatement pstAdmin = conn.prepareStatement(queryAdmin)) {
                pstAdmin.setString(1, username);
                pstAdmin.setString(2, password);
                
                try (ResultSet rsAdmin = pstAdmin.executeQuery()) {
                    if (rsAdmin.next()) {
                        
                        role = "admin";
                        return role;
                    }
                }
            }
            
            // Cek di tabel pembeli
            try (PreparedStatement pstPembeli = conn.prepareStatement(queryPembeli)) {
                pstPembeli.setString(1, username);
                pstPembeli.setString(2, password);
                
                try (ResultSet rsPembeli = pstPembeli.executeQuery()) {
                    if (rsPembeli.next()) {
        System.out.print(role);
        System.out.print(username);
        System.out.print(password);
                        
                        role = "pembeli";
                        return role;
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public boolean registerPembeli(String username, String password, String nomorHp, String alamat) {
        String checkQuery = "SELECT * FROM pembeli WHERE Username = ?";
        String insertQuery = "INSERT INTO pembeli (Username, Password, NomorHp, Alamat) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection()) {
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Username already exists! Please choose another.");
                        return false;
                    }
                }
            }
            
            // Jika username belum digunakan
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setString(3, nomorHp);
                insertStmt.setString(4, alamat);
                
                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred during registration. Please try again.");
        }
        return false;
    }
    
    public static class Pembeli {
        public void lihatDaftarBuku()  {
            String query = "SELECT * FROM Produk";
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                
                while (resultSet.next()) {
                    int idBuku = resultSet.getInt("IdBuku");
                    String judulBuku = resultSet.getString("JudulBuku");
                    String namaPenulis = resultSet.getString("NamaPenulis");
                    double harga = resultSet.getDouble("Harga");
                    String pesan = resultSet.getString("Pesan");
                    
                    System.out.println("ID: " + idBuku + ", Judul: " + judulBuku + ", Penulis: " + namaPenulis + ", Harga: " + harga + ", Pesan: " + pesan);
                }
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat mengambil data buku.");
                e.printStackTrace();
            }
        }
        public void pesanBuku(int idPembeli, int idBuku, int jumlahBuku) {
            String query = "INSERT INTO Transaksi (IdPembeli, IdBuku, JumlahBuku) VALUES (?, ?, ?)";
            
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setInt(1, idPembeli);
                preparedStatement.setInt(2, idBuku);
                preparedStatement.setInt(3, jumlahBuku);
                
                preparedStatement.executeUpdate();
                System.out.println("Pemesanan buku berhasil dilakukan.");
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat melakukan pemesanan.");
                e.printStackTrace();
            }
        }
    }
    
    // Kelas Admin untuk tambah, edit, dan hapus buku
    public static class Admin {
        
        // Menambah buku baru
        public void tambahBuku(int idBuku, String judulBuku, String namaPenulis, double harga)  {
            String query = "INSERT INTO Produk (IdBuku, JudulBuku, NamaPenulis, Harga) VALUES (?, ?, ?, ?)";
            
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setInt(1, idBuku);
                preparedStatement.setString(2, judulBuku);
                preparedStatement.setString(3, namaPenulis);
                preparedStatement.setDouble(4, harga);
                
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat menambah buku.");
                e.printStackTrace();
            }
        }
        
        // Mengedit buku
        public void editBuku(int idBuku, String judulBuku, String namaPenulis, double harga, String pesan) {
            String query = "UPDATE Produk SET JudulBuku = ?, NamaPenulis = ?, Harga = ? WHERE IdBuku = ?";
            
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setString(1, judulBuku);
                preparedStatement.setString(2, namaPenulis);
                preparedStatement.setDouble(3, harga);
                preparedStatement.setInt(4, idBuku);
                
                preparedStatement.executeUpdate();
                System.out.println("Buku berhasil diedit.");
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat mengedit buku.");
                e.printStackTrace();
            }
        }
        
        // Menghapus buku
        public void hapusBuku(int idBuku) {
            String query = "DELETE FROM Produk WHERE IdBuku = ?";
            
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setInt(1, idBuku);
                
                preparedStatement.executeUpdate();
                System.out.println("Buku berhasil dihapus.");
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat menghapus buku.");
                e.printStackTrace();
            }
        }
        
        // Meng-ACC pemesanan
        public void accPemesanan(int idTransaksi, int idAdmin) {
            String query = "UPDATE Transaksi SET IdAdmin = ?, Status = 'ACC' WHERE IdTransaksi = ?";
            
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setInt(1, idAdmin);
                preparedStatement.setInt(2, idTransaksi);
                
                preparedStatement.executeUpdate();
                System.out.println("Pemesanan telah disetujui.");
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat meng-ACC pemesanan.");
                e.printStackTrace();
            }
        }
        
        // Melihat daftar pemesanan yang menunggu ACC
        public void lihatPemesananMenunggu() {
            String query = "SELECT * FROM Transaksi WHERE Status IS NULL";
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                
                while (resultSet.next()) {
                    int idTransaksi = resultSet.getInt("IdTransaksi");
                    int idPembeli = resultSet.getInt("IdPembeli");
                    int idBuku = resultSet.getInt("IdBuku");
                    int jumlahBuku = resultSet.getInt("JumlahBuku");
                    
                    System.out.println("ID Transaksi: " + idTransaksi + ", Pembeli: " + idPembeli + ", Buku: " + idBuku + ", Jumlah: " + jumlahBuku);
                }
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat mengambil data pemesanan.");
                e.printStackTrace();
            }
        }
    }
}
