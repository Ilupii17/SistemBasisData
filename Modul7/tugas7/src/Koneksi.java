import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {
    private Connection konek;

    public Connection getConnection() {
        try {
            konek = DriverManager.getConnection("jdbc:mysql://localhost/tugas_sbd","root", "");
            System.out.println("Koneksi Berhasil");
            JOptionPane.showMessageDialog(null,"Koneksi Berhasil");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Koneksi Gagal" + e.getMessage());
        }
        return konek;
    }

    // Metode main untuk menguji koneksi
    public static void main(String[] args) {
        Koneksi koneksTest = new Koneksi();
        Connection connection = koneksTest.getConnection();
    }
}
