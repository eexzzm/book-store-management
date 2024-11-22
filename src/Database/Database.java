/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

/**
 *
 * @author Lenovo
 */

import DataModels.*;
import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Database {
    private static Connection connect;
    
    public Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/Unsika_Book_Store", "root", "");
        } catch (
            ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() {
        return connect;
    }
    
    //Melakukan koneksi ke database
//    public static Connection getConnection() {
////        if (connect == null) {
////            try {
////                String url = "jdbc:mysql://localhost:3306/Unsika_Book_Store";
////                String user = "root";
////                String password = "";
////                connect = DriverManager.getConnection(url, user, password);
////            } catch (SQLException e) {
////                e.printStackTrace();
////            }
////        }
////        return connect;
//    }
    
    public String getUser(String username, String password) {
        String role = null;
        String queryAdmin = "SELECT * FROM admin WHERE Username=? AND Password=?";
        String queryPembeli = "SELECT * FROM pembeli WHERE Username=? AND Password=?";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Unsika_Book_Store", "root", "")) {
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
        public ArrayList<Produk> lihatDaftarBuku()  {
            String query = "SELECT * FROM Produk";
            ArrayList<Produk> listProduk = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Unsika_Book_Store", "root", "");
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        int idBuku = rs.getInt("IdBuku");
                        String judulBuku = rs.getString("JudulBuku");
                        String namaPenulis = rs.getString("NamaPenulis");
                        double harga = rs.getDouble("Harga");
                        
                        Produk produk = new Produk(idBuku, judulBuku, namaPenulis, harga);
                        listProduk.add(produk);
                    };
                }
                
                return listProduk;
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat mengambil data buku.");
                e.printStackTrace();
                return null;
            }
        }
        
        
    
        
        // Menambah buku baru
        public ResponDatabase tambahBuku(String judulBuku, String namaPenulis, double harga)  {
            String query = "INSERT INTO Produk (JudulBuku, NamaPenulis, Harga) VALUES (?, ?, ?)";
            
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setString(1, judulBuku);
                preparedStatement.setString(2, namaPenulis);
                preparedStatement.setDouble(3, harga);
                
                preparedStatement.executeUpdate();
                return new ResponDatabase("sukses", null);
                
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat menambah buku.");
                e.printStackTrace();
                return new ResponDatabase("gagal", null);

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
        public ResponDatabase hapusBuku(int idBuku) {
            String query = "DELETE FROM Produk WHERE IdBuku = ?";
            
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Unsika_Book_Store", "root", "");
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setInt(1, idBuku);
                
                preparedStatement.executeUpdate();
                System.out.println("Buku berhasil dihapus.");
                
                return new ResponDatabase("sukses", null);
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat menghapus buku.");
                e.printStackTrace();
                return new ResponDatabase("gagal", null);
            }
        }
        
        // Meng-ACC pemesanan
        public void accPemesanan(int idTransaksi, int idAdmin) {
            String status = null;
            String query = "UPDATE transaksi SET transaksi.IdAdmin = ?, detil_transaksi.StatusTransaksi = '?' WHERE IdTransaksi = ?"
                    +" INNER JOIN detil_transaksi ON transaksi.idTransaksi=detil_transaksi.idTransaksi";
                            // query inner join masih sementara
            
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setInt(1, idAdmin);
                preparedStatement.setInt(2, idTransaksi);
                preparedStatement.setString(3, status);
                
                
                preparedStatement.executeUpdate();
                System.out.println("Pemesanan telah disetujui.");
                
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat meng-ACC pemesanan.");
                e.printStackTrace();
            }
        }
        
        //Melihat daftar pemesanan yang menunggu ACC
        public ArrayList<Transaksi> lihatTransaksi() {
             String query = "SELECT * FROM transaksi";
            ArrayList<Transaksi> listTransaksi = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Unsika_Book_Store", "root", "");
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        int idTransaksi = rs.getInt("IdTransaksi");
                        int idPembeli = rs.getInt("IdPembeli");
                        int idBuku = rs.getInt("IdBuku");
                        int jumlahBuku = rs.getInt("JumlahBuku");
                        
                        Transaksi transaksi = new Transaksi( idTransaksi, idPembeli,idBuku, jumlahBuku);
                        listTransaksi.add(transaksi);
                    };
                }
                
                return listTransaksi;
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan saat mengambil data buku.");
                e.printStackTrace();
                return null;
            }
            
//            String query = "SELECT transaksi.idPembeli, transaksi.idBuku, FROM transaksi WHERE Status IS NULL";
//            ArrayList<Transaksi> listTransaksi = new ArrayList<>();
//            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Unsika_Book_Store", "root", "");
//                 Statement statement = connection.createStatement();
//                 ResultSet resultSet = statement.executeQuery(query)) {
//                
//                while (resultSet.next()) {
//                    int idTransaksi = resultSet.getInt("IdTransaksi");
//                    int idPembeli = resultSet.getInt("IdPembeli");
//                    int idBuku = resultSet.getInt("IdBuku");
//                    int jumlahBuku = resultSet.getInt("JumlahBuku");
//                    
//                    System.out.println("ID Transaksi: " + idTransaksi + ", Pembeli: " + idPembeli + ", Buku: " + idBuku + ", Jumlah: " + jumlahBuku);
//                    return listTransaksi;
//                }
//            } catch (SQLException e) {
//                System.out.println("Terjadi kesalahan saat mengambil data pemesanan.");
//                e.printStackTrace();
//                return null;
//            }
        }
        
        public void pesanBuku(int idPembeli, int idBuku, int jumlahBuku, String status, double totalHarga) {
            String queryTransaksi = "INSERT INTO transaksi (IdPembeli, IdBuku, JumlahBuku, StatusTransaksi, TotalHarga) VALUES (?, ?, ?, Pending, ?)";
            
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Unsika_Book_Store", "root", "")){
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryTransaksi)) {
                
                preparedStatement.setInt(1, idPembeli);
                preparedStatement.setInt(2, idBuku);
                preparedStatement.setInt(3, jumlahBuku);
                preparedStatement.setString(4, status);
                preparedStatement.setDouble(5, totalHarga);
                
                System.out.println("totalHargaNihBosss di database: "+totalHarga); 
                
                
                preparedStatement.executeUpdate();
                System.out.println("Pemesanan buku berhasil dilakukan.");
                } 
            
            } catch (SQLException e) {
                System.out.println("Terjadi kesalahan pesan buku.query pesan buku "); 
            }
        }
        
//        public void pesanBukuDetil(String status, double harga) {
//    String queryDetilTransaksi = "INSERT INTO detail_transaksi (StatusTransaksi, TotalPembayaran) VALUES (?,?)";
//            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Unsika_Book_Store", "root", "")){
//        try (PreparedStatement preparedStatement = connection.prepareStatement(queryDetilTransaksi)) {
//
//        preparedStatement.setString(1, status);
//        preparedStatement.setDouble(2, harga);
//
//        int rowsInserted = preparedStatement.executeUpdate(); // Mengecek jumlah baris yang diinsert
//        if (rowsInserted > 0) {
//            System.out.println("Pemesanan buku berhasil dilakukan (status dan harga total).");
//        } else {
//            System.out.println("Tidak ada baris yang diinsert ke detail_transaksi.");
//        }
//    } catch (SQLException e) {
//        System.out.println("Terjadi kesalahan pesan buku.query pesan buku detil");
//        e.printStackTrace();
//    }
//    
//    }
//}


    public int getIdPembeli() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
