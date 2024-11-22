/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModels;

/**
 *
 * @author Lenovo
 */
public class Produk {
    public int IdBuku;
    public String JudulBuku, NamaPenulis;
    public double Harga;
    
    
    public Produk(int IdBuku, String JudulBuku, String NamaPenulis, double Harga ){
        this.IdBuku = IdBuku;
        this.JudulBuku = JudulBuku;
        this.NamaPenulis = NamaPenulis;
        this.Harga = Harga;
    }
}
