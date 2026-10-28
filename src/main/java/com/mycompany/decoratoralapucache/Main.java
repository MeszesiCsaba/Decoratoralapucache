/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.decoratoralapucache;
import java.time.Clock;
import java.time.Duration;
import java.util.List;

/**
 *
 * @author mcsab
 */
public final class Main {
    private Main() {}

    public static void main(String[] args) {
        boolean force = List.of(args).contains("--force");

        ContentSource rawSource = new MockContentSource(List.of(
                new News("Demo", "https://example.test/1",
                        "Első hír", "Cache-minta.")
        ));

        CachedContentSource cache = new CachedContentSource(
                rawSource, Duration.ofMinutes(10), Clock.systemUTC());

        NewsService service = new NewsService(cache, cache);

        try {
            System.out.println(service.sourceName());
            service.load(force).forEach(System.out::println);
        } catch (SourceException e) {
            System.err.println("Forráshiba: " + e.getMessage());
        }
    }
}
