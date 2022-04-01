package server.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


@RunWith(MockitoJUnitRunner.class)
class FileStorageServiceTest {

    private FileStorageService fileStorageService;

    MockMultipartFile file;

    @BeforeEach
    public void setUp() {
        file = new MockMultipartFile("test", "test.txt", "text/plain", "some test ".getBytes());
        fileStorageService = Mockito.mock(FileStorageService.class);
    }

    @Test
    public void testSave() {
        Mockito.doNothing().when(fileStorageService).save(isA(MultipartFile.class), isA(String.class));

        fileStorageService.save(file, "test");

        Mockito.verify(fileStorageService, times(1)).save(file, "test");
    }

    @Test
    public void testSaveNull() {
        Mockito.doThrow(new RuntimeException()).when(fileStorageService).save(isNull(), isNull());

        assertThrows(RuntimeException.class, () -> {
            fileStorageService .save(null, null);
        });
    }

    @Test
    public void testDelete() {
        Mockito.doNothing().when(fileStorageService).delete(isA(String.class));

        fileStorageService.delete("test");

        Mockito.verify(fileStorageService, times(1)).delete("test");
    }

    @Test
    public void testDeleteNull() {
        Mockito.doThrow(new RuntimeException()).when(fileStorageService).delete(isNull());

        assertThrows(RuntimeException.class, () -> {
            fileStorageService.delete(null);
        });
    }

    @Test
    public void testLoad() throws IOException {
        Mockito.when(fileStorageService.load(Mockito.any(String.class))).thenReturn(new InputStreamResource(file.getInputStream()));

        fileStorageService.load("test");
        Mockito.verify(fileStorageService, times(1)).load("test");
    }
}