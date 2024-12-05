import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS
 */
public class from_buku extends javax.swing.JFrame {

    /**
     * Creates new form from_buku
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
    
    private void tampil_buku(){
        Object[]baris = {"Nama Anggota","Nama Anggota","Alamat","Telpon","Tanggal Bergabung"};
        tabmode= new DefaultTableModel(null, baris);
        anggota.setModel(tabmode);
        String sql = "select * from anggota order by id_anggota asc";
        try {
            Connection kon = new koneksi().getConnection();
            Statement stat = kon.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()){
                String id_anggota = hasil.getString("id_anggota");
                String nama_anggota = hasil.getString("nama_anggota");
                String alamat = hasil.getString("alamat");
                String telpon = hasil.getString("telpon");
                String tanggal = hasil.getString("tanggal_bergabung");
                String[]data = {id_anggota, nama_anggota, alamat, telpon, tanggal};
                tabmode.addRow(data);
            }
            kon.close();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Menampilkan data Gagal: " + e.getMessage());
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

        try {
            // Cek apakah tanggal valid
            dateFormat.parse(tanggal);
        } catch (ParseException e) {
            // Jika tidak valid, tampilkan pesan kesalahan
            JOptionPane.showMessageDialog(null, "Format tanggal tidak valid. Gunakan format yyyy-MM-dd.");
            return;
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
            tampil_buku();

            // Memanggil fungsi clear_input() untuk membersihkan field input
            clear_input();
        } catch (Exception e) {
            // Menampilkan pesan informasi kesalahan menggunakan JOptionPane jika terjadi exception
            JOptionPane.showMessageDialog(null, "Menyimpan data Gagal: " + e.getMessage());
        }
    }


        private void tampil_data_input() {
        // Mendapatkan indeks baris yang dipilih pada tabel
        int baris = anggota.getSelectedRow();
        // Memeriksa apakah ada baris yang dipilih
        if (baris != -1) {
            // Mendapatkan nilai kolom nama_anggota, alamat, telpon, dan tanggal_bergabung dari baris yang dipilih
            String nama_anggota = anggota.getValueAt(baris, 1).toString();  // Kolom nama_anggota
            String alamat = anggota.getValueAt(baris, 2).toString();         // Kolom alamat
            String telpon = anggota.getValueAt(baris, 3).toString();         // Kolom telpon
            // Menampilkan data pada JTextField
            txt_namaAnggota.setText(nama_anggota);
            txt_alamat.setText(alamat);
            txt_telpon.setText(telpon);
    }
}

    
        private void edit_data() {
        // Mendapatkan indeks baris yang dipilih pada tabel
        int baris = anggota.getSelectedRow();

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
                pst.setString(5, anggota.getValueAt(baris, 0).toString()); // ID anggota yang dipilih

                // Mengeksekusi pernyataan SQL UPDATE
                pst.executeUpdate();

                // Menampilkan pesan informasi sukses
                JOptionPane.showMessageDialog(null, "Data berhasil diubah");

                // Me-refresh tabel, membersihkan input, dan menutup koneksi
                tampil_buku();
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

    
    private void hapus_data() {
	// Mendapatkan indeks baris yang dipilih pada tabel
	int bar = anggota.getSelectedRow();
	// Memeriksa apakah ada baris yang dipilih
 	if (bar != -1) {
		// Menampilkan konfirmasi dialog untuk menghapus data
 		int confirm = JOptionPane.showConfirmDialog
 		(null, "Apakah Anda yakin ingin menghapus data ini?",
		"Konfirmasi", JOptionPane.YES_NO_OPTION);
		// Memeriksa apakah pengguna menekan tombol Yes pada dialog konfirmasi
 	if (confirm == JOptionPane.YES_OPTION) {
 		try {
                    // Membuat koneksi ke database
                    Connection kon = new koneksi().getConnection();
                    // Mengambil ID buku dari tabel
                    String idBuku = (String) anggota.getValueAt(bar, 0);
                    // Menyusun pernyataan SQL DELETE untuk menghapus data
                    String sql = "DELETE FROM anggota WHERE id_anggota=?";
                    PreparedStatement pst = kon.prepareStatement(sql);
                    pst.setString(1, idBuku);
                    // Mengeksekusi pernyataan SQL DELETE
                    pst.executeUpdate();
                    // Menampilkan pesan informasi sukses
                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus", "Informasi",
                    JOptionPane.INFORMATION_MESSAGE);
                    // Bersihkan inputan setelah dihapus
                    clear_input();
                    // Tampilkan data terbaru di tabel
                    tampil_buku();
 		} catch (Exception e) {
                    // Menangkap pengecualian dan menampilkan pesan kesalahan
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
 		}
 	}
 	} else {
		// Menampilkan pesan atau melakukan tindakan lain jika tidak ada baris yang dipilih
 		JOptionPane.showMessageDialog(null, "Pilih baris tabel terlebihdahulu", "Peringatan", JOptionPane.WARNING_MESSAGE);
 }
}
        
    public from_buku() {
        initComponents();
        tampil_buku();
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
        anggota = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txt_namaAnggota = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_alamat = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_telpon = new javax.swing.JTextField();
        buton_edit = new javax.swing.JButton();
        buton_simpan = new javax.swing.JButton();
        buton_hapus = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txt_tanggal = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Data Buku");

        anggota.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id_anggota", "Nama anggota", "alamat", "telpon", "tanggal bergabung"
            }
        ));
        jScrollPane1.setViewportView(anggota);

        jLabel2.setText("Nama Anggota");

        txt_namaAnggota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namaAnggotaActionPerformed(evt);
            }
        });

        jLabel3.setText("Alamat");

        txt_alamat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_alamatActionPerformed(evt);
            }
        });

        jLabel4.setText("Telpon");

        txt_telpon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_telponActionPerformed(evt);
            }
        });

        buton_edit.setText("Edit");
        buton_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buton_editActionPerformed(evt);
            }
        });

        buton_simpan.setText("Simpan");
        buton_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buton_simpanActionPerformed(evt);
            }
        });

        buton_hapus.setText("Hapus");
        buton_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buton_hapusActionPerformed(evt);
            }
        });

        jLabel5.setText("Tanggal_bergabung");

        txt_tanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_tanggalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(216, 216, 216)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_telpon, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(buton_edit)
                            .addComponent(jLabel2))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(buton_simpan)
                                .addGap(42, 42, 42)
                                .addComponent(buton_hapus))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_namaAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(285, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_namaAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txt_telpon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(txt_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buton_edit)
                    .addComponent(buton_simpan)
                    .addComponent(buton_hapus))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void buton_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buton_editActionPerformed
        // TODO add your handling code here:
        if (!validateInput()) {
            return;  // Menghentikan jika input tidak valid
        }
        edit_data();
    }//GEN-LAST:event_buton_editActionPerformed

    private void buton_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buton_simpanActionPerformed
        // TODO add your handling code here:
        //@Override
        if (!validateInput()) {
            return;  // Menghentikan jika input tidak valid
        }
        simpan_data();
        
        
    }//GEN-LAST:event_buton_simpanActionPerformed

    private void buton_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buton_hapusActionPerformed
        // TODO add your handling code here:
        hapus_data();
    }//GEN-LAST:event_buton_hapusActionPerformed

    private void txt_tanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_tanggalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tanggalActionPerformed

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
            java.util.logging.Logger.getLogger(from_buku.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(from_buku.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(from_buku.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(from_buku.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new from_buku().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable anggota;
    private javax.swing.JButton buton_edit;
    private javax.swing.JButton buton_hapus;
    private javax.swing.JButton buton_simpan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txt_alamat;
    private javax.swing.JTextField txt_namaAnggota;
    private javax.swing.JTextField txt_tanggal;
    private javax.swing.JTextField txt_telpon;
    // End of variables declaration//GEN-END:variables
}
