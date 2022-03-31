package server.servicespecs;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageServiceSpec {

    void save(MultipartFile file, String filePath);

}
