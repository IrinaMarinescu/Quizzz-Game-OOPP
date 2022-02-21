package commons;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class ActivityTest {

    public Activity someActivity;

    @BeforeEach
    void setUp() {
        someActivity = new Activity("00-a","ss/ss.png", "a", 40000, "b");
    }

    @Test
    public void checkConstructor() {
        var test = new Activity("00-a","ss/ss.png", "a", 40000, "b");
        assertEquals(test.title, someActivity.title);
        assertNotEquals(test.consumptionInWh, someActivity.consumptionInWh);
        assertEquals(test.source, someActivity.source);
    }

    @Test
    public void equalsHashCode() {
        var a = new Activity("00-a","ss/ss.png", "a", 40000, "b");
        assertEquals(a, someActivity);
        assertEquals(a.hashCode(), someActivity.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new Activity("00-b", "aa/aa.png", "b", 10, "c");
        assertNotEquals(a, someActivity);
        assertNotEquals(a.hashCode(), someActivity.hashCode());
    }
}