/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.decoratoralapucache;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

/**
 *
 * @author mcsab
 */

class CachedContentSourceContractTest
        extends ContentSourceContractTest {

    @Override
    protected ContentSource createSource() {
        ContentSource inner = new MockContentSource(List.of(
                new News("Test", "https://example.test/1", "Minta", "")
        ));

        return new CachedContentSource(
                inner,
                Duration.ofMinutes(5),
                Clock.fixed(Instant.EPOCH, ZoneOffset.UTC));
    }
}