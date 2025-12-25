package at.ac.fhcampus.simple_manager.Models;


public class CollectionEntry {
    private int id;
    private String title;
    private String author;
    private EntryType type;

    public CollectionEntry(int id, String title, String author, EntryType type) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.type = type;
    }

    //Getter for Instanzvariabeln:
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public EntryType getType() {
        return type;
    }

    //Setter für Instanzvariabeln:
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setType(EntryType type) {
        this.type = type;
    }

    //toString für die anzeige !

    @Override
    public String toString() {
        return title + " | " + author + " | " + type;
    }
}
