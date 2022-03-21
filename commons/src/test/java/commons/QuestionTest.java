package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionTest {

    private Question sut;

    @BeforeEach
    void setup() {
        Activity activity = new Activity("id", "abc/abc.png", "Hello world?", 123, "www.google.com");
        sut = new Question(List.of(activity), "world", 7, "TrueFalse");
    }

    @Test
    void constructor() {
        assertNotNull(sut);
    }

    @Test
    void getActivities() {
        Activity activity = new Activity("id", "abc/abc.png", "Hello world?", 123, "www.google.com");
        assertEquals(activity, sut.getActivities().get(0));
    }

    @Test
    void getQuestion() {
        assertEquals("world", sut.getQuestion());
    }

    @Test
    void setQuestion() {
        assertEquals("world", sut.getQuestion());
        sut.setQuestion("hello");
        assertEquals("hello", sut.getQuestion());
    }

    @Test
    void getCorrectAnswer() {
        assertSame(7, sut.getCorrectAnswer());
    }

    @Test
    void getQuestionType() {
        assertEquals("TrueFalse", sut.getQuestionType());
    }
}
