package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import server.ActivityFilter;
import server.database.ActivityRepository;
import server.dependedoncomponents.RandomDOC;
import server.services.FileStorageService;

/**
 * The @DataJpaTest annotation helps in creating an in-memory environment in which to test database queries.
 *
 * @see <a href="https://www.bezkoder.com/spring-boot-unit-test-jpa-repo-datajpatest/">Spring Data Repository Unit Test</a>
 */
@DataJpaTest
class ActivityControllerTest {

    @Autowired
    private ActivityRepository repo;

    @MockBean
    private FileStorageService fileStorageService;

    private ActivityController sut;

    private Activity activity;
    private Activity nullActivity;

    @BeforeEach
    public void setUp() {
        sut = new ActivityController(repo, new Random(), fileStorageService, new ActivityFilter());
        activity = new Activity("00-a", "ss/ss.png", "a", 5, "b");
        nullActivity = new Activity(null, null, null, 0, null);
    }

    @Test
    public void cannotAddNullTitleOrSource() {
        var s = sut.add(nullActivity);
        assertEquals(BAD_REQUEST, s.getStatusCode());
    }

    @Test
    void fetchRandomSimilarity() {
        sut.importActivities(List.of(
            new Activity("A", "ss/ss.png", "flying a plane", 10, "b"),
            new Activity("B", "ss/sds.png", "TITLE", 20, "google.com"),
            new Activity("C", "ss/sda.png", "using a lamp", 80, "bing.com"),
            new Activity("D", "ss/sss.png", "doing something", 100, "yandex.com")
        ));

        List<Activity> res = sut.fetchRandom(2);

        boolean indexZeroSmall = res.get(0).id.equals("A") || res.get(0).id.equals("B");
        boolean indexOneSmall = res.get(1).id.equals("A") || res.get(1).id.equals("B");
        assertSame(indexZeroSmall, indexOneSmall);
    }

    @Test
    void fetchTooMuch() {
        sut.importActivities(List.of(
            new Activity("A", "ss/ss.png", "flying a plane", 10, "b"),
            new Activity("B", "ss/sds.png", "TITLE", 20, "google.com")
        ));

        List<Activity> res = sut.fetchRandom(5);

        assertSame(2, res.size());
    }

    @Test
    public void testAdd() {
        var s = sut.add(activity);
        Activity activity = repo.findById("00-a");

        assertEquals(activity.consumptionInWh, 5);
    }

    @Test
    public void testAddPutImage() {
        MockMultipartFile file = new MockMultipartFile("test", "test.txt", "text/plain", "some test ".getBytes());
        var s = sut.addImage("A", "ss/ss.png", "flying a plane", "10", "b", file);

        assertEquals(OK, s.getStatusCode());
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
        sut = new ActivityController(repo, new RandomDOC(0), fileStorageService, new ActivityFilter());
        sut.importActivities(List.of(new Activity("00-b", "ss/ss.png", "flying a plane", 10, "b")));
        List<Question> res = new ArrayList<>();
        sut.generateTrueFalseQuestion(0, res);
        Question q = res.get(0);

        assertEquals("flying a plane consumes 30Wh.", q.getQuestion());
        assertSame(1, q.getCorrectAnswer());
    }

    @Test
    void trueFalseQuestionType2() {
        sut = new ActivityController(repo, new RandomDOC(1), fileStorageService, new ActivityFilter());
        sut.importActivities(List.of(
            new Activity("00-b", "ss/ss.png", "flying a plane", 10, "b"),
            new Activity("054-b", "ss/sds.png", "TITLE", 15, "google.com"))
        );
        List<Question> res = new ArrayList<>();
        sut.generateTrueFalseQuestion(0, res);
        Question q = res.get(0);

        if (q.getQuestion().equals("flying a plane consumes more than TITLE.")) {
            assertSame(1, q.getCorrectAnswer());
        } else if (q.getQuestion().equals("TITLE consumes more than flying a plane.")) {
            assertSame(0, q.getCorrectAnswer());
        } else {
            fail();
        }
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
    public void testDeleteNotFound() {
        var s = sut.deleteActivity(nullActivity);
        assertEquals(BAD_REQUEST, s.getStatusCode());
    }

    @Test
    public void testDelete() {
        var s = sut.add(activity);
        sut.deleteActivity(activity);
        assertEquals(activity, s.getBody());
    }

    @Test
    public void testUpdate() {
        sut.add(activity);
        activity.title = "test";

        sut.updateActivity(activity);

        Activity newActivity = repo.findById(activity.id);
        assertEquals("test", newActivity.title);
    }

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
        assertEquals(13, q.getActivities().get(0).consumptionInWh);
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

        assertEquals("What can you do instead of using a lamp?", q.getQuestion());
        assertEquals("flying a plane", q.getActivities().get(q.getCorrectAnswer()).title);
    }
}