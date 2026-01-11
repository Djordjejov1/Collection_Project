package at.ac.fhcampus.simple_manager.Services;

import at.ac.fhcampus.simple_manager.Models.CollectionEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonExportService {

    private final Gson gson;

    public JsonExportService() {
        this.gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    public Path exportEntry(CollectionEntry entry) throws Exception {

        String projectPath = System.getProperty("user.dir");

        Path exportDir = Paths.get(projectPath, "exports");
        Files.createDirectories(exportDir);

        String fileName = "entry_" + entry.getId() + ".json";
        Path filePath = exportDir.resolve(fileName);

        String json = gson.toJson(entry);
        Files.writeString(filePath, json);

        return filePath;
    }

    public List<CollectionEntry> importAny(String absolutePath) throws Exception {
        Path filePath = Paths.get(absolutePath);
        // Falls die Datei nicht existiert, geben wir eine leere Liste zurück (verhindert Absturz)
        if (!Files.exists(filePath)) return new ArrayList<>();

        // Die Datei wird eingelesen und Leerzeichen am Anfang/Ende werden entfernt (.trim())
        String json = Files.readString(filePath).trim();
        List<CollectionEntry> resultList = new ArrayList<>();

        // PRÜFUNG: Startet der Text mit '[', ist es ein JSON-Array (Liste)
        if (json.startsWith("[")) {
            // TypeToken wird benötigt, da Java zur Laufzeit die Typen von Listen "vergisst" (Type Erasure)
            // Wir sagen GSON hiermit explizit: "Erwarte eine ArrayList voll mit CollectionEntry-Objekten"
            Type listType = new TypeToken<ArrayList<CollectionEntry>>(){}.getType();
            resultList = gson.fromJson(json, listType);

            // PRÜFUNG: Startet der Text mit '{', ist es ein einzelnes JSON-Objekt
        } else if (json.startsWith("{")) {
            // GSON wandelt den Text in ein einzelnes Objekt um
            CollectionEntry singleEntry = gson.fromJson(json, CollectionEntry.class);
            if (singleEntry != null) {
                // Wir packen das Einzelobjekt in eine Liste, damit der Rückgabetyp einheitlich bleibt
                resultList.add(singleEntry);
            }
        }

        return resultList;
    }
}
