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
    private Path root = Paths.get("images");

    /**
     * {@inheritDoc}
     *
     * @param rootPath the root of the upload path.
     */
    @Override
    public void init(String rootPath) {
        try {
            root = Paths.get(rootPath);
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param file the file to save.
     * @param filePath the path to the file.
     */
    @Override
    public void save(MultipartFile file, String filePath) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(filePath));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param filePath the path of the file to delete.
     */
    @Override
    public void delete(String filePath) {
        try {
            Path path = root.resolve(filePath);
            Files.deleteIfExists(path);
        } catch (Exception e) {
            throw new RuntimeException("Error when deleting " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param filePath the path to the file to load.
     * @return a resource, representing the file loaded from the file system.
     */
    @Override
    public Resource load(String filePath) {
        try {
            Path file = root.resolve(filePath);
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
