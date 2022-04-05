package client.scenes;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import client.dependedoncomponents.ExitPopUpCtrlDOC;
import client.dependedoncomponents.MainCtrlDOC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExitPopUpCtrlTest {

    private ExitPopUpCtrl sut;
    private MainCtrlDOC mainCtrlDOC;

    @BeforeEach
    public void setup() {
        mainCtrlDOC = new MainCtrlDOC();
        sut = new ExitPopUpCtrlDOC(mainCtrlDOC);
    }

    @Test
    public void constructorTest() {
        assertNotNull(sut);
        assertSame(mainCtrlDOC, sut.getMainCtrl());
    }

    @Test
    public void setTypeTest() {
        sut.setType(0);
        assertSame(0, sut.getType());
    }
}
