package server.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import server.servicespecs.FileStorageServiceSpec;

@Service
public class FileStorageService implements FileStorageServiceSpec {
    private final Path root = Paths.get("images");

    public void init() {
        try {
            Files.createDirectories(root);
            System.out.println("initialized storage location.");
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location " + e.getMessage());
        }
    }

    @Override
    public void save(MultipartFile file, String filePath) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(filePath));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
