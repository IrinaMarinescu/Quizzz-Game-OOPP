package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PairTest {

    Pair<Integer, Integer> pair1;
    Pair<Integer, Integer> pair2;
    Pair<Integer, Integer> pair3;

    @BeforeEach
    public void setUp() {
        pair1 = new Pair<Integer, Integer>(3, 10);
        pair2 = new Pair<Integer, Integer>(1, 10);
        pair3 = new Pair<Integer, Integer>(1, 10);
    }

    @Test
    public void constructorTest() {
        assertNotNull(pair1);
        assertNotNull(pair2);
    }

    @Test
    public void getKeyTest() {
        assertSame(1, pair2.getKey());
    }

    @Test
    public void setKeyTest() {
        pair2.setKey(2);
        assertSame(2, pair2.getKey());
    }

    @Test
    public void getValueTest() {
        assertSame(10, pair2.getValue());
    }

    @Test
    public void setValueTest() {
        pair2.setValue(20);
        assertSame(20, pair2.getValue());
    }

    @Test
    public void equalsSameObj() {
        assertEquals(pair1, pair1);
    }

    @Test
    public void equalsTest() {
        assertEquals(pair2, pair3);
    }

    @Test
    public void equalsDifferentTest() {
        assertNotEquals(pair1, pair2);
    }
}
