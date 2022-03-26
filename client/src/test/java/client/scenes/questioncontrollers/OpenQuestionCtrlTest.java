package client.scenes.questioncontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import client.dependedoncomponents.EmoteCtrlDOC;
import client.dependedoncomponents.MainCtrlDOC;
import client.dependedoncomponents.OpenQuestionCtrlDOC;
import client.dependedoncomponents.TimeUtilsDOC;
import client.dependedoncomponents.TimerBarCtrlDOC;
import client.scenes.QuestionFrameCtrl;
import commons.Activity;
import commons.Question;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpenQuestionCtrlTest {

    private OpenQuestionCtrl sut;
    private QuestionFrameCtrl questionFrame;
    private MainCtrlDOC mainCtrlDOC;
    private TimeUtilsDOC timeUtilsDOC;
    private EmoteCtrlDOC emoteCtrlDOC;
    private TimerBarCtrlDOC timerBarCtrlDOC;
    private Question question;

    /**
     * Setup for tests
     */
    @BeforeEach
    public void setup() {
        mainCtrlDOC = new MainCtrlDOC();
        timeUtilsDOC = new TimeUtilsDOC(150);
        emoteCtrlDOC = new EmoteCtrlDOC();
        timerBarCtrlDOC = new TimerBarCtrlDOC();

        questionFrame = new QuestionFrameCtrl(null, timeUtilsDOC, null, mainCtrlDOC,
                timerBarCtrlDOC, emoteCtrlDOC);;

        sut = new OpenQuestionCtrlDOC(mainCtrlDOC, questionFrame);
        question = new Question(List.of(new Activity("a", null, "using your phone",
                15, "Mirella")), "How many Wh does using your phone take?",
                0, "open question");
        sut.initialize(question);
    }

    @Test
    public void constructorTest() {
        assertNotNull(sut);
        assertSame(mainCtrlDOC, sut.getMainCtrl());
    }

    @Test
    void initialize() {
        assertEquals(question, sut.getQuestion());
        assertEquals("How many Wh does using your phone take?", ((OpenQuestionCtrlDOC) sut).text);
    }
}
