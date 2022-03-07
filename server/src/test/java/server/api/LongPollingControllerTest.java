package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;

/**
 * Tests for long polling functions on server side
 */
public class LongPollingControllerTest {

    private LongPollingController sut;

    @BeforeEach
    void setup() {
        sut = new LongPollingController();
    }

    @Test
    void dispatchJSONNoArgs() {
        sut.dispatch("HELLO");

        assertEquals("{\"type\":\"HELLO\"}", sut.json);
    }

    @Test
    void dispatchJSONTwoArgs() {
        sut.dispatch("HELLO", Pair.of("beautiful", "world"), Pair.of("lorem", "ipsum"));

        assertEquals("{\"type\":\"HELLO\",\"beautiful\":\"world\",\"lorem\":\"ipsum\"}", sut.json);
    }

    /**
     * WARNING:
     * <p>
     * The method receivePoll involves freezing threads and a subsequent event to unfreeze that thread
     * I had to involve time and threads to test these methods,
     * since they are important and there is no other meaningful way
     */
    @Test
    void notifyTest() {
        CompletableFuture.delayedExecutor(10, TimeUnit.MILLISECONDS).execute(() -> {
            sut.dispatch("TEST");
        });
        ResponseEntity<String> res = sut.receivePoll();
        assertEquals(ResponseEntity.ok("{\"type\":\"TEST\"}"), res);
    }

    @Test
    void waitDispatchCalled() {
        AtomicBoolean frozen = new AtomicBoolean(true);
        CompletableFuture.delayedExecutor(10, TimeUnit.MILLISECONDS).execute(() -> {
            sut.dispatch("TEST");
            frozen.set(false);
        });

        ResponseEntity<String> res = sut.receivePoll();
        // Getting to this point is only possible if the thread is unfrozen
        if (frozen.get()) {
            fail();
        }
    }
}
