package com.news.service;

import java.util.List;

import com.news.model.NewsArticle;
import com.news.model.NewsArticleNoSQL;

/**
 * @author Robson Martins
 */
public interface NewsArticleService {

    List<NewsArticle> getAllRows();

    NewsArticle getById(Long id);

    Long add(NewsArticle nd);
    
    Long add(NewsArticleNoSQL nd);

    void deleteById(Long id);

    void deleteAll();
}
