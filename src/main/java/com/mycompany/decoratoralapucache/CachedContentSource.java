/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.decoratoralapucache;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
/**
 *
 * @author mcsab
 */
public final class CachedContentSource
        implements ContentSource, ForceRefresh {
    private final ContentSource inner;
    private final Duration ttl;
    private final Clock clock;

    private List<News> cached;
    private Instant storedAt;

    public CachedContentSource(
            ContentSource inner, Duration ttl, Clock clock) {
        if (inner == null || ttl == null || ttl.isZero()
                || ttl.isNegative() || clock == null) {
            throw new IllegalArgumentException(
                    "Forrás, pozitív TTL és óra szükséges.");
        }
        this.inner = inner;
        this.ttl = ttl;
        this.clock = clock;
    }

    @Override
    public String name() {
        return inner.name() + " (cached)";
    }

    @Override
    public synchronized List<News> load() throws SourceException {
        if (cacheErvenyes()) {
            return cached;
        }

        List<News> fresh = List.copyOf(inner.load());
        cached = fresh;
        storedAt = clock.instant();
        return cached;
    }

    @Override
    public synchronized List<News> loadFresh() throws SourceException {
        List<News> fresh = List.copyOf(inner.load());
        cached = fresh;
        storedAt = clock.instant();
        return cached;
    }

    private boolean cacheErvenyes() {
        return cached != null
                && storedAt.plus(ttl).isAfter(clock.instant());
    }
}
