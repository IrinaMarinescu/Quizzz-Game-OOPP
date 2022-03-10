package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller has functions needed for long polling
 */
@RestController
@RequestMapping("/poll")
public class LongPollingController {

    private final ObjectMapper mapper = new ObjectMapper();
    public String json;
    public Integer receivingGameId = 0;

    /**
     * This is where front-end sends a request which gets stored
     *
     * @param gameId The id of the game to which data will be returned to
     * @return A JSON string corresponding to the result
     */
    @GetMapping(path = {"/{gameId}"})
    synchronized ResponseEntity<String> receivePoll(@PathVariable int gameId) {
        try {
            do {
                wait();
            } while (gameId != receivingGameId);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("Thread that was paused due to long polling on server got interrupted");
        }
        return ResponseEntity.ok(json);
    }

    /**
     * This must be called by server-side methods to send data to the client.
     * This generates a JSON String and sends it to all connected players.
     * <p>
     *
     * @param receivingGameId The ID of the game to which the data must be sent to
     * @param type            The type of the request
     *                        (Possible values: "START_MP_GAME", "EMOJI", "HALVE_TIME", "DISCONNECT", "JOIN")
     * @param keyValuePairs   The key value pairs in generated JSON String
     *                        <p>
     *                        <p>
     *                        EXAMPLE:
     *                        <p>
     *                        <p>
     *                        dispatch(4, "EMOJI", Pair.of("name", "Per"), Pair.of("reaction", "happy"))
     *                        <p>
     *                        <p>
     *                        WILL RESULT IN FOLLOWING JSON STRING SENT TO ALL PLAYERS IN LOBBY WITH ID 4:
     *                        <p>
     *                        <p>
     *                        {"type":"EMOJI","name":"Per","reaction":"happy"}
     *                        <p>
     *                        <p>
     */

    @SafeVarargs
    final synchronized void dispatch(int receivingGameId, String type, Pair<String, String>... keyValuePairs) {
        this.receivingGameId = receivingGameId;

        ObjectNode res = mapper.createObjectNode();
        res.put("type", type);
        for (Pair<String, String> pair : keyValuePairs) {
            res.put(pair.getFirst(), pair.getSecond());
        }

        try {
            json = mapper.writeValueAsString(res);
            notifyAll();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.err.println("Error at Long polling JSON parsing on server");
        }
    }
}
