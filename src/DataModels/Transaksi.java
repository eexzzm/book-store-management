/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModels;

/**
 *
 * @author Lenovo
 */
public class Transaksi {
    public int idTransaksi, idPembeli, idBuku, jumlahBuku;
    public String alamat, noHp, status;
    public double harga;
    
//    public Transaksi(int Tra=, int idBuku, String alamat, double harga, String noHp, String status){
//        this.idPembeli = idPembeli;
//        this.idBuku = idBuku;
//        this.alamat = alamat;
//        this.harga = harga;
//        this.noHp = noHp;
//        this.status = status;
//    }
    
    public Transaksi (int idTransaksi, int idPembeli, int idBuku, int jumlahBuku){
        this.idTransaksi = idTransaksi;
        this.idPembeli = idPembeli;
        this.idBuku = idBuku;
        this.jumlahBuku = jumlahBuku;
    }
}
