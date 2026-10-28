/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.decoratoralapucache;
import java.util.List;

/**
 *
 * @author mcsab
 */
public final class MockContentSource implements ContentSource {
    private final List<News> news;

    public MockContentSource(List<News> news) {
        this.news = List.copyOf(news);
    }

    @Override
    public String name() {
        return "Mock (" + news.size() + " news)";
    }

    @Override
    public List<News> load() {
        return List.copyOf(news);
    }
}
