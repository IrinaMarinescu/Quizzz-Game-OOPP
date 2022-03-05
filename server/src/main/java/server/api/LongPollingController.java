package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller has functions needed for long polling
 */
@RestController
@RequestMapping("/poll")
public class LongPollingController {

    private final ObjectMapper mapper = new ObjectMapper();
    private String json;

    /**
     * This is where front-end sends a request which gets stored
     *
     * @return A JSON string corresponding to the result
     */
    @GetMapping(path = {"", "/"})
    synchronized public ResponseEntity<String> receivePoll() {
        try {
            wait();
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
     * TODO: Think of all necessary request types
     *
     * @param type          The type of the request (Possible values: "JOKER", "EMOJI", ???)
     * @param keyValuePairs The key value pairs in generated JSON String
     *                      <p>
     *                      <p>
     *                      EXAMPLE:
     *                      <p>
     *                      <p>
     *                      dispatch("JOKER", Pair.of("name", "Per"), Pair.of("reaction", "happy"))
     *                      <p>
     *                      <p>
     *                      WILL RESULT IN FOLLOWING JSON STRING:
     *                      <p>
     *                      <p>
     *                      {"type":"EMOJI","name":"Per","reaction":"happy"}
     *                      <p>
     *                      <p>
     */
    @SafeVarargs
    final synchronized public void dispatch(String type, Pair<String, String>... keyValuePairs) {
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
