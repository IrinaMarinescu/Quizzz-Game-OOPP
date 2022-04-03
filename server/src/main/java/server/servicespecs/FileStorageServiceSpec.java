package server.servicespecs;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageServiceSpec {

    /**
     * Initializes the File Storage Service by creating the folder to the root path.
     *
     * @param rootPath the root of the upload path.
     */
    void init(String rootPath);

    /**
     * Saves a multipart file to a specified location on the file system.
     *
     * @param file the file to save.
     * @param filePath the path to the file.
     */
    void save(MultipartFile file, String filePath);

    /**
     * Loads a file present in the system by its path as a resource.
     *
     * @param filePath the path to the file to load.
     * @return a resource representing the loaded file.
     */
    Resource load(String filePath);

    /**
     * Deletes a file from the file system by its path.
     *
     * @param filePath the path of the file to delete.
     */
    void delete(String filePath);


}
