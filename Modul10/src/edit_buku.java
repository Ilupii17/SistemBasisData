
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class edit_buku extends javax.swing.JFrame {
    private DefaultTableModel tabmode;

    /**
     * Creates new form edit_buku
     */
    
    private void clear_input() {
    JnamaBuku.setText("");
    JPengarang.setText("");
    JPenerbit.setText("");
}

    private boolean validateInput() {
    // Validasi jika ada field yang kosong
    if (JnamaBuku.getText().isEmpty() || JPengarang.getText().isEmpty() || JPenerbit.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return false;
    }
    return true;
}
    
    private void tampil_buku() {
    Object[] baris = {"Kode Buku", "Nama Buku", "Pengarang", "Penerbit"};
      tabmode = new DefaultTableModel(null, baris);
      tbl_buku.setModel(tabmode);

      String sql = "SELECT * FROM tbl_buku ORDER BY id_buku ASC";

      try {
        Connection kon = new koneksi().getConnection(); // Membuka koneksi
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

    
    

    private void simpan_data() {
        if (!validateInput()) return;

        try (Connection kon = new koneksi().getConnection()) {
            String sql = "INSERT INTO tbl_buku (nama_buku, pengarang, penerbit) VALUES (?, ?, ?)";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, JnamaBuku.getText());
            pst.setString(2, JPengarang.getText());
            pst.setString(3, JPenerbit.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            tampil_buku();
            clear_input();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Menyimpan data gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void hapus_data() {
        int bar = tbl_buku.getSelectedRow();
        if (bar == -1) {
            JOptionPane.showMessageDialog(null, "Pilih baris tabel terlebih dahulu", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection kon = new koneksi().getConnection()) {
                String id_buku = tbl_buku.getValueAt(bar, 0).toString();
                String sql = "DELETE FROM tbl_buku WHERE id_buku=?";
                PreparedStatement pst = kon.prepareStatement(sql);
                pst.setString(1, id_buku);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                tampil_buku();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


   private void edit_data() {
        // Mendapatkan indeks baris yang dipilih pada tabel
        int baris = tbl_buku.getSelectedRow();

        // Memeriksa apakah ada baris yang dipilih
        if (baris != -1) {
            try {
                // Membuat koneksi ke database
                Connection kon = new koneksi().getConnection();

                // Menyusun pernyataan SQL UPDATE untuk mengubah data buku
                String sql = "UPDATE tbl_buku SET nama_buku=?, pengarang=?, penerbit=? WHERE id_buku=?";
                PreparedStatement pst = kon.prepareStatement(sql);

                // Mengatur nilai parameter pada pernyataan SQL UPDATE
                pst.setString(1, JnamaBuku.getText());
                pst.setString(2, JPengarang.getText());
                pst.setString(3, JPenerbit.getText());
                pst.setString(4, tbl_buku.getValueAt(baris, 0).toString()); // ID buku yang dipilih

                // Mengeksekusi pernyataan SQL UPDATE
                pst.executeUpdate();

                // Menampilkan pesan informasi sukses
                JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Informasi", JOptionPane.INFORMATION_MESSAGE);

                // Me-refresh tabel, membersihkan input, dan menutup koneksi
                tampil_buku();
                clear_input();
                kon.close();
            } catch (Exception e) {
                // Menampilkan pesan informasi kesalahan jika terjadi exception
                JOptionPane.showMessageDialog(null, "Perubahan data Gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Menampilkan pesan informasi jika tidak ada baris yang dipilih
            JOptionPane.showMessageDialog(null, "Pilih baris data yang akan diubah", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    
    
    public edit_buku() {
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        JnamaBuku = new javax.swing.JTextField();
        JPengarang = new javax.swing.JTextField();
        JPenerbit = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_buku = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Data Buku");

        jLabel4.setText("Penerbit");

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

        jLabel2.setText("Nama Buku");

        JnamaBuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JnamaBukuActionPerformed(evt);
            }
        });

        JPengarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JPengarangActionPerformed(evt);
            }
        });

        JPenerbit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JPenerbitActionPerformed(evt);
            }
        });

        jLabel3.setText("Pengarang");

        tbl_buku.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "id buku", "Nama Buku", "Pengarang", "Penerbit"
            }
        ));
        jScrollPane2.setViewportView(tbl_buku);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(212, 212, 212)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jButton2)
                        .addGap(28, 28, 28)
                        .addComponent(jButton3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(226, 226, 226)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JPenerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JPengarang, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JnamaBuku, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(284, 284, 284)
                        .addComponent(jLabel1)))
                .addContainerGap(241, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JnamaBuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JPengarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JPenerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JPenerbitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JPenerbitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JPenerbitActionPerformed

    private void JPengarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JPengarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JPengarangActionPerformed

    private void JnamaBukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JnamaBukuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JnamaBukuActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        hapus_data();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if(validateInput()){
            simpan_data();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(validateInput()){
            edit_data();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(edit_buku.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(edit_buku.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(edit_buku.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(edit_buku.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new edit_buku().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField JPenerbit;
    private javax.swing.JTextField JPengarang;
    private javax.swing.JTextField JnamaBuku;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tbl_buku;
    // End of variables declaration//GEN-END:variables
}
