package controller; 

import model.Note; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    
    // --- Konstanta Database ---
    private static final String DB_NAME = "diary_notes.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;
    private static final String TABLE_NAME = "notes";
    
    // Formatter untuk konversi LocalDateTime ke string
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // --- 1. Koneksi ---
    private Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(CONNECTION_STRING);
        } catch (Exception e) {
            System.err.println("Gagal terhubung atau driver tidak ditemukan: " + e.getMessage());
        }
        return conn;
    }

    // --- 2. Create Table ---
    public void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n"
                + " id integer PRIMARY KEY,\n" 
                + " content text NOT NULL,\n"    
                + " date_created text NOT NULL,\n" 
                + " category text\n" 
                + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("Tabel 'notes' berhasil dibuat atau sudah ada.");
            
        } catch (SQLException e) {
            System.err.println("Gagal membuat tabel: " + e.getMessage());
        }
    }
    
    // --- 3. CREATE (Menambah Catatan) ---
    public void insertNote(String content, String category) {
        String sql = "INSERT INTO " + TABLE_NAME + "(content, date_created, category) VALUES(?, ?, ?)";

        String now = LocalDateTime.now().format(FORMATTER);

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, content); 
            pstmt.setString(2, now); 
            pstmt.setString(3, category); 
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan catatan: " + e.getMessage());
        }
    }
    
    // --- 4. READ (Membaca Catatan dengan Filter Waktu) ---
    /**
     * Mengambil catatan dari database dalam rentang hari tertentu.
     * @param days Jumlah hari ke belakang dari hari ini. Jika days == 0, ambil semua.
     * @return List<Note> daftar objek Note.
     */
    public List<Note> getFilteredNotes(int days) {
        List<Note> notes = new ArrayList<>();
        String sql;
        
        // Query untuk filter waktu
        if (days == 0) {
            // Ambil semua
            sql = "SELECT id, content, date_created, category FROM " + TABLE_NAME + " ORDER BY date_created DESC";
        } else {
            // Filter berdasarkan tanggal yang lebih baru dari limitDate
            sql = "SELECT id, content, date_created, category FROM " + TABLE_NAME 
                + " WHERE date_created >= ? "
                + " ORDER BY date_created DESC";
        }

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (days != 0) {
                // Tentukan tanggal batas waktu (days ke belakang)
                LocalDateTime limitDate = LocalDateTime.now().minusDays(days);
                String limitDateStr = limitDate.format(FORMATTER);
                pstmt.setString(1, limitDateStr);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime date = LocalDateTime.parse(rs.getString("date_created"), FORMATTER);
                    
                    Note note = new Note(
                        rs.getInt("id"),
                        rs.getString("content"),
                        date,
                        rs.getString("category")
                    );
                    notes.add(note);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil catatan filter: " + e.getMessage());
        }
        return notes;
    }
    
    // --- 5. UPDATE (Mengubah Catatan) ---
    public void updateNote(int id, String newContent, String newCategory) {
        String sql = "UPDATE " + TABLE_NAME + " SET content = ?, category = ? WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newContent);
            pstmt.setString(2, newCategory);
            pstmt.setInt(3, id);
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Gagal mengupdate catatan (ID: " + id + "): " + e.getMessage());
        }
    }

    // --- 6. DELETE (Menghapus Catatan) ---
    public void deleteNote(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Gagal menghapus catatan (ID: " + id + "): " + e.getMessage());
        }
    }
}