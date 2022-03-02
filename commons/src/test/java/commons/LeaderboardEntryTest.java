package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaderboardEntryTest {

    private LeaderboardEntry sut;

    @BeforeEach
    public void setup() {
        sut = new LeaderboardEntry("James", 24);
    }

    @Test
    void getName() {
        assertEquals("James", sut.getName());
        assertNotNull(sut.getName());
    }

    @Test
    void getScore() {
        assertSame(24, sut.getScore());
    }

    @Test
    void getScoreString() {
        assertEquals("24", sut.getScoreString());
        assertNotNull(sut.getScoreString());
        assertNotEquals(24, sut.getScoreString());
    }

    @Test
    void compareToSmaller() {
        LeaderboardEntry other = new LeaderboardEntry("Michael", 33);
        assertTrue(sut.compareTo(other) > 0);
    }

    @Test
    void compareToGreater() {
        LeaderboardEntry other = new LeaderboardEntry("Michael", 11);
        assertTrue(sut.compareTo(other) < 0);
    }

    @Test
    void compareToEqual() {
        LeaderboardEntry other = new LeaderboardEntry("Michael", 24);
        assertEquals(0, sut.compareTo(other));
    }

    @Test
    void sortDescending() {
        List<LeaderboardEntry> unsorted = List.of(sut, new LeaderboardEntry("Michael", 35));
        List<LeaderboardEntry> sorted = unsorted.stream().sorted().collect(Collectors.toList());

        assertNotEquals(sorted, unsorted);
        assertEquals(sorted.get(0), unsorted.get(1));
        assertEquals(sorted.get(1), unsorted.get(0));
    }

    @Test
    void testEqualsTrue() {
        LeaderboardEntry other = new LeaderboardEntry("James", 24);
        assertEquals(sut, other);
    }

    @Test
    void testEqualsFalseScore() {
        LeaderboardEntry other = new LeaderboardEntry("James", 22);
        assertNotEquals(sut, other);
    }

    @Test
    void testEqualsFalseName() {
        LeaderboardEntry other = new LeaderboardEntry("Jame", 24);
        assertNotEquals(sut, other);
    }

    @Test
    void testEqualsFalseNullField() {
        LeaderboardEntry other = new LeaderboardEntry(null, 24);
        assertNotEquals(sut, other);
    }

    @Test
    void testEqualsFalseNull() {
        LeaderboardEntry other = null;
        assertNotEquals(sut, other);
    }

    @Test
    void testHashCodeEqual() {
        LeaderboardEntry other = new LeaderboardEntry("James", 24);
        assertEquals(sut.hashCode(), other.hashCode());
    }

    @Test
    void testHashCodeNotEqual() {
        LeaderboardEntry other = new LeaderboardEntry("Jame", 24);
        assertNotEquals(sut.hashCode(), other.hashCode());
    }
}