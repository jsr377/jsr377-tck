package javax.application.threading;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Andres Almiray
 */
public abstract class ThreadingHandlerTest {
    protected abstract ThreadingHandler resolveThreadingHandler();

    protected abstract boolean isUIThread();

    @Test
    public void verify_executeInsideUIAsync() {
        // expect:
        assertThat(isUIThread(), equalTo(false));

        // given:
        AtomicBoolean threadWitness = new AtomicBoolean();
        AtomicBoolean executeWitness = new AtomicBoolean();
        Runnable task = () -> {
            if (isUIThread()) {
                threadWitness.set(true);
            }
            executeWitness.set(true);
        };

        // when:
        resolveThreadingHandler().executeInsideUIAsync(task);
        await().timeout(2, TimeUnit.SECONDS).until(executeWitness::get, equalTo(true));

        // then:
        assertThat(threadWitness.get(), equalTo(true));
    }

    @Test
    public void verify_executeInsideUISync() {
        // expect:
        assertThat(isUIThread(), equalTo(false));

        // given:
        AtomicBoolean threadWitness = new AtomicBoolean();
        Runnable task = () -> {
            if (isUIThread()) {
                threadWitness.set(true);
            }
        };

        // when:
        resolveThreadingHandler().executeInsideUISync(task);

        // then:
        assertThat(threadWitness.get(), equalTo(true));
    }

    @Test
    public void verify_executeInsideUISync_and_return_value() {
        // expect:
        assertThat(isUIThread(), equalTo(false));

        // given:
        Callable<Boolean> task = this::isUIThread;

        // when:
        Boolean result = resolveThreadingHandler().executeInsideUISync(task);

        // then:
        assertThat(result, equalTo(true));
    }

    @Test
    public void verify_executeOutsideUI() {
        // expect:
        assertThat(isUIThread(), equalTo(false));

        // given:
        AtomicBoolean threadWitness = new AtomicBoolean();
        AtomicBoolean executeWitness = new AtomicBoolean();
        Runnable task = () -> {
            if (!isUIThread()) {
                threadWitness.set(true);
            }
            executeWitness.set(true);
        };

        // when:
        resolveThreadingHandler().executeOutsideUI(task);
        await().timeout(2, TimeUnit.SECONDS).until(executeWitness::get, equalTo(true));

        // then:
        assertThat(threadWitness.get(), equalTo(true));
    }

    @Test
    public void verify_executeOutsideUIAsync() {
        // expect:
        assertThat(isUIThread(), equalTo(false));

        // given:
        AtomicBoolean threadWitness = new AtomicBoolean();
        AtomicBoolean executeWitness = new AtomicBoolean();
        Runnable task = () -> {
            if (!isUIThread()) {
                threadWitness.set(true);
            }
            executeWitness.set(true);
        };

        // when:
        resolveThreadingHandler().executeOutsideUIAsync(task);
        await().timeout(2, TimeUnit.SECONDS).until(executeWitness::get, equalTo(true));

        // then:
        assertThat(threadWitness.get(), equalTo(true));
    }

    @Test
    public void verify_executeOutsideUIAsync_returning_CompletionStage() {
        // expect:
        assertThat(isUIThread(), equalTo(false));

        // given:
        AtomicBoolean executeWitness = new AtomicBoolean();
        Callable<Boolean> task = () -> {
            executeWitness.set(true);
            return !isUIThread();
        };

        // when:
        CompletionStage<Boolean> promise = resolveThreadingHandler().executeOutsideUIAsync(task);
        promise.thenAccept(result -> {
            // then:
            assertThat(result, equalTo(true));
        });
        await().timeout(2, TimeUnit.SECONDS).until(executeWitness::get, equalTo(true));
    }

    @Test
    public void verify_executeInsideUIAsync_returning_CompletionStage() {
        // expect:
        assertThat(isUIThread(), equalTo(false));

        // given:
        AtomicBoolean executeWitness = new AtomicBoolean();
        Callable<Boolean> task = () -> {
            executeWitness.set(true);
            return isUIThread();
        };

        // when:
        CompletionStage<Boolean> promise = resolveThreadingHandler().executeInsideUIAsync(task);
        promise.thenAccept(result -> {
            // then:
            assertThat(result, equalTo(true));
        });
        await().timeout(2, TimeUnit.SECONDS).until(executeWitness::get, equalTo(true));
    }
}
