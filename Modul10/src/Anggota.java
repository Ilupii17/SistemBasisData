
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

//library simpel date (untuk tanggal)
import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 *
 * @author ASUS
 */
public class Anggota extends javax.swing.JFrame {

    /**
     * Creates new form Anggota
     */
    
    private DefaultTableModel tabmode; 
    
    private void clear_input() {
    txt_namaAnggota.setText("");
    txt_alamat.setText("");
    txt_telpon.setText("");
    txt_tanggal.setText("");  // Jika Anda juga ingin mengosongkan input tanggal
}

    private boolean validateInput() {
    // Validasi jika ada field yang kosong
    if (txt_namaAnggota.getText().isEmpty() || txt_alamat.getText().isEmpty() || txt_telpon.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return false;
    }
    return true;
}
    
    private void tampil_anggota() {
    // Menentukan kolom tabel
    Object[] baris = {"ID Anggota", "Nama Anggota", "Alamat", "Telepon", "Tanggal Bergabung"};
    tabmode = new DefaultTableModel(null, baris);
    tbl_anggota.setModel(tabmode);

    // SQL untuk mengambil data dari tabel
    String sql = "SELECT * FROM anggota ORDER BY id_anggota ASC";

    try (
        // Membuat koneksi dan statement menggunakan try-with-resources
        Connection kon = new koneksi().getConnection();
        Statement stat = kon.createStatement();
        ResultSet hasil = stat.executeQuery(sql)
    ) {
        // Menambahkan data ke tabel
        while (hasil.next()) {
            String id_anggota = hasil.getString("id_anggota");
            String nama_anggota = hasil.getString("nama_anggota");
            String alamat = hasil.getString("alamat");
            String telpon = hasil.getString("telpon");
            String tanggal_bergabung = hasil.getString("tanggal_bergabung");
            String[] data = {id_anggota, nama_anggota, alamat, telpon, tanggal_bergabung};
            tabmode.addRow(data);
        }
    } catch (Exception e) {
        // Menampilkan pesan error
        JOptionPane.showMessageDialog(null, "Menampilkan data Gagal: " + e.getMessage(),
                                      "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }
}
    
    

private void simpan_data() {
    // Mengambil nilai input dari field teks nama anggota
    String nama_anggota = txt_namaAnggota.getText();
    // Mengambil nilai input dari field teks alamat
    String alamat = txt_alamat.getText();
    // Mengambil nilai input dari field teks telpon
    String telpon = txt_telpon.getText();
    // Mengambil nilai input dari field teks tanggal
    String tanggal = txt_tanggal.getText();

    // Validasi format tanggal
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false); // Set lenient to false to avoid accepting invalid date
    
    if (!telpon.matches("\\d+")) {
        JOptionPane.showMessageDialog(this, "Nomor yang dimasukkan harus angka!", "Input Error", JOptionPane.ERROR_MESSAGE);
        return; // Menghentikan proses simpan jika input salah
    }

    try {
        // Cek apakah tanggal valid
        dateFormat.parse(tanggal);
    } catch (ParseException e) {
        // Jika tidak valid, tampilkan pesan kesalahan
        JOptionPane.showMessageDialog(null, "Format tanggal salah, harus menggunakan yyyy-MM-dd", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        return; // Menghentikan proses simpan jika format tanggal salah
    }

    try {
        // Membuat koneksi ke database
        Connection kon = new koneksi().getConnection();
        
        // Membuat SQL INSERT dengan PreparedStatement untuk menghindari SQL Injection
        String sql = "INSERT INTO anggota (nama_anggota, alamat, telpon, tanggal_bergabung) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = kon.prepareStatement(sql);
        
        // Set nilai parameter
        pst.setString(1, nama_anggota);
        pst.setString(2, alamat);
        pst.setString(3, telpon);
        pst.setString(4, tanggal);
        
        // Menyimpan data
        pst.executeUpdate();
        
        // Menampilkan pesan informasi sukses menggunakan JOptionPane
        JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        
        // Menutup koneksi ke database
        kon.close();
        
        // Memanggil fungsi tampil_buku() untuk me-refresh tabel dengan data baru
        tampil_anggota();
        
        // Memanggil fungsi clear_input() untuk membersihkan field input
        clear_input();
    } catch (Exception e) {
        // Menampilkan pesan informasi kesalahan menggunakan JOptionPane jika terjadi exception
        JOptionPane.showMessageDialog(null, "Menyimpan data Gagal: " + e.getMessage());
    }
}

    
    private void hapus_data() {
    // Mendapatkan indeks baris yang dipilih pada tabel
    int bar = tbl_anggota.getSelectedRow();

    // Memeriksa apakah ada baris yang dipilih
    if (bar != -1) {
        // Menampilkan konfirmasi dialog untuk menghapus data
        int confirm = JOptionPane.showConfirmDialog(
            null, 
            "Apakah Anda yakin ingin menghapus data ini?", 
            "Konfirmasi", 
            JOptionPane.YES_NO_OPTION
        );

        // Memeriksa apakah pengguna menekan tombol Yes pada dialog konfirmasi
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Membuat koneksi ke database
                Connection kon = new koneksi().getConnection();

                // Mengambil ID anggota dari tabel
                String idAnggota = tbl_anggota.getValueAt(bar, 0).toString(); // ID anggota pada kolom pertama

                // Menyusun pernyataan SQL DELETE untuk menghapus data
                String sql = "DELETE FROM anggota WHERE id_anggota=?";
                PreparedStatement pst = kon.prepareStatement(sql);
                pst.setString(1, idAnggota);

                // Mengeksekusi pernyataan SQL DELETE
                pst.executeUpdate();

                // Menampilkan pesan informasi sukses
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus", "Informasi", JOptionPane.INFORMATION_MESSAGE);

                // Bersihkan inputan setelah dihapus
                clear_input();

                // Tampilkan data terbaru di tabel
                tampil_anggota();

                // Menutup koneksi
                kon.close();
            } catch (Exception e) {
                // Menangkap pengecualian dan menampilkan pesan kesalahan
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } else {
        // Menampilkan pesan atau melakukan tindakan lain jika tidak ada baris yang dipilih
        JOptionPane.showMessageDialog(null, "Pilih baris tabel terlebih dahulu", "Peringatan", JOptionPane.WARNING_MESSAGE);
    }
}

    private void edit_data() {
    // Mendapatkan indeks baris yang dipilih pada tabel
    int baris = tbl_anggota.getSelectedRow();
    String telpon = txt_telpon.getText();
    
    if (!telpon.matches("\\d+")) {
        JOptionPane.showMessageDialog(this, "Nomor yang dimasukkan harus angka!", "Input Error", JOptionPane.ERROR_MESSAGE);
        return; // Menghentikan proses simpan jika input salah
    }

    // Memeriksa apakah ada baris yang dipilih
    if (baris != -1) {
        try {
            // Membuat koneksi ke database
            Connection kon = new koneksi().getConnection();

            // Menyusun pernyataan SQL UPDATE untuk mengubah data
            String sql = "UPDATE anggota SET nama_anggota=?, alamat=?, telpon=?, tanggal_bergabung=? WHERE id_anggota=?";
            PreparedStatement pst = kon.prepareStatement(sql);

            // Mengatur nilai parameter pada pernyataan SQL UPDATE
            pst.setString(1, txt_namaAnggota.getText());
            pst.setString(2, txt_alamat.getText());
            pst.setString(3, txt_telpon.getText());
            pst.setString(4, txt_tanggal.getText()); // Pastikan tanggal dalam format yang benar
            pst.setString(5, tbl_anggota.getValueAt(baris, 0).toString()); // ID anggota yang dipilih

            // Mengeksekusi pernyataan SQL UPDATE
            pst.executeUpdate();

            // Menampilkan pesan informasi sukses
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");

            // Me-refresh tabel, membersihkan input, dan menutup koneksi
            tampil_anggota();
            clear_input();
            kon.close();
        } catch (Exception e) {
            // Menampilkan pesan informasi kesalahan jika terjadi exception
            JOptionPane.showMessageDialog(null, "Perubahan data Gagal: " + e.getMessage(), "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    } else {
        // Menampilkan pesan informasi jika tidak ada baris yang dipilih
        JOptionPane.showMessageDialog(null, "Pilih baris data yang akan diubah", "Peringatan", JOptionPane.WARNING_MESSAGE);
    }
}

    
    
    
    public Anggota() {
        initComponents();
        tampil_anggota();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_anggota = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txt_namaAnggota = new javax.swing.JTextField();
        txt_alamat = new javax.swing.JTextField();
        txt_telpon = new javax.swing.JTextField();
        txt_tanggal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Data Anggota");

        tbl_anggota.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "title 5"
            }
        ));
        jScrollPane1.setViewportView(tbl_anggota);

        jLabel2.setText("Nama Anggota");

        txt_namaAnggota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namaAnggotaActionPerformed(evt);
            }
        });

        txt_alamat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_alamatActionPerformed(evt);
            }
        });

        txt_telpon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_telponActionPerformed(evt);
            }
        });

        txt_tanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_tanggalActionPerformed(evt);
            }
        });

        jLabel3.setText("Alamat");

        jLabel4.setText("Telpon");

        jLabel5.setText("Tanggal");

        jButton1.setText("Edit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Simpan");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Hapus");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_namaAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_telpon, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jButton2)
                        .addGap(28, 28, 28)
                        .addComponent(jButton3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(277, 277, 277))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_namaAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_telpon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_namaAnggotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_namaAnggotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_namaAnggotaActionPerformed

    private void txt_alamatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_alamatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_alamatActionPerformed

    private void txt_telponActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telponActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_telponActionPerformed

    private void txt_tanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_tanggalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tanggalActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(validateInput()){
            edit_data();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if(validateInput()){
            simpan_data();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        hapus_data();
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Anggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Anggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Anggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Anggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Anggota().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_anggota;
    private javax.swing.JTextField txt_alamat;
    private javax.swing.JTextField txt_namaAnggota;
    private javax.swing.JTextField txt_tanggal;
    private javax.swing.JTextField txt_telpon;
    // End of variables declaration//GEN-END:variables
}
