package at.ac.fhcampus.simple_manager.Services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import at.ac.fhcampus.simple_manager.Models.CollectionEntry;

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
}
