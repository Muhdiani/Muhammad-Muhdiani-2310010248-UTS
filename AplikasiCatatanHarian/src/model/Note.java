package model; // Sesuai dengan struktur package Anda

import java.time.LocalDateTime;

/**
 * Kelas Model (POJO) untuk merepresentasikan satu entri Catatan Harian.
 * Menerapkan konsep Pemrograman Berorientasi Objek.
 */
public class Note {
    
    // Fields / Attributes (sesuai kolom database)
    private int id;
    private LocalDateTime date; // date_created
    private String content;
    private String category; // Kolom baru

    // 1. Constructor untuk membuat objek dari data di database (dengan ID)
    public Note(int id, String content, LocalDateTime date, String category) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.category = category;
    }

    // 2. Constructor untuk catatan baru (tanpa ID, karena ID dibuat oleh database)
    public Note(String content, LocalDateTime date, String category) {
        this.content = content;
        this.date = date;
        this.category = category;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getContent() { return content; }
    public LocalDateTime getDate() { return date; }
    public String getCategory() { return category; }
    
    // --- Setters (Berguna untuk operasi EDIT/UPDATE) ---
    // ID tidak memiliki setter karena ID adalah Primary Key yang tidak boleh diubah
    public void setContent(String content) { this.content = content; }
    public void setCategory(String category) { this.category = category; }
    
    /**
     * Override toString() untuk representasi string yang berguna (misalnya di log)
     */
    @Override
    public String toString() {
        // Menampilkan kategori, tanggal, dan cuplikan konten (maks 30 karakter)
        return "[" + category + "] " 
             + date.toLocalDate().toString() 
             + " - " 
             + content.substring(0, Math.min(content.length(), 30)) 
             + (content.length() > 30 ? "..." : "");
    }
}