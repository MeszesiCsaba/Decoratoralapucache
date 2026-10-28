/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.decoratoralapucache;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author mcsab
 */
abstract class ContentSourceContractTest {
    protected abstract ContentSource createSource();

    @Test
    void loadNemAdhatNullErteket() throws Exception {
        assertNotNull(createSource().load());
    }

    @Test
    void nevNemLehetUres() {
        assertFalse(createSource().name().isBlank());
    }

    @Test
    void hirekForrasaEsUrljeNemUres() throws Exception {
        for (News news : createSource().load()) {
            assertFalse(news.source().isBlank());
            assertFalse(news.url().isBlank());
        }
    }

    @Test
    void ervenyesForrasNemDobRuntimeKivetelt() {
        assertDoesNotThrow(() -> createSource().load());
    }

    @Test
    void masodikBetoltesNemValtoztatjaMegAzElsoListat()
            throws Exception {
        ContentSource source = createSource();
        List<News> first = source.load();
        List<News> snapshot = List.copyOf(first);

        source.load();

        assertEquals(snapshot, first);
    }

    @Test
    void visszaadottListaNemModosithato() throws Exception {
        List<News> news = createSource().load();

        assertThrows(UnsupportedOperationException.class,
                () -> news.add(new News(
                        "Test", "https://example.test/new", "Új hír", "")));
    }
}