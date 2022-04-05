package server.dependedoncomponents;

import commons.Game;
import commons.LeaderboardEntry;
import commons.Question;
import java.util.List;
import java.util.UUID;
import server.api.LobbyController;

public class LobbyCtrlDOC extends LobbyController {

    public UUID testId = UUID.randomUUID();

    @Override
    public Game createGame(List<Question> questions) {
        return new Game(testId, questions, List.of(new LeaderboardEntry("James", 7)));
    }
}
