import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class from_buku extends javax.swing.JFrame {
  private DefaultTableModel tabmode;

    // Method untuk menampilkan data buku ke dalam tabel
  private void tampil_buku() {
    Object[] baris = {"Kode Buku", "Nama Buku", "Pengarang", "Penerbit"};
      tabmode = new DefaultTableModel(null, baris);
      tbl_buku.setModel(tabmode);

      String sql = "SELECT * FROM tbl_buku ORDER BY id_buku ASC";

      try {
        Connection kon = new Koneksi().getConnection(); // Membuka koneksi
        Statement stat = kon.createStatement();
        ResultSet hasil = stat.executeQuery(sql);

      while (hasil.next()) {
        String id_buku = hasil.getString("id_buku");
        String nama_buku = hasil.getString("nama_buku");
        String pengarang = hasil.getString("pengarang");
        String penerbit = hasil.getString("penerbit");
        String[] data = {id_buku, nama_buku, pengarang, penerbit};
        tabmode.addRow(data);
      }
      kon.close(); // Menutup koneksi
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Menampilkan data gagal", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Konstruktor
    public from_buku() {
        initComponents(); // Inisialisasi komponen GUI
        tampil_buku();    // Menampilkan data ke tabel
    }

    // Method untuk inisialisasi komponen GUI
    private void initComponents() {
      jLabel1 = new javax.swing.JLabel();
      jScrollPane1 = new javax.swing.JScrollPane();
      tbl_buku = new javax.swing.JTable();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

      jLabel1.setText("Data Buku");

      tbl_buku.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{
            "Kode Buku", "Nama Buku", "Pengarang", "Penerbit"
          }
      ));
      jScrollPane1.setViewportView(tbl_buku);

      // Layout GUI
      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
          layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
            .addGap(20, 20, 20)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel1))
            .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
              .addGap(20, 20, 20)
              .addComponent(jLabel1)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
              .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }

    // Method main untuk menjalankan aplikasi
    public static void main(String[] args) {
      java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
          new from_buku().setVisible(true);
          }
      });
    }

    // Deklarasi variabel komponen GUI
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_buku;
}
