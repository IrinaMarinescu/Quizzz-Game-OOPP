package server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ActivityFilterTest {

    private ActivityFilter act;
    private Activity testActivity1;
    private Activity testActivity2;
    private Activity testActivity3;


    @BeforeEach
    public void setUp() {
        act = new ActivityFilter();
        testActivity1 = new Activity("washing-dishes", null, "How much does washing the dishes consume?", 500, null);
        testActivity2 =
            new Activity("washing-dishes", null, "Using the dishwasher monthly daily for 1 hour.", 500, null);
        testActivity3 =
            new Activity("washing-dishes", null, "using the dishwasher monthly daily for 1 hour.", 500, null);
    }

    @Test
    void handlingHowQuestions() {

        act.handlingHowQuestions(testActivity1);
        assertEquals("washing the dishes", testActivity1.title);
    }

    @Test
    void handlingMonthlyActivities() {

        act.handlingPunctuation(testActivity2);
        act.handlingPunctuation(testActivity3);
        act.handlingMonthlyActivities(testActivity2);
        act.handlingMonthlyActivities(testActivity3);
        assertEquals("Using the dishwasher daily for 1 hour for a month", testActivity2.title);
        assertEquals("using the dishwasher daily for 1 hour for a month", testActivity3.title);
    }

    @Test
    void handlingDailyActivities() {

        act.handlingPunctuation(testActivity2);
        act.handlingPunctuation(testActivity3);
        act.handlingDailyActivities(testActivity2);
        act.handlingDailyActivities(testActivity3);
        assertEquals("Using the dishwasher monthly for 1 hour for a day", testActivity2.title);
        assertEquals("using the dishwasher monthly for 1 hour for a day", testActivity3.title);
    }

    @Test
    void handlingPunctuation() {

        act.handlingPunctuation(testActivity1);
        act.handlingPunctuation(testActivity2);
        act.handlingPunctuation(testActivity3);
        assertEquals("How much does washing the dishes consume", testActivity1.title);
        assertEquals("Using the dishwasher monthly daily for 1 hour", testActivity2.title);
        assertEquals("using the dishwasher monthly daily for 1 hour", testActivity3.title);
    }

    @Test
    void changingToUpperCase() {

        act.changingToUpperCase(testActivity3);
        assertEquals("Using the dishwasher monthly daily for 1 hour.", testActivity3.title);
    }

    @Test
    void changingToLowerCase() {

        act.changingToLowerCase(testActivity2);
        assertEquals("using the dishwasher monthly daily for 1 hour.", testActivity2.title);
    }
}