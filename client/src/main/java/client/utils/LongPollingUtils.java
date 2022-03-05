package client.utils;

import static client.utils.ServerUtils.APPLICATION_JSON;

import client.scenes.QuestionFrameCtrl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import javax.inject.Inject;
import org.glassfish.jersey.client.ClientConfig;

/**
 * Controls client-side functionality related to long-polling
 */
public class LongPollingUtils {

    private final ServerUtils serverUtils;
    private final QuestionFrameCtrl questionFrameCtrl;
    private final ObjectMapper mapper = new ObjectMapper();
    private boolean active;

    /**
     * Constructor
     *
     * @param serverUtils       ServerUtils object
     * @param questionFrameCtrl QuestionFrameCtrl object
     */
    @Inject
    public LongPollingUtils(ServerUtils serverUtils, QuestionFrameCtrl questionFrameCtrl) {
        this.serverUtils = serverUtils;
        this.questionFrameCtrl = questionFrameCtrl;
        this.active = false;
    }

    /**
     * Changes whether polling is active on front-end
     *
     * @param pollingActive Whether polling must be active on front-end
     */
    public void setPollingActive(boolean pollingActive) {
        if (!active && pollingActive) {
            new Thread(this::sendPoll).start();
        }
        active = pollingActive;
    }

    /**
     * Executes long-polling loop
     */
    private void sendPoll() {
        String json = ClientBuilder.newClient(new ClientConfig())
            .target(serverUtils.getServerIP()).path("poll")
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<String>() {
            });

        try {
            if (active) {
                JsonNode response = mapper.readTree(json);
                performAction(response);
                sendPoll();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.err.println("Error while parsing JSON from long polling on client-side");
        }
    }

    /**
     * Calls appropriate method based on received JSON string
     *
     * @param response An object generated from the parsed JSON string that allows for value retrieval
     */
    private void performAction(JsonNode response) {
        switch (response.get("type").asText()) {
            case "EMOJI":
                String name = response.get("name").asText();
                String reaction = response.get("reaction").asText();
                questionFrameCtrl.displayNewEmoji(name, reaction);
                break;
            case "JOKER":
                String sort = response.get("sort").asText();
                if (sort.equals("halveTime")) {
                    // This should target mainCtrl at first
                    questionFrameCtrl.halveRemainingTime();
                }
                break;
            default:
                System.err.println("Unknown long polling response type");
                break;
        }
    }
}
