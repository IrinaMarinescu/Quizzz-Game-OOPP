package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import client.dependedoncomponents.AddActivityDialogCtrlDOC;
import client.scenes.exceptions.InvalidDataException;
import client.utils.ServerUtils;
import java.io.File;
import org.junit.jupiter.api.Test;

class AddActivityDialogCtrlTest {

    AddActivityDialogCtrl activityDialogCtrl =
        new AddActivityDialogCtrlDOC(new AdminInterfaceCtrl(new MainCtrl(), new ServerUtils()), new ServerUtils());

    @Test
    void testSetGetImage() {
        File newFile = new File("test.png");
        activityDialogCtrl.setUploadedImage(newFile);

        assertEquals(newFile, activityDialogCtrl.getUploadedImage());
    }

    @Test
    void testGetExtension() {
        var s = activityDialogCtrl.getExtension("test.png");
        assertEquals(s.get(), ".png");
    }

    @Test
    void testGetEmptyExtension() {
        var s = activityDialogCtrl.getExtension("brr");
        assertFalse(s.isPresent());
    }

    @Test
    void testValidateNullFile() {
        assertThrows(InvalidDataException.class, () -> {
            activityDialogCtrl.validateFileProperties(null);
        });
    }

    @Test
    void testValidateInvalidExtension() {
        assertThrows(InvalidDataException.class, () -> {
            activityDialogCtrl.validateFileProperties(new File("test.pdf"));
        });
    }

    @Test
    public void testValidateNonAlphanumeric() {
        assertThrows(InvalidDataException.class, () -> {
            activityDialogCtrl.validateFormDataHelper(";");
        });
    }
}