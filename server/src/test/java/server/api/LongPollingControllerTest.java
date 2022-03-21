//package server.api;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.junit.jupiter.api.Assertions.fail;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicInteger;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.util.Pair;
//import org.springframework.http.ResponseEntity;
//
///**
// * Tests for long polling functions on server side
// */
//public class LongPollingControllerTest {
//
//    private LongPollingController sut;
//
//    @BeforeEach
//    void setup() {
//        sut = new LongPollingController();
//    }
//
//    @Test
//    void dispatchJSONNoArgs() {
//        sut.dispatch(0, "HELLO");
//
//        assertEquals("{\"type\":\"HELLO\"}", sut.json);
//    }
//
//    @Test
//    void dispatchJSONTwoArgs() {
//        sut.dispatch(0, "HELLO", Pair.of("beautiful", "world"), Pair.of("lorem", "ipsum"));
//
//        assertEquals("{\"type\":\"HELLO\",\"beautiful\":\"world\",\"lorem\":\"ipsum\"}", sut.json);
//    }
//
//    @Test
//    void dispatchId() {
//        assertSame(0, sut.receivingGameId);
//        sut.dispatch(7, "TEST");
//        assertSame(7, sut.receivingGameId);
//    }
//
//    /**
//     * WARNING:
//     * <p>
//     * Tests below involve freezing threads and a subsequent events to unfreeze them.
//     * Time and threads had to be involved to test these methods,
//     * since they are important and there is no other meaningful way to test them.
//     */
//    @Test
//    void notifyTest() {
//        CompletableFuture.delayedExecutor(10, TimeUnit.MILLISECONDS).execute(() -> {
//            sut.dispatch(0, "TEST");
//        });
//        ResponseEntity<String> res = sut.receivePoll(0);
//        assertEquals(ResponseEntity.ok("{\"type\":\"TEST\"}"), res);
//    }
//
//    @Test
//    void waitUntilDispatchCalled() {
//        AtomicBoolean noDispatchYet = new AtomicBoolean(true);
//        CompletableFuture.delayedExecutor(10, TimeUnit.MILLISECONDS).execute(() -> {
//            sut.dispatch(0, "TEST");
//            noDispatchYet.set(false);
//        });
//
//        sut.receivePoll(0);
//        // Getting to this point is only possible if the thread is unfrozen
//        if (noDispatchYet.get()) {
//            fail();
//        }
//    }
//
//    @Test
//    void receivingGameIdMismatch() {
//        AtomicInteger senderId = new AtomicInteger(-1);
//        CompletableFuture.delayedExecutor(10, TimeUnit.MILLISECONDS).execute(() -> {
//            sut.dispatch(0, "TEST");
//            senderId.set(0);
//        });
//        CompletableFuture.delayedExecutor(20, TimeUnit.MILLISECONDS).execute(() -> {
//            sut.dispatch(6, "TEST");
//            senderId.set(6);
//        });
//
//        sut.receivePoll(6);
//        // The assertion is only reached when the delayedExecutor with matching gameId dispatches
//        assertSame(6, senderId.get());
//    }
//
//}
