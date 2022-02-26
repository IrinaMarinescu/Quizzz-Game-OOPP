package commons;

/**
 * Wrapper for an entry in a (any) leaderboard
 */
public class LeaderboardEntry implements Comparable<LeaderboardEntry> {

    private String name;
    private Integer score;

    /**
     * Constructor
     *
     * @param name  Name of player
     * @param score Score of player
     */
    public LeaderboardEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Getter of name field
     *
     * @return Value of name field
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of score field
     *
     * @return Value of score field
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter of score field (as a string)
     *
     * @return Value of score field (cast to a string)
     */
    public String getScoreString() {
        return score.toString();
    }

    /**
     * Compares this to another leaderboard entry based on the players' score
     *
     * @param other The other LeaderBoard entry to be compared to
     * @return An integer representing the which LeaderboardEntry is greater
     */
    @Override
    public int compareTo(LeaderboardEntry other) {
        return Integer.compare(other.getScore(), score);
    }
}
