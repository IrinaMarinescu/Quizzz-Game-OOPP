package commons;

import java.util.List;
import java.util.UUID;

public class Game {

    private UUID id;
    private List<Question> questions;
    private List<LeaderboardEntry> players;
    private int round;

    public Game(UUID id, List<Question> questions, List<LeaderboardEntry> players) {
        this.id = id;
        this.questions = questions;
        this.players = players;
        this.round = 1;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<LeaderboardEntry> getPlayers() {
        return players;
    }

    public UUID getId() {
        return id;
    }

    public int getRound() {
        return round;
    }


}
