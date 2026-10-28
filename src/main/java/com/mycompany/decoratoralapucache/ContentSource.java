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
public interface ContentSource {
    String name();

    List<News> load() throws SourceException;
}
