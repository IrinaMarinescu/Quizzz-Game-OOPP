package client.scenes.questioncontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import client.dependedoncomponents.EmoteCtrlDOC;
import client.dependedoncomponents.InsteadOfQuestionCtrlDOC;
import client.dependedoncomponents.MainCtrlDOC;
import client.dependedoncomponents.TimeUtilsDOC;
import client.dependedoncomponents.TimerBarCtrlDOC;
import client.scenes.QuestionFrameCtrl;
import commons.Activity;
import commons.Question;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsteadOfQuestionCtrlTest {
    private InsteadOfQuestionCtrl sut;
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

        questionFrame = new QuestionFrameCtrl(mainCtrlDOC, timerBarCtrlDOC, emoteCtrlDOC, timeUtilsDOC, null);

        sut = new InsteadOfQuestionCtrlDOC(mainCtrlDOC, questionFrame);
        Activity a1 = new Activity("a", null, "using your phone",
                1500, "Mirella");
        Activity a2 = new Activity("b", null, "showering",
                1200, "Mirella");
        Activity a3 = new Activity("c", null, "playing Wii",
                1300, "Mirella");
        Activity a4 = new Activity("d", null, "heating your living room",
                1800, "Mirella");
        question = new Question(List.of(a1, a2, a3, a4), "Instead of using your phone, what could you do?", 1,
                "instead of question");
        sut.initialize(question);
    }

    @Test
    public void constructorTest() {
        assertNotNull(sut);
        assertSame(mainCtrlDOC, sut.getMainCtrl());
    }

    @Test
    void setAnswerA() {
        sut.setAnswerA();
        assertEquals(0, sut.getSelectedAnswerButton());
    }

    @Test
    void setAnswerB() {
        sut.setAnswerB();
        assertEquals(1, sut.getSelectedAnswerButton());
    }

    @Test
    void setAnswerC() {
        sut.setAnswerC();
        assertEquals(2, sut.getSelectedAnswerButton());
    }

    @Test
    void initialize() {
        assertEquals("Instead of using your phone, what could you do?",
                ((InsteadOfQuestionCtrlDOC) sut).getQuestionText());
    }
}
