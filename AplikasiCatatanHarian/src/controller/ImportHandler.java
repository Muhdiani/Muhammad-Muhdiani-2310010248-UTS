package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.Note;

public class ImportHandler {

    private DatabaseHandler dbHandler;
    
    // Asumsikan format tanggal di CSV adalah yyyy-MM-dd (dari Date.toLocalDate().toString())
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; 
    
    // Constructor menerima DatabaseHandler
    public ImportHandler(DatabaseHandler handler) {
        this.dbHandler = handler;
    }

    /**
     * Membuka dialog JFileChooser dan memproses file CSV yang dipilih.
     * @param parentComponent Komponen induk untuk dialog pesan.
     * @return Jumlah catatan yang berhasil diimpor.
     */
    public int importNotesFromCsv(java.awt.Component parentComponent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih File CSV untuk Diimpor");
        
        int userSelection = fileChooser.showOpenDialog(parentComponent);
        int importedCount = 0;

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            
            try {
                importedCount = readCsvAndInsert(fileToOpen);
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parentComponent, "Gagal membaca file: " + e.getMessage(), "Kesalahan IO", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parentComponent, "Kesalahan dalam format data CSV: " + e.getMessage(), "Kesalahan Data", JOptionPane.ERROR_MESSAGE);
            }
        }
        return importedCount;
    }
    
    /**
     * Membaca file CSV dan memasukkan data catatan baru ke database.
     */
    private int readCsvAndInsert(File file) throws IOException {
        int insertedCount = 0;
        // Gunakan separator yang sama dengan yang kita pakai saat export (;)
        String line;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Lewati header CSV (baris pertama)
            br.readLine(); 
            
            while ((line = br.readLine()) != null) {
                // Split baris berdasarkan semicolon (;)
                // Gunakan limit = 4 untuk memastikan kolom konten (ke-4) utuh
                String[] data = line.split(";", 4); 
                
                if (data.length < 4) {
                    // Abaikan baris yang formatnya tidak lengkap
                    continue; 
                }
                
                // Data Index: 0=ID, 1=Tanggal, 2=Kategori, 3=Konten
                
                // Konversi string tanggal menjadi LocalDateTime
                LocalDate datePart = LocalDate.parse(data[1], DATE_FORMATTER);
                // Karena database kita butuh LocalDateTime, kita tambahkan waktu default 00:00
                LocalDateTime dateTime = datePart.atStartOfDay(); 
                
                String category = data[2].trim();
                String content = data[3].trim();
                
                // Masukkan sebagai catatan baru (ID akan dibuat otomatis)
                dbHandler.insertNote(content, category);
                insertedCount++;
            }
        }
        return insertedCount;
    }
}