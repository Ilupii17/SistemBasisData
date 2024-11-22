import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    txt_name.setText("");
    txt_pengarang.setText("");
    txt_penerbit.setText("");
}
    private boolean validateInput() {
    if (txt_name.getText().isEmpty() || txt_pengarang.getText().isEmpty() || txt_penerbit.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return false;
    }
    return true;
}   
    
    private void tampil_buku(){
        Object[]baris = {"Kode Buku","Nama Buku","Pengarang","Penerbit"};
        tabmode= new DefaultTableModel(null, baris);
        tbl_buku.setModel(tabmode);
        String sql = "select * from tbl_buku order by id_buku asc";
        try {
            Connection kon = new koneksi().getConnection();
            Statement stat = kon.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()){
                String id_buku = hasil.getString("id_buku");
                String nama_buku = hasil.getString("nama_buku");
                String pengarang = hasil.getString("pengarang");
                String penerbit = hasil.getString("penerbit");
                String[]data = {id_buku, nama_buku, pengarang, penerbit};
                tabmode.addRow(data);
            }
            kon.close();
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Menampilkan data Gagal","Informasi",JOptionPane.INFORMATION_MESSAGE);
        }
    }
        
    private void simpan_data() {
        //if (!validateInput()) return;
        // Mengambil nilai input dari field teks nama buku
        String nama_buku = txt_name.getText();
        // Mengambil nilai input dari field teks pengarang
        String pengarang = txt_pengarang.getText();
        // Mengambil nilai input dari field teks penerbit
        String penerbit = txt_penerbit.getText();
        try {
            // Membuat koneksi ke database
            Connection kon = new koneksi().getConnection();
            // Membuat objek Statement untuk mengeksekusi pernyataan SQL
            Statement stat = kon.createStatement();
             // Menyusun pernyataan SQL INSERT untuk menyimpan data baru kedalam tabel
            String sql = "INSERT INTO tbl_buku (nama_buku, pengarang,penerbit) "+ "VALUES ('" + nama_buku + "', '" + pengarang + "','" + penerbit + "')";
            // Mengeksekusi pernyataan SQL INSERT
            stat.executeUpdate(sql);
            // Menampilkan pesan informasi sukses menggunakan JOptionPane
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan","Informasi", JOptionPane.INFORMATION_MESSAGE);
            // Menutup koneksi ke database
             kon.close();
            // Memanggil fungsi tampil_buku() untuk me-refresh tabel dengan data baru
             tampil_buku();
            // Memanggil fungsi clear_input() untuk membersihkan field input
             clear_input();
         } catch (Exception e) {
        // Menampilkan pesan informasi kesalahan menggunakan JOptionPane jikater jadi exception
        JOptionPane.showMessageDialog(null, "Menyimpan data Gagal: " + e.getMessage());
        }
    }
    
    private void tampil_data_input() {
        // Mendapatkan indeks baris yang dipilih pada tabel
        int baris = tbl_buku.getSelectedRow();
        // Memeriksa apakah ada baris yang dipilih
        if (baris != -1) {
        // Mendapatkan nilai kolom nama_buku, pengarang, dan penerbit dari barisyang dipilih
        String nama_buku = tbl_buku.getValueAt(baris, 1).toString();
        String pengarang = tbl_buku.getValueAt(baris, 2).toString();
        String penerbit = tbl_buku.getValueAt(baris, 3).toString();
        // Menampilkan data pada JTextField
        txt_name.setText(nama_buku);
        txt_pengarang.setText(pengarang);
        txt_penerbit.setText(penerbit);
        }
    }
    
        private void edit_data() {
        //if (!validateInput()) return;
        // Mendapatkan indeks baris yang dipilih pada tabel
        int baris = tbl_buku.getSelectedRow();
        // Memeriksa apakah ada baris yang dipilih
        if (baris != -1) {
            try {
            // Membuat koneksi ke database
                    Connection kon = new koneksi().getConnection();
                    // Menyusun pernyataan SQL UPDATE untuk mengubah data
                    String sql = "update tbl_buku set nama_buku=?, pengarang=?,penerbit=? where id_buku=?";
                    PreparedStatement pst = kon.prepareStatement(sql);
                    // Mengatur nilai parameter pada pernyataan SQL UPDATE
                    pst.setString(1, txt_name.getText());
                    pst.setString(2, txt_pengarang.getText());
                    pst.setString(3, txt_penerbit.getText());
                    pst.setString(4, tbl_buku.getValueAt(baris,0).toString());
                    // Mengeksekusi pernyataan SQL UPDATE
                    pst.executeUpdate();
                    // Menampilkan pesan informasi sukses
                    JOptionPane.showMessageDialog(null, "Data berhasil diubah");
                    //Me-refresh tabel, membersihkan input, dan menutup koneksi
                    tampil_buku();
                    clear_input();
                    kon.close();
                } catch (Exception e) {
                    // Menampilkan pesan informasi kesalahan jika terjadi exception
                    JOptionPane.showMessageDialog(null, "Perubahan data Gagal","Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Menampilkan pesan informasi jika tidak ada baris yang dipilih
            JOptionPane.showMessageDialog(null, "Pilih baris data yang akandiubah");
            }
           }
    
    private void hapus_data() {
	// Mendapatkan indeks baris yang dipilih pada tabel
	int bar = tbl_buku.getSelectedRow();
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
                    String idBuku = (String) tbl_buku.getValueAt(bar, 0);
                    // Menyusun pernyataan SQL DELETE untuk menghapus data
                    String sql = "DELETE FROM tbl_buku WHERE id_buku=?";
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
                    JOptionPane.showMessageDialog
                    (null, "Error: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
 		}
 	}
 	} else {
		// Menampilkan pesan atau melakukan tindakan lain jika tidak ada baris yang dipilih
 		JOptionPane.showMessageDialog(null, "Pilih baris tabel terlebihdahulu", "Peringatan", JOptionPane.WARNING_MESSAGE);
 }
}
        
    public Anggota() {
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
        tbl_buku = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txt_name = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_pengarang = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_penerbit = new javax.swing.JTextField();
        buton_edit = new javax.swing.JButton();
        buton_simpan = new javax.swing.JButton();
        buton_hapus = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Data Buku");

        tbl_buku.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "id_buku", "Nama Buku", "Pengarang", "Penerbit"
            }
        ));
        jScrollPane1.setViewportView(tbl_buku);

        jLabel2.setText("Nama Buku");

        txt_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nameActionPerformed(evt);
            }
        });

        jLabel3.setText("Pengarang");

        txt_pengarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_pengarangActionPerformed(evt);
            }
        });

        jLabel4.setText("Penerbit");

        txt_penerbit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_penerbitActionPerformed(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(216, 216, 216)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_penerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_pengarang, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(74, 74, 74)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(53, 53, 53)
                                        .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(253, 253, 253)
                        .addComponent(buton_edit)
                        .addGap(46, 46, 46)
                        .addComponent(buton_simpan)
                        .addGap(42, 42, 42)
                        .addComponent(buton_hapus)))
                .addContainerGap(286, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_pengarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txt_penerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buton_edit)
                    .addComponent(buton_simpan)
                    .addComponent(buton_hapus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nameActionPerformed

    private void txt_pengarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_pengarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pengarangActionPerformed

    private void txt_penerbitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_penerbitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_penerbitActionPerformed

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
    private javax.swing.JButton buton_edit;
    private javax.swing.JButton buton_hapus;
    private javax.swing.JButton buton_simpan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_buku;
    private javax.swing.JTextField txt_name;
    private javax.swing.JTextField txt_penerbit;
    private javax.swing.JTextField txt_pengarang;
    // End of variables declaration//GEN-END:variables
}
