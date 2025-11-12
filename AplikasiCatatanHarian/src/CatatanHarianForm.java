import controller.DatabaseHandler; 
import controller.ExportHandler;  
import model.Note;                
import java.util.List;            
import javax.swing.JOptionPane;   
import javax.swing.table.DefaultTableModel; 
import javax.swing.ButtonGroup;    
import java.io.File;               
import java.time.LocalDateTime;    
import java.util.Date;             
import controller.ImportHandler;

public class CatatanHarianForm extends javax.swing.JFrame {
private DatabaseHandler dbHandler;
    private int selectedNoteId = -1;
    private ButtonGroup exportButtonGroup; // Variabel untuk mengelompokkan radio button
    private ExportHandler exportHandler;
    private ImportHandler importHandler;

    public CatatanHarianForm() {
        initComponents();
        
        // 1. Inisialisasi Database Handler dan pastikan tabel dibuat
        dbHandler = new DatabaseHandler();
        dbHandler.createNewTable();
        
        // Set tanggal JCalendar ke hari ini (Memperbaiki bug tampilan)
        jCalendarInput.setDate(new Date());
        
        // Inisialisasi Handlers
        exportHandler = new ExportHandler();
        importHandler = new ImportHandler(dbHandler);
        
        // 2. Kelompokkan Radio Button
        setupRadioButtons();
        
        // 3. Set model ComboBox Waktu (jika belum di initComponents)
        jComboBoxOrderWaktu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Waktu", "5 hari terakhir", "10 hari terakhir", "15 hari terakhir", "20 hari terakhir" }));
        
        // 4. Tambahkan listener ke ComboBox Waktu (FIXED: Memanggil loadNotesToTable)
        jComboBoxOrderWaktu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Panggil fungsi refresh tabel setiap kali ComboBox diubah
                loadNotesToTable(); 
            }
        });
        
        // 5. Memuat catatan saat form pertama kali dibuka (FIXED: Panggilan ditambahkan)
        loadNotesToTable();
    }
    
    /**
     * Mengelompokkan Radio Button dan mengatur default.
     */
    private void setupRadioButtons() {
        exportButtonGroup = new ButtonGroup();

        exportButtonGroup.add(jRadioButtonConvertTxt);
        exportButtonGroup.add(jRadioButtonConvertCsv);
        // Set default selection
        jRadioButtonConvertTxt.setSelected(true);
    }

    /**
     * Metode utama untuk me-refresh/memuat ulang data ke JTable.
     * Ini adalah satu-satunya method yang harus dipanggil untuk me-refresh tabel.
     */
    private void loadNotesToTable() {
        searchAndLoadNotes();
    }
    
    /**
     * Mengambil filter waktu dan keyword, lalu memuat data ke JTable.
     */
    private void searchAndLoadNotes() {
        String keyword = jTextFieldCari.getText().trim();
        int days = getSelectedFilterDays(); // Memanggil method helper
        
        // 1. Ambil data dari database menggunakan metode search
        List<Note> noteList = dbHandler.searchNotes(days, keyword); 
        
        // 2. Tampilkan hasil di JTable
        displayNotesInTable(noteList);
    }
    
    /**
     * Menampilkan List<Note> hasil query ke dalam JTable.
     * @param noteList Daftar catatan yang akan ditampilkan.
     */
    private void displayNotesInTable(List<Note> noteList) {
        // 1. Siapkan model tabel dengan nama kolom yang benar
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Tanggal", "Kategori", "Isi Catatan"}, 0
        ) {
            // Membuat kolom ID tidak bisa diedit
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        
        // 2. Isi model dengan data dari List<Note>
        for (Note note : noteList) {
            String snippet = note.getContent().substring(0, Math.min(note.getContent().length(), 70)); 
            if (note.getContent().length() > 70) snippet += " ...";
            
            model.addRow(new Object[]{
                note.getId(),
                note.getDate().toLocalDate().toString(), // Hanya tanggal
                note.getCategory(),
                snippet
            });
        }
        
        // 3. Setel model ke JTable
        jTableCatatan.setModel(model);
        
        // 4. Sembunyikan kolom ID (kolom 0)
        jTableCatatan.getColumnModel().getColumn(0).setMinWidth(0);
        jTableCatatan.getColumnModel().getColumn(0).setMaxWidth(0);
        jTableCatatan.getColumnModel().getColumn(0).setWidth(0);
    }

    /**
     * (METHOD BARU YANG HILANG)
     * Mengambil nilai integer (jumlah hari) dari ComboBox filter.
     * @return Jumlah hari ke belakang. 0 jika "Semua Waktu" atau gagal parsing.
     */
    private int getSelectedFilterDays() {
        String selectedItem = (String) jComboBoxOrderWaktu.getSelectedItem();
        if (selectedItem == null || selectedItem.toLowerCase().contains("semua")) {
            return 0;
        }

        // Contoh: "5 hari terakhir" -> 5
        try {
            String numberPart = selectedItem.split(" ")[0];
            return Integer.parseInt(numberPart);
        } catch (Exception e) {
            System.err.println("Gagal parsing filter ComboBox: " + e.getMessage());
            return 0; // Kembalikan 0 (ambil semua) jika gagal parsing
        }
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
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaInput = new javax.swing.JTextArea();
        jButtonTambah = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonHapus = new javax.swing.JButton();
        jButtonExport = new javax.swing.JButton();
        jButtonImport = new javax.swing.JButton();
        jButtonKeluar = new javax.swing.JButton();
        jCalendarInput = new com.toedter.calendar.JCalendar();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableCatatan = new javax.swing.JTable();
        jComboBoxKategori = new javax.swing.JComboBox<>();
        jComboBoxOrderWaktu = new javax.swing.JComboBox<>();
        jRadioButtonConvertTxt = new javax.swing.JRadioButton();
        jRadioButtonConvertCsv = new javax.swing.JRadioButton();
        jButtonCari = new javax.swing.JButton();
        jTextFieldCari = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("APLIKASI CATATAN HARIAN");

        jLabel2.setText("TULIS CATATAN");

        jTextAreaInput.setColumns(20);
        jTextAreaInput.setRows(5);
        jScrollPane1.setViewportView(jTextAreaInput);

        jButtonTambah.setText("TAMBAH");
        jButtonTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTambahActionPerformed(evt);
            }
        });

        jButtonEdit.setText("EDIT");
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });

        jButtonHapus.setText("HAPUS");
        jButtonHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHapusActionPerformed(evt);
            }
        });

        jButtonExport.setText("EXPORT");
        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportActionPerformed(evt);
            }
        });

        jButtonImport.setText("IMPORT");
        jButtonImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportActionPerformed(evt);
            }
        });

        jButtonKeluar.setText("KELUAR");
        jButtonKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKeluarActionPerformed(evt);
            }
        });

        jTableCatatan.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableCatatan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCatatanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableCatatan);

        jComboBoxKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Personal", "Pekerjaan", "Ide", "Umum" }));

        jComboBoxOrderWaktu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "5 hari terakhir", "10 hari terakhir", "15 hari terakhir", "20 hari terakhir" }));

        jRadioButtonConvertTxt.setText(".TXT");

        jRadioButtonConvertCsv.setText(".CSV");

        jButtonCari.setText("CARI");
        jButtonCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonHapus)
                        .addGap(0, 65, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addComponent(jCalendarInput, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonExport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonImport)
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtonConvertCsv)
                            .addComponent(jRadioButtonConvertTxt))
                        .addGap(109, 109, 109))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxOrderWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(226, 226, 226)
                        .addComponent(jLabel1)
                        .addGap(177, 177, 177)
                        .addComponent(jButtonKeluar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jButtonCari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldCari, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonKeluar)
                    .addComponent(jLabel1))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCalendarInput, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonTambah)
                            .addComponent(jButtonEdit)
                            .addComponent(jButtonHapus)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonCari)
                            .addComponent(jTextFieldCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jComboBoxKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(69, 69, 69)
                                .addComponent(jComboBoxOrderWaktu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(63, 63, 63)
                                .addComponent(jRadioButtonConvertTxt)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonConvertCsv)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButtonExport)
                                    .addComponent(jButtonImport))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
// 1. Validasi Mode Edit Aktif
    if (selectedNoteId == -1) {
        JOptionPane.showMessageDialog(this, "Pilih catatan dari tabel (klik baris) yang ingin diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // 2. Ambil Input Baru
    String newContent = jTextAreaInput.getText().trim();
    String newCategory = (String) jComboBoxKategori.getSelectedItem();
    
    // 3. Validasi Konten
    if (newContent.isEmpty() || newCategory == null) {
        JOptionPane.showMessageDialog(this, "Konten catatan baru dan kategori tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // 4. Panggil Update di DatabaseHandler
    try {
        dbHandler.updateNote(selectedNoteId, newContent, newCategory);
        
        // 5. Reset Status dan Perbarui Tampilan
        selectedNoteId = -1; // Keluar dari mode edit
        jTextAreaInput.setText("");
        jComboBoxKategori.setSelectedIndex(0); // Reset kategori ke default (opsional)
        jButtonTambah.setText("TAMBAH"); // 👈 **Penting:** Reset teks tombol
        loadNotesToTable();
        
        JOptionPane.showMessageDialog(this, "Catatan berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mengupdate catatan: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButtonEditActionPerformed

    private void jButtonTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTambahActionPerformed
        String content = jTextAreaInput.getText().trim();
    String category = (String) jComboBoxKategori.getSelectedItem();

    if (content.isEmpty() || category == null) {
        JOptionPane.showMessageDialog(this, "Isi catatan dan kategori tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Ini selalu operasi TAMBAH (Insert)
    dbHandler.insertNote(content, category);
    
    // Reset status dan perbarui tampilan
    selectedNoteId = -1; // Reset mode edit
    jTextAreaInput.setText("");
    loadNotesToTable();
    JOptionPane.showMessageDialog(this, "Catatan berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonTambahActionPerformed

    private void jButtonHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHapusActionPerformed
// 1. Dapatkan baris yang dipilih
    int selectedRow = jTableCatatan.getSelectedRow();
    
    // Validasi: Pastikan ada baris yang dipilih
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih baris catatan yang ingin dihapus terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // 2. Konfirmasi Penghapusan
    int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus catatan ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        
        try {
            // 3. Ambil ID dari kolom pertama (kolom ID yang tersembunyi)
            int noteId = (int) jTableCatatan.getModel().getValueAt(selectedRow, 0); 
            
            // 4. Hapus dari Database
            dbHandler.deleteNote(noteId);
            
            // 5. Reset Status dan Bersihkan Input
            selectedNoteId = -1; // Pastikan ID direset
            jTextAreaInput.setText("");
            jComboBoxKategori.setSelectedIndex(0); // Reset kategori ke default (opsional)
            jButtonTambah.setText("TAMBAH"); // 👈 **Penting:** Reset teks tombol
            
            // 6. Perbarui Tampilan JTable
            loadNotesToTable();
            
            JOptionPane.showMessageDialog(this, "Catatan berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus catatan: " + e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_jButtonHapusActionPerformed

    private void jTableCatatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCatatanMouseClicked
       // 1. Ambil baris yang dipilih
    int selectedRow = jTableCatatan.getSelectedRow();

    if (selectedRow != -1) {
        // 2. Ambil ID dari kolom pertama (indeks 0 - ID yang tersembunyi)
        // Kita simpan ID ini di variabel kelas untuk digunakan oleh tombol EDIT/HAPUS
        selectedNoteId = (int) jTableCatatan.getModel().getValueAt(selectedRow, 0);
        
        // 3. Ambil Kategori dan Konten dari kolom tabel
        String category = (String) jTableCatatan.getModel().getValueAt(selectedRow, 2); // Kolom Kategori di indeks 2
        String snippet = (String) jTableCatatan.getModel().getValueAt(selectedRow, 3); // Kolom Isi Catatan di indeks 3
        
        // 4. (Optional namun Direkomendasikan) Ambil Isi Penuh dari Database
        // Karena JTable hanya menampilkan cuplikan, kita perlu isi penuh.
        try {

            jTextAreaInput.setText(snippet);
            jComboBoxKategori.setSelectedItem(category);

            
        } catch (Exception e) {
             System.err.println("Gagal memuat detail catatan: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_jTableCatatanMouseClicked

    private void jButtonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportActionPerformed
String format = "";
    
    // 1. Tentukan format yang dipilih dari radio button
    if (jRadioButtonConvertTxt.isSelected()) {
        format = "TXT";
    } else if (jRadioButtonConvertCsv.isSelected()) {
        format = "CSV";
    } else {
        JOptionPane.showMessageDialog(this, "Pilih salah satu format ekspor (TXT atau CSV).", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // 2. Ambil semua data catatan
    // Menggunakan getFilteredNotes(0) untuk mengambil SEMUA catatan.
    List<Note> allNotes = dbHandler.getFilteredNotes(0);
    
    // 3. Panggil ExportHandler
    // 'this' digunakan sebagai parent component untuk dialog pop-up.
    exportHandler.exportNotes(allNotes, format, this);
    }//GEN-LAST:event_jButtonExportActionPerformed

    private void jButtonImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportActionPerformed
       // 1. Panggil metode import dari ImportHandler
    int importedCount = importHandler.importNotesFromCsv(this);
    
    // 2. Beri tahu pengguna hasilnya
    if (importedCount > 0) {
        JOptionPane.showMessageDialog(this, 
                "Berhasil mengimpor " + importedCount + " catatan dari file CSV!", 
                "Import Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
        
        // 3. Perbarui tampilan tabel
        loadNotesToTable();
    } else if (importedCount == 0) {
        JOptionPane.showMessageDialog(this, 
                "Tidak ada catatan baru yang berhasil diimpor.", 
                "Import Selesai", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    }//GEN-LAST:event_jButtonImportActionPerformed

    private void jButtonKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKeluarActionPerformed
       // Tampilkan dialog konfirmasi
    int response = JOptionPane.showConfirmDialog(
        this, 
        "Anda yakin ingin keluar dari Aplikasi Catatan Harian?", 
        "Konfirmasi Keluar", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE
    );
    
    // Jika pengguna memilih YES, tutup aplikasi
    if (response == JOptionPane.YES_OPTION) {
        System.exit(0);
    }
    }//GEN-LAST:event_jButtonKeluarActionPerformed

    private void jButtonCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCariActionPerformed
        searchAndLoadNotes();
    }//GEN-LAST:event_jButtonCariActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
/* Set the Nimbus look and feel */
    try {
        // Ganti L&F dari Nimbus ke L&F Sistem (SystemLookAndFeel)
        javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(CatatanHarianForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">

    try {
        // Ganti L&F dari Nimbus ke L&F Sistem (SystemLookAndFeel)
        javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(CatatanHarianForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new CatatanHarianForm().setVisible(true);
        }
    });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCari;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonExport;
    private javax.swing.JButton jButtonHapus;
    private javax.swing.JButton jButtonImport;
    private javax.swing.JButton jButtonKeluar;
    private javax.swing.JButton jButtonTambah;
    private com.toedter.calendar.JCalendar jCalendarInput;
    private javax.swing.JComboBox<String> jComboBoxKategori;
    private javax.swing.JComboBox<String> jComboBoxOrderWaktu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButtonConvertCsv;
    private javax.swing.JRadioButton jRadioButtonConvertTxt;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableCatatan;
    private javax.swing.JTextArea jTextAreaInput;
    private javax.swing.JTextField jTextFieldCari;
    // End of variables declaration//GEN-END:variables
}
