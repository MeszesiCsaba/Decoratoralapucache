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
public final class NewsService {
    private final ContentSource source;
    private final ForceRefresh forceRefresh;

    public NewsService(ContentSource source, ForceRefresh forceRefresh) {
        this.source = source;
        this.forceRefresh = forceRefresh;
    }

    public List<News> load(boolean force) throws SourceException {
        return force ? forceRefresh.loadFresh() : source.load();
    }

    public String sourceName() {
        return source.name();
    }
}
