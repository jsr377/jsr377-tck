/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2015-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.application.event.tck;

import org.junit.jupiter.api.Test;

import javax.application.event.EventBus;
import javax.application.event.EventHandler;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Andres Almiray
 */
public abstract class EventBusTest {
    protected abstract EventBus resolveEventBus();

    @Test
    public void subscribe_and_unsubscribe() {
        // given:
        EventBus eventBus = resolveEventBus();
        TestEvent1Handler eventHandler = new TestEvent1Handler();
        eventBus.subscribe(eventHandler);
        eventBus.unsubscribe(eventHandler);

        //when:
        eventBus.publishEvent(new Event1());
        eventBus.publishEvent(new Event2());

        // then:
        assertNull(eventHandler.event);
    }

    @Test
    public void publish_event_synchronously() {
        // given:
        EventBus eventBus = resolveEventBus();
        TestEvent1Handler eventHandler = new TestEvent1Handler();
        eventBus.subscribe(eventHandler);

        //when:
        eventBus.publishEvent(new Event1());
        eventBus.publishEvent(new Event2());

        // then:
        assertTrue(eventHandler.event instanceof Event1);
    }

    @Test
    public void publish_event_asynchronously() throws Exception {
        // given:
        EventBus eventBus = resolveEventBus();
        TestEvent1Handler eventHandler = new TestEvent1Handler();
        eventBus.subscribe(eventHandler);

        //when:
        eventBus.publishEventAsync(new Event1());
        eventBus.publishEventAsync(new Event2());
        Thread.sleep(200L);

        // then:
        assertTrue(eventHandler.event instanceof Event1);
    }

    public static abstract class AbstractTestEventHandler {
        protected Event event;
        protected int called;
    }

    public static class TestEvent1Handler extends AbstractTestEventHandler {
        @EventHandler
        public void handleEvent1(Event1 event) {
            this.event = event;
            this.called++;
        }
    }

    public static class TestEvent2Handler extends AbstractTestEventHandler {
        @EventHandler
        public void handleEvent2(Event2 event) {
            this.event = event;
            this.called++;
        }
    }

    public static class TestEventHandler extends AbstractTestEventHandler {
        @EventHandler
        public void handleEvent1(Event1 event) {
            this.event = event;
            this.called++;
        }

        @EventHandler
        public void handleEvent2(Event2 event) {
            this.event = event;
            this.called++;
        }
    }

    public interface Event {

    }

    public static class Event1 implements Event {
    }

    public static class Event2 implements Event {
    }
}
