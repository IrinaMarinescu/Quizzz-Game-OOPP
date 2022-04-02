package commons;

import java.util.List;
import java.util.UUID;

/**
 * Holds information about an ongoing game
 */
public class Game {

    private UUID id;
    private List<Question> questions;
    private List<LeaderboardEntry> players;
    private int round;


    /**
     * Empty constructor need to create an instance from JSON file
     */
    public Game() {
    }

    /**
     * Constructor
     *
     * @param id        The id of the game
     * @param questions The questions in this game
     * @param players   The player of this game
     */
    public Game(UUID id, List<Question> questions, List<LeaderboardEntry> players) {
        this.id = id;
        this.questions = questions;
        this.players = players;
        this.round = 0;
    }

    /**
     * Returns all questions
     *
     * @return A list containing all questions
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * @return A list of all players (corresponding LeaderboardEntries)
     */
    public List<LeaderboardEntry> getPlayers() {
        return players;
    }

    /**
     * Sets players
     *
     * @param players A list of players (corresponding LeaderboardEntries)
     */
    public void setPlayers(List<LeaderboardEntry> players) {
        this.players = players;
    }

    /**
     * @return The ID of this game
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return The current round
     */
    public int getRound() {
        return round;
    }

    /**
     * Increments the number of the round
     */
    public void incrementRound() {
        round++;
    }

    /**
     * @return The next question, based on current round
     */
    public Question nextQuestion() {
        return questions.get(round - 1);
    }

    /**
     * Update score of the given player
     *
     * @param player The player that score should be updated
     * @return The updated list of players
     */
    public void updateScores(LeaderboardEntry player) {
        for (LeaderboardEntry p : this.players) {
            if (p.hasSameName(player)) {
                p.setScore(player.getScore());
                return;
            }
        }
    }
}
