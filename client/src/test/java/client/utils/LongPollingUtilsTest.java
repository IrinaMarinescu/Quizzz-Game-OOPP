package client.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import client.dependedoncomponents.MainCtrlDOC;
import client.dependedoncomponents.QuestionFrameCtrlDOC;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for long polling functions on client-side
 */
public class LongPollingUtilsTest {

    private LongPollingUtils sut;
    private MainCtrlDOC mainCtrlDOC;
    private QuestionFrameCtrlDOC questionFrameCtrlDOC;
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mainCtrlDOC = new MainCtrlDOC();
        questionFrameCtrlDOC = new QuestionFrameCtrlDOC();
        mapper = new ObjectMapper();

        sut = new LongPollingUtils(null, mainCtrlDOC, questionFrameCtrlDOC);
    }

    @Test
    void constructor() {
        assertNotNull(sut);
        assertSame(questionFrameCtrlDOC, sut.questionFrameCtrl);
        assertFalse(sut.active);
    }

    @Test
    void setPollingActive() {
        assertFalse(sut.active);
        sut.setActive(true);
        assertTrue(sut.active);
        sut.setActive(false);
        assertFalse(sut.active);
    }

    @Test
    void performActionStart() throws JsonProcessingException {
        JsonNode action = mapper.readTree("{\"type\":\"START_MP_GAME\"}");

        sut.performAction(action);
        assertSame(1, mainCtrlDOC.countLogs());
        assertSame(1, mainCtrlDOC.countLogs("game"));
    }

    @Test
    void performActionEmoji() throws JsonProcessingException {
        JsonNode action = mapper.readTree("{\"type\":\"EMOJI\",\"name\":\"James\",\"reaction\":\"sad\"}");

        sut.performAction(action);
        assertSame(1, questionFrameCtrlDOC.countLogs());
        assertEquals("James sad", questionFrameCtrlDOC.getLog(0));
    }

    @Test
    void performActionHalve() throws JsonProcessingException {
        JsonNode action = mapper.readTree("{\"type\":\"HALVE_TIME\"}");

        sut.performAction(action);
        assertSame(1, mainCtrlDOC.countLogs());
        assertSame(1, mainCtrlDOC.countLogs("halve"));
    }

    @Test
    void performActionDisconnect() throws JsonProcessingException {
        JsonNode action = mapper.readTree("{\"type\":\"DISCONNECT\",\"name\":\"John\"}");

        sut.performAction(action);
        assertSame(1, mainCtrlDOC.countLogs());
        assertSame(1, mainCtrlDOC.countLogs("leave John"));
    }
}
