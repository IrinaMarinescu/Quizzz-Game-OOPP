package client.scenes.questioncontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import client.dependedoncomponents.EmoteCtrlDOC;
import client.dependedoncomponents.MainCtrlDOC;
import client.dependedoncomponents.QuestionThreePicturesCtrlDOC;
import client.dependedoncomponents.TimeUtilsDOC;
import client.dependedoncomponents.TimerBarCtrlDOC;
import client.scenes.QuestionFrameCtrl;
import commons.Activity;
import commons.Question;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionThreePicturesCtrlTest {

    private QuestionThreePicturesCtrl sut;
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

        sut = new QuestionThreePicturesCtrlDOC(mainCtrlDOC, questionFrame);
        Activity a1 = new Activity("a", null, "using your phone",
            1500, "x");
        Activity a2 = new Activity("b", null, "showering",
            1200, "x");
        Activity a3 = new Activity("c", null, "playing Wii",
            1300, "x");

        question = new Question(List.of(a1, a2, a3), "Which one consumes the most?", 1,
            "threePicturesQuestion");
        sut.initialize(question);
    }

    @Test
    public void constructorTest() {
        assertNotNull(sut);
        assertSame(mainCtrlDOC, sut.getMainCtrl());
    }

    @Test
    public void answerASelectedTest() {
        sut.answerASelected();
        assertSame(0, sut.getSelectedAnswerButton());
    }

    @Test
    public void answerBSelectedTest() {
        sut.answerBSelected();
        assertSame(1, sut.getSelectedAnswerButton());
    }


    @Test
    public void answerCSelectedTest() {
        sut.answerCSelected();
        assertSame(2, sut.getSelectedAnswerButton());
    }

    @Test
    public void initializeTest() {
        assertEquals("Which one consumes the most?", ((QuestionThreePicturesCtrlDOC) sut).getQuestionText());
    }
}
