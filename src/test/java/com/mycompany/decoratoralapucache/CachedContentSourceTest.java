/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.decoratoralapucache;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author mcsab
 */
class CachedContentSourceTest {
    private static final class TestClock extends Clock {
        private Instant now;

        TestClock(Instant now) {
            this.now = now;
        }

        void advance(Duration duration) {
            now = now.plus(duration);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return now;
        }
    }

    private News news(String title) {
        return new News("Test", "https://example.test/" + title,
                title, "");
    }

    @Test
    void ttlLejarataigCacheTalalatUtanUjratolt() throws Exception {
        TestClock clock = new TestClock(Instant.EPOCH);
        AtomicInteger calls = new AtomicInteger();

        ContentSource inner = new ContentSource() {
            public String name() {
                return "Counter";
            }

            public List<News> load() {
                return List.of(news("hír-" + calls.incrementAndGet()));
            }
        };

        CachedContentSource cache =
                new CachedContentSource(inner, Duration.ofMinutes(10), clock);

        assertEquals("hír-1", cache.load().get(0).title());
        clock.advance(Duration.ofMinutes(9));
        assertEquals("hír-1", cache.load().get(0).title());
        clock.advance(Duration.ofMinutes(1));
        assertEquals("hír-2", cache.load().get(0).title());
        assertEquals(2, calls.get());
    }

    @Test
    void forceRefreshMegkeruliAzErvenyesCachet() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        ContentSource inner = new ContentSource() {
            public String name() {
                return "Counter";
            }

            public List<News> load() {
                return List.of(news("hír-" + calls.incrementAndGet()));
            }
        };

        CachedContentSource cache = new CachedContentSource(
                inner, Duration.ofDays(1),
                Clock.fixed(Instant.EPOCH, ZoneOffset.UTC));

        cache.load();
        assertEquals("hír-2", cache.loadFresh().get(0).title());
        assertEquals("hír-2", cache.load().get(0).title());
    }

    @Test
    void lejartCacheUtanForrashibaTovabbDobodik() throws Exception {
        TestClock clock = new TestClock(Instant.EPOCH);
        AtomicInteger calls = new AtomicInteger();

        ContentSource inner = new ContentSource() {
            public String name() {
                return "Sometimes failing";
            }

            public List<News> load() throws SourceException {
                if (calls.incrementAndGet() > 1) {
                    throw new SourceException("offline");
                }
                return List.of(news("első"));
            }
        };

        CachedContentSource cache =
                new CachedContentSource(inner, Duration.ofMinutes(1), clock);

        assertEquals("első", cache.load().get(0).title());
        clock.advance(Duration.ofMinutes(2));

        SourceException error = assertThrows(
                SourceException.class, cache::load);
        assertEquals("offline", error.getMessage());
    }

    @Test
    void aVisszaadottListaNemModosithato() throws Exception {
        CachedContentSource cache = new CachedContentSource(
                new MockContentSource(List.of(news("minta"))),
                Duration.ofMinutes(1),
                Clock.systemUTC());

        assertThrows(UnsupportedOperationException.class,
                () -> cache.load().add(news("új")));
    }

    @Test
    void tobbszaluOlvasasEgyBetoltestIndit() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        CountDownLatch loadStarted = new CountDownLatch(1);
        CountDownLatch allowLoadToFinish = new CountDownLatch(1);

        ContentSource inner = new ContentSource() {
            public String name() {
                return "Blocking source";
            }

            public List<News> load() {
                calls.incrementAndGet();
                loadStarted.countDown();
                try {
                    assertTrue(allowLoadToFinish.await(2, TimeUnit.SECONDS));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
                return List.of(news("egyszer"));
            }
        };

        CachedContentSource cache = new CachedContentSource(
                inner, Duration.ofMinutes(1), Clock.systemUTC());
        var pool = Executors.newFixedThreadPool(4);

        try {
            List<java.util.concurrent.Future<List<News>>> futures =
                    new ArrayList<>();

            futures.add(pool.submit(cache::load));
            assertTrue(loadStarted.await(2, TimeUnit.SECONDS));

            for (int i = 0; i < 3; i++) {
                futures.add(pool.submit(cache::load));
            }

            allowLoadToFinish.countDown();

            for (var future : futures) {
                assertEquals("egyszer", future.get().get(0).title());
            }
            assertEquals(1, calls.get());
        } finally {
            allowLoadToFinish.countDown();
            pool.shutdownNow();
        }
    }
}
