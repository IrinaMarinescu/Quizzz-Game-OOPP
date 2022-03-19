package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import commons.Activity;
import commons.Question;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import server.database.ActivityRepository;
import server.dependedoncomponents.RandomDOC;

/**
 * The @DataJpaTest annotation helps in creating an in-memory environment in which to test database queries.
 *
 * @see <a href="https://www.bezkoder.com/spring-boot-unit-test-jpa-repo-datajpatest/">Spring Data Repository Unit Test</a>
 */
@DataJpaTest
class ActivityControllerTest {

    @Autowired
    private ActivityRepository repo;

    private ActivityController sut;

    @BeforeEach
    public void setUp() {
        sut = new ActivityController(repo, new Random());
    }

    @Test
    public void cannotAddNullTitleOrSource() {
        var s = sut.add(new Activity(null, null, null, 0, null));
        assertEquals(BAD_REQUEST, s.getStatusCode());
    }

    @Test
    public void testAdd() {
        var s = sut.add(new Activity("00-a", "ss/ss.png", "a", 5, "b"));
        Activity activity = repo.findById("00-a");

        assertEquals(activity.consumptionInWh, 5);
    }

    @Test
    public void testImport() {
        var s = sut.importActivities(
            Arrays.asList(
                new Activity("00-a", "ss/ss.png", "a", 5, "b"),
                new Activity("00-b", "ss/ss.png", "a", 10, "b"))
        );
        Activity a1 = repo.findById("00-a");
        Activity a2 = repo.findById("00-b");

        assertEquals(a1.consumptionInWh, 5);
        assertEquals(a2.consumptionInWh, 10);
    }

    @Test
    public void testImportInvalidData() {
        var s = sut.importActivities(
            Arrays.asList(
                new Activity(null, null, null, 0, null),
                new Activity("00-b", "ss/ss.png", "a", 10, "b"))
        );

        assertNull(repo.findById("00-b"));
    }

    // Tests below regard question generation

    @Test
    void associateQuestion() {
        assertEquals("trueFalseQuestion", sut.associateQuestion(0));
        assertEquals("oneImageQuestion", sut.associateQuestion(3));
        assertEquals("insteadOfQuestion", sut.associateQuestion(4));
        assertNull(sut.associateQuestion(5));
    }

    @Test
    void trueFalseQuestionType1() {
        sut = new ActivityController(repo, new RandomDOC(0));
        sut.importActivities(List.of(new Activity("00-b", "ss/ss.png", "flying a plane", 10, "b")));
        List<Question> res = new ArrayList<>();
        sut.generateTrueFalseQuestion(0, res);
        Question q = res.get(0);

        assertEquals("flying a plane consumes 30 Wh per hour.", q.getQuestion());
        assertSame(1, q.getCorrectAnswer());
    }

    @Test
    void trueFalseQuestionType2() {
        sut = new ActivityController(repo, new RandomDOC(1));
        sut.importActivities(List.of(
            new Activity("00-b", "ss/ss.png", "flying a plane", 10, "b"),
            new Activity("054-b", "ss/sds.png", "TITLE", 15, "google.com"))
        );
        List<Question> res = new ArrayList<>();
        sut.generateTrueFalseQuestion(0, res);
        Question q = res.get(0);

        assertEquals("flying a plane consumes more than TITLE.", q.getQuestion());
        assertSame(1, q.getCorrectAnswer());
    }

    @Test
    void generateOpenQuestionTest() {
        sut.importActivities(List.of(new Activity("00-b", "ss/ss.png", "flying a plane", 10, "b")));
        List<Question> res = new ArrayList<>();
        sut.generateOpenQuestion(1, res);
        Question q = res.get(0);

        assertEquals("How much energy in Wh does flying a plane consume?", q.getQuestion());
        assertEquals(10, q.getActivities().get(0).consumptionInWh);
        assertSame(0, q.getCorrectAnswer());
    }

    @Test
    void generateThreePicturesQuestion() {
        sut.importActivities(List.of(
            new Activity("00-b", "ss/ss.png", "flying a plane", 10, "b"),
            new Activity("054-b", "ss/sds.png", "TITLE", 57, "google.com"),
            new Activity("05ds-b", "ss/sda.png", "using a lamp", 56, "bing.com")
        ));
        List<Question> res = new ArrayList<>();
        sut.generateThreePicturesQuestion(2, res);
        Question q = res.get(0);

        assertEquals("Which consumes more?", q.getQuestion());
    }

    @Test
    void oneImageQuestion() {
        sut.importActivities(List.of(
            new Activity("00-b", "ss/ss.png", "flying a plane", 13, "b")
        ));
        List<Question> res = new ArrayList<>();
        sut.generateOneImageQuestion(3, res);
        Question q = res.get(0);

        assertEquals("oneImageQuestion", q.getQuestionType());
        assertEquals("How much energy in Wh does flying a plane consume?", q.getQuestion());
        assertSame(13, q.getActivities().get(0).consumptionInWh);
    }

    @Test
    void generateInsteadOfQuestion() {
        sut.importActivities(List.of(
            new Activity("00-b", "ss/ss.png", "flying a plane", 10, "b"),
            new Activity("054-b", "ss/sds.png", "TITLE", 57, "google.com"),
            new Activity("05ds-b", "ss/sda.png", "using a lamp", 56, "bing.com"),
            new Activity("05324-b", "ss/sss.png", "doing something", 100, "yandex.com")
        ));
        List<Question> res = new ArrayList<>();
        sut.generateInsteadOfQuestion(4, res);
        Question q = res.get(0);

        assertEquals("Instead of using a lamp you can do...", q.getQuestion());
        assertEquals("flying a plane", q.getActivities().get(q.getCorrectAnswer()).title);
    }
}