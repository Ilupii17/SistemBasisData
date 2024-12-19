
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
public class form_pinjaman extends javax.swing.JFrame {

    /**
     * Creates new form form_pinjaman
     */
    
    private void hapus_data() {
    // Mengambil nilai id_peminjaman dari form input
    String id_peminjaman = txt_pinjam.getText();

    // Validasi apakah id_peminjaman sudah dipilih
    if (id_peminjaman.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus terlebih dahulu.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        }
    int konfirmasi = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
    if (konfirmasi == JOptionPane.YES_OPTION) {
        try {
            // Koneksi ke database
            Connection kon = new koneksi().getConnection();
            String sql = "DELETE FROM tbl_peminjaman WHERE id_peminjaman = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, id_peminjaman);

            // Eksekusi delete
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            kon.close();
            tampil_data_peminjaman(); // Refresh tabel
            clear_input(); // Reset form input
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Menghapus data Gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    
    // Fungsi untuk mengisi ComboBox Anggota
    private void combobox_anggota() {
        try {
            // Koneksi ke database
            Connection kon = new koneksi().getConnection();

            // Query untuk mendapatkan data nama anggota
            String sql = "SELECT id_anggota, nama_anggota FROM anggota";
            PreparedStatement pst = kon.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            // Membersihkan item yang ada di ComboBox sebelum menambahkan
            txt_anggota.removeAllItems();
            txt_anggota.addItem("-- Pilih Nama Anggota --"); // Tambahkan item default

            // Loop untuk menambahkan nama anggota ke ComboBox
            while (rs.next()) {
                txt_anggota.addItem(rs.getString("nama_anggota"));
            }

            // Tutup koneksi
            kon.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Memuat Data Anggota: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Fungsi untuk mengisi ComboBox Buku
    private void combobox_buku() {
        try {
            // Koneksi ke database
            Connection kon = new koneksi().getConnection();

            // Query untuk mendapatkan data nama buku
            String sql = "SELECT id_buku, nama_buku FROM tbl_buku";
            PreparedStatement pst = kon.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            // Membersihkan item yang ada di ComboBox sebelum menambahkan
            txt_buku.removeAllItems();
            txt_buku.addItem("-- Pilih Nama Buku --"); // Tambahkan item default

            // Loop untuk menambahkan nama buku ke ComboBox
            while (rs.next()) {
                txt_buku.addItem(rs.getString("nama_buku"));
            }

            // Tutup koneksi
            kon.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Memuat Data Buku: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     // Fungsi untuk mengambil ID Anggota berdasarkan nama yang dipilih
    private String getIdAnggota(String nama_anggota) {
        String id_anggota = null;
        try {
            Connection kon = new koneksi().getConnection();
            String sql = "SELECT id_anggota FROM anggota WHERE nama_anggota = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, nama_anggota);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                id_anggota = rs.getString("id_anggota");
            }

            kon.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mendapatkan ID Anggota: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return id_anggota;
    }
    
    // Fungsi untuk mengambil ID Buku berdasarkan nama yang dipilih
        private String getIdBuku(String nama_buku) {
            String id_buku = null;
            try {
                Connection kon = new koneksi().getConnection();
                String sql = "SELECT id_buku FROM tbl_buku WHERE nama_buku = ?";
                PreparedStatement pst = kon.prepareStatement(sql);
                pst.setString(1, nama_buku);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    id_buku = rs.getString("id_buku");
                }

                kon.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal mendapatkan ID Buku: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return id_buku;
        }
        
    private void simpan_data() {
        // Mengambil nilai dari form input
        String id_peminjaman = txt_pinjam.getText();
        String nama_anggota = txt_anggota.getSelectedItem().toString();
        String nama_buku = txt_buku.getSelectedItem().toString();
        String tanggal_peminjaman = txt_tglpinjam.getText();
        String tanggal_pengembalian = txt_tglkembali.getText();
        String status = txt_status.getSelectedItem().toString();

        String id_anggota = getIdAnggota(nama_anggota);
        String id_buku = getIdBuku(nama_buku);

        if (id_anggota == null || id_buku == null) {
            JOptionPane.showMessageDialog(this, "ID Anggota atau ID Buku tidak ditemukan. Pastikan data valid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
        // Koneksi ke database
        Connection kon = new koneksi().getConnection();
        String sql = "INSERT INTO tbl_peminjaman (id_peminjaman, id_anggota, id_buku, tanggal_peminjaman, tanggal_pengembalian, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = kon.prepareStatement(sql);
        pst.setString(1, id_peminjaman);
        pst.setString(2, id_anggota);
        pst.setString(3, id_buku);
        pst.setString(4, tanggal_peminjaman);
        pst.setString(5, tanggal_pengembalian);
        pst.setString(6, status);

        pst.executeUpdate();

        JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        kon.close();
        tampil_data_peminjaman(); // Refresh tabel
        clear_input(); // Reset form input
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Menyimpan data Gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    
    private void clear_input() {
        txt_pinjam.setText("");
        txt_anggota.setSelectedIndex(0);
        txt_buku.setSelectedIndex(0);
        txt_tglpinjam.setText("");
        txt_tglkembali.setText("");
        txt_status.setSelectedIndex(0);
    }
    
    private DefaultTableModel tabmode;
        private void tampil_data_peminjaman(){
            Object[]baris   = {"Id Peminjaman","Nama Anggota","Nama Buku","Tanggal Peminjaman","Tanggal Kembali","Status"};
            tabmode= new DefaultTableModel(null, baris);
            tbl_peminjaman.setModel(tabmode);
            String sql = 
                "SELECT p.id_peminjaman, a.nama_anggota, b.nama_buku, p.tanggal_peminjaman, p.tanggal_pengembalian, p.status "
                + "FROM tbl_peminjaman p "
                + "JOIN anggota a ON p.id_anggota = a.id_anggota "
                + "JOIN tbl_buku b ON p.id_buku = b.id_buku "
                + "ORDER BY p.id_peminjaman ASC";

            try {
               Connection kon = new koneksi().getConnection();
               Statement stat = kon.createStatement();
               ResultSet hasil = stat.executeQuery(sql);
               while (hasil.next()){
                String id_peminjaman = hasil.getString("id_peminjaman");
                String nama_anggota =  hasil.getString("nama_anggota");
                String nama_buku =  hasil.getString("nama_buku");
                String tanggal_peminjaman =  hasil.getString("tanggal_peminjaman");
                String tanggal_pengembalian =  hasil.getString("tanggal_pengembalian");
                String status =  hasil.getString("status");
                   String[]data = {id_peminjaman, nama_anggota, nama_buku, tanggal_peminjaman, tanggal_pengembalian, status};
                   tabmode.addRow(data);
               }
               kon.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Menampilkan data Gagal","Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
    private void tampil_data_input() {
            int baris = tbl_peminjaman.getSelectedRow();
            if (baris != -1) {
                String id_peminjaman = tbl_peminjaman.getValueAt(baris, 0).toString();
                String nama_anggota = tbl_peminjaman.getValueAt(baris, 1).toString();
                String nama_buku = tbl_peminjaman.getValueAt(baris, 2).toString();
                String tanggal_peminjaman = tbl_peminjaman.getValueAt(baris, 3).toString();
                String tanggal_pengembalian = tbl_peminjaman.getValueAt(baris, 4).toString();
                String status = tbl_peminjaman.getValueAt(baris, 5).toString();

                // Menampilkan data pada JTextField
             
                txt_pinjam.setText(id_peminjaman);
                txt_anggota.setSelectedItem(nama_anggota); // ComboBox untuk nama anggota
                txt_buku.setSelectedItem(nama_buku);      // ComboBox untuk nama buku
                txt_tglpinjam.setText(tanggal_peminjaman);
                txt_tglkembali.setText(tanggal_pengembalian);
                txt_status.setSelectedItem(status);       // ComboBox untuk status
            }
        }
    
    private void edit_data() {
    int baris = tbl_peminjaman.getSelectedRow(); // Mendapatkan baris yang dipilih
    if (baris != -1) {
        try {
            Connection kon = new koneksi().getConnection(); // Koneksi ke database
            String sql = "UPDATE tbl_peminjaman SET id_anggota = ?, id_buku = ?, tanggal_peminjaman = ?, tanggal_pengembalian = ?, status = ? "
                       + "WHERE id_peminjaman = ?";
            PreparedStatement pst = kon.prepareStatement(sql);

            // Mengambil data dari form input
            String nama_anggota = txt_anggota.getSelectedItem().toString();
            String nama_buku = txt_buku.getSelectedItem().toString();
            String tanggal_peminjaman = txt_tglpinjam.getText();
            String tanggal_pengembalian = txt_tglkembali.getText();
            String status = txt_status.getSelectedItem().toString();

            // Mendapatkan ID anggota dan buku dari nama yang dipilih
            String id_anggota = getIdAnggota(nama_anggota);
            String id_buku = getIdBuku(nama_buku);

            // Validasi jika ID anggota atau buku tidak ditemukan
            if (id_anggota == null || id_buku == null) {
                JOptionPane.showMessageDialog(this, "ID Anggota atau ID Buku tidak ditemukan. Pastikan data valid.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Set parameter untuk query SQL
            pst.setString(1, id_anggota); // ID Anggota
            pst.setString(2, id_buku);   // ID Buku
            pst.setString(3, tanggal_peminjaman); // Tanggal Peminjaman
            pst.setString(4, tanggal_pengembalian); // Tanggal Pengembalian
            pst.setString(5, status);   // Status
            pst.setString(6, tbl_peminjaman.getValueAt(baris, 0).toString()); // ID Peminjaman dari tabel

            // Eksekusi query
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");

            // Refresh tabel dan reset form
            tampil_data_peminjaman(); // Fungsi untuk menampilkan data di tabel
            clear_input();           // Fungsi untuk membersihkan input form

            kon.close(); // Tutup koneksi
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Perubahan data Gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih baris data yang akan diubah");
        }
    }
    
    public form_pinjaman() {
        initComponents();
        combobox_anggota();
        combobox_buku();
        tampil_data_peminjaman();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_pinjam = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_anggota = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        txt_buku = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txt_tglpinjam = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_tglkembali = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_status = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_peminjaman = new javax.swing.JTable();
        Jedit = new javax.swing.JButton();
        Jsimpan = new javax.swing.JButton();
        jHapus = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Data Pinjaman");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("ID Pinjaman");

        txt_pinjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_pinjamActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Nama Anggota");

        txt_anggota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_anggotaActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Nama Buku");

        txt_buku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_bukuActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Tanggal Pinjaman");

        txt_tglpinjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_tglpinjamActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Tanggal Kembali");

        txt_tglkembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_tglkembaliActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Status");

        txt_status.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dipinjam", "Dikembalikan" }));
        txt_status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_statusActionPerformed(evt);
            }
        });

        tbl_peminjaman.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tbl_peminjaman);

        Jedit.setText("Edit");
        Jedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JeditActionPerformed(evt);
            }
        });

        Jsimpan.setText("Simpan");
        Jsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JsimpanActionPerformed(evt);
            }
        });

        jHapus.setText("Hapus");
        jHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txt_tglpinjam, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txt_buku, javax.swing.GroupLayout.Alignment.LEADING, 0, 156, Short.MAX_VALUE)
                        .addComponent(txt_anggota, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_pinjam, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txt_status, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_tglkembali, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)))
                .addGap(274, 274, 274))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(270, 270, 270)
                .addComponent(Jedit)
                .addGap(51, 51, 51)
                .addComponent(jHapus)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(155, 155, 155)
                    .addComponent(Jsimpan)
                    .addContainerGap(505, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_pinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_anggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_buku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_tglpinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(txt_tglkembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txt_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Jedit)
                    .addComponent(jHapus))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(295, Short.MAX_VALUE)
                    .addComponent(Jsimpan)
                    .addGap(279, 279, 279)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_pinjamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_pinjamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pinjamActionPerformed

    private void txt_anggotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_anggotaActionPerformed
        // TODO add your handling code here:
        //combobox_anggota();
    }//GEN-LAST:event_txt_anggotaActionPerformed

    private void txt_bukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_bukuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_bukuActionPerformed

    private void txt_tglpinjamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_tglpinjamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tglpinjamActionPerformed

    private void txt_tglkembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_tglkembaliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tglkembaliActionPerformed

    private void txt_statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_statusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_statusActionPerformed

    private void JeditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JeditActionPerformed
        // TODO add your handling code here:
        edit_data();
    }//GEN-LAST:event_JeditActionPerformed

    private void JsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JsimpanActionPerformed
        // TODO add your handling code here:
        simpan_data();
    }//GEN-LAST:event_JsimpanActionPerformed

    private void jHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jHapusActionPerformed
        // TODO add your handling code here:
        hapus_data();
    }//GEN-LAST:event_jHapusActionPerformed

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
            java.util.logging.Logger.getLogger(form_pinjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(form_pinjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(form_pinjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(form_pinjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new form_pinjaman().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Jedit;
    private javax.swing.JButton Jsimpan;
    private javax.swing.JButton jHapus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tbl_peminjaman;
    private javax.swing.JComboBox txt_anggota;
    private javax.swing.JComboBox txt_buku;
    private javax.swing.JTextField txt_pinjam;
    private javax.swing.JComboBox txt_status;
    private javax.swing.JTextField txt_tglkembali;
    private javax.swing.JTextField txt_tglpinjam;
    // End of variables declaration//GEN-END:variables
}
