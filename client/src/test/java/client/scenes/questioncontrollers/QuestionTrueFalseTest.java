package client.scenes.questioncontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import client.dependedoncomponents.EmoteCtrlDOC;
import client.dependedoncomponents.MainCtrlDOC;
import client.dependedoncomponents.QuestionTrueFalseCtrlDOC;
import client.dependedoncomponents.TimeUtilsDOC;
import client.dependedoncomponents.TimerBarCtrlDOC;
import client.scenes.QuestionFrameCtrl;
import commons.Activity;
import commons.Question;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionTrueFalseTest {
    private QuestionTrueFalseCtrl sut;
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

        questionFrame = new QuestionFrameCtrl(null, timeUtilsDOC, null, mainCtrlDOC, timerBarCtrlDOC, emoteCtrlDOC);

        sut = new QuestionTrueFalseCtrlDOC(mainCtrlDOC, questionFrame);
        Activity a1 = new Activity("a", null, "using your phone",
            1500, "x");
        Activity a2 = new Activity("b", null, "showering",
            1200, "x");
        question = new Question(List.of(a1, a2), a1.title + " consumes more than " + a2.title, 0,
            "trueFalseQuestion");
        sut.initialize(question);
    }

    @Test
    public void constructorTest() {
        assertNotNull(sut);
        assertSame(mainCtrlDOC, sut.getMainCtrl());
    }

    @Test
    void trueSelected() {
        sut.trueSelected();
        assertEquals(0, sut.getSelectedAnswerButton());
    }

    @Test
    void falseSelected() {
        sut.falseSelected();
        assertEquals(1, sut.getSelectedAnswerButton());
    }


    @Test
    void initialize() {
        assertEquals("using your phone consumes more than showering",
            ((QuestionTrueFalseCtrlDOC) sut).getQuestionText());
    }
}
