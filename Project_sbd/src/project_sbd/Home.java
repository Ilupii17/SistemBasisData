/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project_sbd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Hp
 */
public class Home extends javax.swing.JFrame {

    /**
     * Creates new form Home
     */
    
    private int hargaDasar;  // Variabel kelas untuk menyimpan harga dasar
    private void loadFilms() {
    try {
        // Koneksi ke database
        Connection kon = koneksi.getConnection();

        // Query untuk mendapatkan data film
        String sql = "SELECT film_name FROM films";
        PreparedStatement pst = kon.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        // Membersihkan item yang ada di ComboBox sebelum menambahkan
        filmComboBox.removeAllItems();
        filmComboBox.addItem("-- Pilih Film --"); // Tambahkan item default

        // Loop untuk menambahkan film ke ComboBox
        while (rs.next()) {
            filmComboBox.addItem(rs.getString("film_name"));
        }

        // Tutup koneksi
        kon.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
   
    private void hitungTotal() {
    String filmTerpilih = (String) filmComboBox.getSelectedItem();

    if (filmTerpilih == null || filmTerpilih.equals("-- Pilih Film --")) {
        JOptionPane.showMessageDialog(this, "Pilih film terlebih dahulu.");
        return;
    }

    try (Connection conn = koneksi.getConnection()) {
        String query = "SELECT price FROM films WHERE film_name = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, filmTerpilih);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            hargaDasar = rs.getInt("price");
        } else {
            JOptionPane.showMessageDialog(this, "Film tidak ditemukan dalam database.");
            return;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengakses data film: " + e.getMessage());
        e.printStackTrace();
    }

    // Menambahkan biaya berdasarkan pilihan jenis tiket
    if (regularButton.isSelected()) {
        hargaDasar += 15000;
    } else if (ultraButton.isSelected()) {
        hargaDasar += 25000;
    } else if (goldButton.isSelected()) {
        hargaDasar += 40000;
    }

    // Tampilkan harga dasar pada field
    hargaField.setText(String.valueOf(hargaDasar));

    // Update total bayar secara otomatis
    updateTotalBayar();
}

    
    private void updateTotalBayar() {
    try {
        // Mengambil jumlah tiket dari input di jumlahTiketField
        int jumlahBeli = Integer.parseInt(jumlahTiketField.getText().trim());

        if (jumlahBeli <= 0) {
            JOptionPane.showMessageDialog(this, "Jumlah tiket harus lebih dari 0.");
            return;
        }

        // Menggunakan harga dasar yang sudah dihitung
        int totalBayar = jumlahBeli * hargaDasar;

        // Menampilkan total bayar pada text field totalField
        totalField.setText(String.valueOf(totalBayar));
    } catch (NumberFormatException e) {
        // Jika input bukan angka, kosongkan total bayar dan beri pesan
        totalField.setText("");
        //JOptionPane.showMessageDialog(this, "Jumlah tiket harus berupa angka valid.");
    }
}


   private void printData() {
    // Mengambil data dari input form
    String namaPembeli = namaPembeliField.getText().trim();
    String jumlahTiketText = jumlahTiketField.getText().trim();
    String pembayaranText = pembayaranField.getText().trim();
    
    if (namaPembeli.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nama pembeli harus diisi.");
        return;
    }

    int jumlahTiket = 0;
    try {
        jumlahTiket = Integer.parseInt(jumlahTiketText);
        if (jumlahTiket <= 0) {
            JOptionPane.showMessageDialog(this, "Jumlah tiket harus lebih dari 0.");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Jumlah tiket harus berupa angka.");
        return;
    }

    int pembayaran = 0;
    try {
        pembayaran = Integer.parseInt(pembayaranText);
        if (pembayaran < 0) {
            JOptionPane.showMessageDialog(this, "Pembayaran tidak bisa kurang dari 0.");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Pembayaran harus berupa angka.");
        return;
    }

    int totalBayar = Integer.parseInt(totalField.getText().trim());
    if (pembayaran < totalBayar) {
        //JOptionPane.showMessageDialog(this, "Pembayaran tidak cukup.");
        return;
    }
    
    int kembalian = pembayaran - totalBayar;

    // Menyimpan data transaksi ke database
    try (Connection conn = koneksi.getConnection()) {
        String query = "INSERT INTO transactions (buyer_name, ticket_count, total_price, payment, kembali) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, namaPembeli);
        stmt.setInt(2, jumlahTiket);
        stmt.setInt(3, totalBayar);
        stmt.setInt(4, pembayaran);
        stmt.setInt(5, kembalian);
        stmt.executeUpdate();

        JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan transaksi: " + e.getMessage());
        e.printStackTrace();
    }

    // Menampilkan data transaksi di JTextArea
    try (Connection conn = koneksi.getConnection()) {
        String query = "SELECT * FROM transactions ORDER BY id DESC LIMIT 1"; // Menampilkan 1 transaksi terbaru
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        StringBuilder sb = new StringBuilder();
        sb.append("Transaksi\n");
        while (rs.next()) {
            sb.append("Nama Pembeli: ").append(rs.getString("buyer_name")).append("\n");
            sb.append("Jumlah Tiket: ").append(rs.getInt("ticket_count")).append("\n");
            sb.append("Total Bayar: ").append(rs.getInt("total_price")).append("\n");
            sb.append("Pembayaran: ").append(rs.getInt("payment")).append("\n");
            sb.append("Kembalian: ").append(rs.getInt("kembali")).append("\n");
            sb.append("-----------------------------\n");
        }
        jTextArea1.setText(sb.toString()); // Menampilkan hasil query di JTextArea
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menampilkan transaksi: " + e.getMessage());
        e.printStackTrace();
    }
}


    
    
    private void setupRadioButtons() {
        // Membuat ButtonGroup untuk radio button
        ButtonGroup group = new ButtonGroup();

        // Menambahkan radio button ke grup
        group.add(regularButton);
        group.add(ultraButton);
        group.add(goldButton);
        regularButton.addActionListener(evt -> hitungTotal());
        ultraButton.addActionListener(evt -> hitungTotal());
        goldButton.addActionListener(evt -> hitungTotal());
       
}
    
    private void hitungKembalian() {
    try {
        int totalBayar = Integer.parseInt(totalField.getText().trim()); // Ambil total bayar
        int pembayaran = Integer.parseInt(pembayaranField.getText().trim()); // Ambil pembayaran
        int kembalian = pembayaran - totalBayar; // Hitung kembalian
        kembalianField.setText(String.valueOf(kembalian)); // Tampilkan kembalian di field kembalian
    } catch (NumberFormatException e) {
        // Jika input bukan angka, kosongkan field kembalian
        kembalianField.setText("");
        JOptionPane.showMessageDialog(this, "Pembayaran atau total bayar tidak valid.");
    }
}

    
    public Home() {
        initComponents();
        loadFilms();
        setupRadioButtons();
        
        pembayaranField.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            hitungKembalian();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            hitungKembalian();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            hitungKembalian();
        }
    });
        
        // Menambahkan DocumentListener untuk meng-update total bayar secara otomatis saat jumlah tiket berubah
        jumlahTiketField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateTotalBayar();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateTotalBayar();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateTotalBayar();
            }
        });
        
        

    }
    
     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        filmComboBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        goldButton = new javax.swing.JRadioButton();
        regularButton = new javax.swing.JRadioButton();
        ultraButton = new javax.swing.JRadioButton();
        jumlahTiketField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        hargaField = new javax.swing.JTextField();
        totalField = new javax.swing.JTextField();
        pembayaranField = new javax.swing.JTextField();
        kembalianField = new javax.swing.JTextField();
        namaPembeliField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setBackground(new java.awt.Color(255, 102, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Kembalian");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));

        jLabel2.setBackground(new java.awt.Color(255, 102, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("Penjualan Tiket Bioskop");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, -1, -1));

        getContentPane().add(filmComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, -1, -1));

        jLabel4.setBackground(new java.awt.Color(255, 102, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Film");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        goldButton.setText("Gold");
        goldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goldButtonActionPerformed(evt);
            }
        });
        getContentPane().add(goldButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 110, -1, -1));

        regularButton.setText("Reguler");
        regularButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regularButtonActionPerformed(evt);
            }
        });
        getContentPane().add(regularButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, -1, -1));

        ultraButton.setText("Ultra XD");
        ultraButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ultraButtonActionPerformed(evt);
            }
        });
        getContentPane().add(ultraButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, -1, -1));
        getContentPane().add(jumlahTiketField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, 90, -1));

        jLabel7.setBackground(new java.awt.Color(255, 102, 255));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Harga Satuan");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jLabel6.setBackground(new java.awt.Color(255, 102, 255));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Jenis Tiket");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        jLabel9.setBackground(new java.awt.Color(255, 102, 255));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Nama Pembeli");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        jLabel8.setBackground(new java.awt.Color(255, 102, 255));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Data Pembelian");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, -1, -1));

        jLabel10.setBackground(new java.awt.Color(255, 102, 255));
        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Jumlah Tiket");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        jLabel11.setBackground(new java.awt.Color(255, 102, 255));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        jLabel12.setBackground(new java.awt.Color(255, 102, 255));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Total Bayar");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        jLabel13.setBackground(new java.awt.Color(255, 102, 255));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Cetak");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 90, -1, -1));
        getContentPane().add(hargaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 100, -1));

        totalField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalFieldActionPerformed(evt);
            }
        });
        getContentPane().add(totalField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 90, -1));
        getContentPane().add(pembayaranField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, 90, -1));
        getContentPane().add(kembalianField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 320, 90, -1));
        getContentPane().add(namaPembeliField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 90, -1));

        jLabel14.setBackground(new java.awt.Color(255, 102, 255));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Pembayaran");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        jButton1.setText("exit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 360, 90, -1));

        jButton2.setText("input data");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, -1));

        jButton3.setText("Print data");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 360, 90, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 110, 300, 230));

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\Hp\\Downloads\\loginsuuuu.jpg")); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goldButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goldButtonActionPerformed
        // TODO add your handling code here:
        //hitungTotal();
    }//GEN-LAST:event_goldButtonActionPerformed

    private void regularButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regularButtonActionPerformed
        // TODO add your handling code here:
        //hitungTotal();
    }//GEN-LAST:event_regularButtonActionPerformed

    private void ultraButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ultraButtonActionPerformed
        // TODO add your handling code here:
        //hitungTotal();
    }//GEN-LAST:event_ultraButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        printData();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void totalFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalFieldActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_totalFieldActionPerformed

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
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox filmComboBox;
    private javax.swing.JRadioButton goldButton;
    private javax.swing.JTextField hargaField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jumlahTiketField;
    private javax.swing.JTextField kembalianField;
    private javax.swing.JTextField namaPembeliField;
    private javax.swing.JTextField pembayaranField;
    private javax.swing.JRadioButton regularButton;
    private javax.swing.JTextField totalField;
    private javax.swing.JRadioButton ultraButton;
    // End of variables declaration//GEN-END:variables
}
