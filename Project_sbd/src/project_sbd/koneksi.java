/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project_sbd;

/**
 *
 * @author Hp
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/cinema";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Ganti sesuai password MySQL Anda

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal terhubung ke database");
        }
    }
}

