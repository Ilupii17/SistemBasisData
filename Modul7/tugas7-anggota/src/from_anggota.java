import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class from_anggota extends javax.swing.JFrame {
    private DefaultTableModel tabmode;

    // Method untuk menampilkan data anggota ke dalam tabel
    private void tampil_anggota() {
        Object[] baris = {"ID Anggota", "Nama Anggota", "Alamat", "Telepon", "Tanggal Bergabung"};
        tabmode = new DefaultTableModel(null, baris);
        tbl_anggota.setModel(tabmode);

        String sql = "SELECT * FROM anggota ORDER BY id_anggota ASC";

        try {
            Connection kon = new koneksi().getConnection(); // Membuka koneksi
            Statement stat = kon.createStatement();
            ResultSet hasil = stat.executeQuery(sql);

            while (hasil.next()) {
                String id_anggota = hasil.getString("id_anggota");
                String nama_anggota = hasil.getString("nama_anggota");
                String alamat = hasil.getString("alamat");
                String telpon = hasil.getString("telpon");
                String tanggal_bergabung = hasil.getString("tanggal_bergabung");

                String[] data = {id_anggota, nama_anggota, alamat, telpon, tanggal_bergabung};
                tabmode.addRow(data);
            }
            kon.close(); // Menutup koneksi
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Menampilkan data gagal: " + e.getMessage(), "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Konstruktor
    public from_anggota() {
        initComponents(); // Inisialisasi komponen GUI
        tampil_anggota(); // Menampilkan data ke tabel
    }

    // Method untuk inisialisasi komponen GUI
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_anggota = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Data Anggota");

        tbl_anggota.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID Anggota", "Nama Anggota", "Alamat", "Telepon", "Tanggal Bergabung"
                }
        ));
        jScrollPane1.setViewportView(tbl_anggota);

        // Layout GUI
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                new from_anggota().setVisible(true);
            }
        });
    }

    // Deklarasi variabel komponen GUI
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_anggota;
}

