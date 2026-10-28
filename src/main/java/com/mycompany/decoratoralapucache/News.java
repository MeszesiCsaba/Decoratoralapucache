/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.decoratoralapucache;

/**
 *
 * @author mcsab
 */
public record News(String source, String url, String title, String description) {
    public News {
        if (source == null || source.isBlank()
                || url == null || url.isBlank()
                || title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "A forrás, az URL és a cím kötelező.");
        }
        description = description == null ? "" : description;
    }
}
