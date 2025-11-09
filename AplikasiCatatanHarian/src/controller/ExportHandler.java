package controller;

import model.Note;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;

public class ExportHandler {

    /**
     * Membuka dialog untuk menyimpan file dan menjalankan proses ekspor.
     * @param notes Daftar catatan (List<Note>) yang akan diekspor.
     * @param format Format file yang dipilih ("PDF", "TXT", atau "CSV").
     * @param parentComponent Komponen induk untuk dialog pesan (biasanya JFrame/this).
     */
    public void exportNotes(List<Note> notes, String format, java.awt.Component parentComponent) {
        if (notes.isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "Tidak ada catatan untuk diekspor.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Membuka JFileChooser untuk memilih lokasi penyimpanan
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Catatan sebagai " + format);
        fileChooser.setSelectedFile(new File("CatatanHarian_Export." + format.toLowerCase()));

        int userSelection = fileChooser.showSaveDialog(parentComponent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            try {
                if (format.equals("TXT")) {
                    exportToTxt(notes, fileToSave.getAbsolutePath());
                } else if (format.equals("CSV")) {
                    exportToCsv(notes, fileToSave.getAbsolutePath());
                } else if (format.equals("PDF")) {
                    // Implementasi PDF memerlukan library eksternal (misalnya iText).
                    // Untuk saat ini, kita akan menampilkan pesan kesalahan.
                    JOptionPane.showMessageDialog(parentComponent, "Ekspor PDF memerlukan library tambahan dan belum diimplementasikan.", "Fitur Belum Tersedia", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                JOptionPane.showMessageDialog(parentComponent, "Ekspor " + format + " berhasil!\nDisimpan di: " + fileToSave.getAbsolutePath(), "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parentComponent, "Gagal menulis file: " + e.getMessage(), "Kesalahan IO", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // --- Logika Ekspor TXT ---
    private void exportToTxt(List<Note> notes, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("APLIKASI CATATAN HARIAN - EXPORT TXT\n\n");
            for (Note note : notes) {
                writer.write("----------------------------------------\n");
                writer.write("ID: " + note.getId() + "\n");
                writer.write("Tanggal: " + note.getDate().toLocalDate() + "\n");
                writer.write("Kategori: " + note.getCategory() + "\n");
                writer.write("Konten:\n" + note.getContent() + "\n");
            }
        }
    }
    
    // --- Logika Ekspor CSV ---
    private void exportToCsv(List<Note> notes, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Header CSV
            writer.append("ID;Tanggal;Kategori;Konten Penuh\n"); 
            
            // Data
            for (Note note : notes) {
                // Gunakan String.format dan replaceNewLine agar konten menjadi satu baris
                String safeContent = note.getContent().replace("\n", " ").replace("\r", " ").replace(";", ",");
                
                writer.append(String.format("%d;%s;%s;%s\n", 
                        note.getId(), 
                        note.getDate().toLocalDate().toString(), 
                        note.getCategory(), 
                        safeContent));
            }
        }
    }
}