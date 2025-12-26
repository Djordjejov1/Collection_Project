package at.ac.fhcampus.simple_manager.Models;


import at.ac.fhcampus.simple_manager.MainApp;
import com.google.gson.annotations.Expose;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class CollectionEntry {
    @Expose private int id;
    @Expose private String title;
    @Expose private String author;
    @Expose private EntryType type;

    private  final BooleanProperty selected = new SimpleBooleanProperty(false); // verursacht sonst fehler ohne transient

    public CollectionEntry(int id, String title, String author, EntryType type) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.type = type;
    }


    //Testdata:
    public static void loadTestData() {
        MainApp.getEntries().addAll(
                new CollectionEntry(1, "The Hobbit", "J.R.R. Tolkien", EntryType.BOOK),
                new CollectionEntry(2, "Clean Code", "Robert C. Martin", EntryType.BOOK),
                new CollectionEntry(3, "Interstellar", "Christopher Nolan", EntryType.DVD),
                new CollectionEntry(4, "Dark Side of the Moon", "Pink Floyd", EntryType.CD),
                new CollectionEntry(5, "1984", "George Orwell", EntryType.BOOK),
                new CollectionEntry(6, "Inception", "Christopher Nolan", EntryType.DVD)
        );
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
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setType(EntryType type) {
        this.type = type;
    }


    //Boolean getter setter für das Binding:
    public boolean isSelected() { return selected.get(); }
    public void setSelected(boolean value) { selected.set(value); }
    public BooleanProperty selectedProperty() { return selected; }

    //toString für die anzeige !
    @Override
    public String toString() {
        return title + " | " + author + " | " + type;
    }
}
