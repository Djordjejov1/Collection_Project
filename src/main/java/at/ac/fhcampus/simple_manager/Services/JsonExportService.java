package at.ac.fhcampus.simple_manager.Services;

import at.ac.fhcampus.simple_manager.Models.CollectionEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public CollectionEntry importEntry(String fileName) throws Exception {

        String projectPath = System.getProperty("user.dir");

        Path importDir = Paths.get(projectPath, "imports");
        Path filePath = importDir.resolve(fileName);

        if (!Files.exists(filePath)) {
            return null;
        }

        String json = Files.readString(filePath);

        return gson.fromJson(json, CollectionEntry.class);
    }
}
