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
import java.util.logging.Level;
import java.util.logging.Logger;

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
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, "Gagal terhubung ke database.", e);
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
    
    // --- 4. READ (Membaca Catatan dengan Filter Waktu TANPA Pencarian) ---
    public List<Note> getFilteredNotes(int days) {
        // Panggil searchNotes dengan keyword kosong untuk kompatibilitas
        return searchNotes(days, "");
    }
    
    // --- 5. READ (Membaca Catatan dengan Filter Waktu DAN Pencarian) ---
    /**
     * Mengambil catatan berdasarkan filter hari dan keyword pencarian.
     * @param days Jumlah hari terakhir, atau 0 untuk semua.
     * @param keyword Kata kunci untuk mencari di kolom 'content' atau 'category'.
     * @return List<Note> hasil pencarian.
     */
    public List<Note> searchNotes(int days, String keyword) {
        List<Note> noteList = new ArrayList<>();
        
        // 1. Persiapan klausa WHERE
        StringBuilder whereClause = new StringBuilder();
        List<String> params = new ArrayList<>();
        
        // Klausa Hari Terakhir (hanya jika days > 0)
        if (days > 0) {
            // Menggunakan fungsi date() SQLite untuk memfilter tanggal
            whereClause.append(" date_created >= date('now', '-" + days + " day') "); 
        }
        
        // Klausa Keyword Pencarian
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Jika sudah ada klausa hari, tambahkan AND
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            // Mencari di kolom 'content' ATAU 'category'
            whereClause.append(" (content LIKE ? OR category LIKE ?) ");
            
            // Simpan parameter untuk binding
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        
        // 2. Buat query final
        String sql = "SELECT id, date_created, content, category FROM " + TABLE_NAME;
        if (whereClause.length() > 0) {
            sql += " WHERE " + whereClause.toString();
        }
        sql += " ORDER BY date_created DESC";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 3. Set parameter keyword
            int paramIndex = 1;
            for (String param : params) {
                pstmt.setString(paramIndex++, param);
            }
            
            // 4. Eksekusi query dan proses ResultSet
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Parsing tanggal menggunakan FORMATTER yang sudah didefinisikan
                    LocalDateTime date = LocalDateTime.parse(rs.getString("date_created"), FORMATTER);
                    Note note = new Note(
                        rs.getInt("id"),
                        rs.getString("content"),
                        date,
                        rs.getString("category")
                    );
                    noteList.add(note);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saat mencari catatan: " + e.getMessage());
        }
        return noteList;
    }
    
    // --- 6. UPDATE (Mengubah Catatan) ---
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

    // --- 7. DELETE (Menghapus Catatan) ---
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