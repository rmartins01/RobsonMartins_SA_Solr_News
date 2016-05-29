package com.news.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.news.model.NewsArticle;
import com.news.service.NewsArticleService;

/**
 * @author Robson Martins
 */
@Controller
@RequestMapping("/news")
public class NewsArticleController {
    @Autowired
    private NewsArticleService newsArticleService;

    @Autowired
    private SolrTemplate solrTemplate;

    @RequestMapping("/getList.json")
    @ResponseBody
    public List<NewsArticle> getList() {
        return newsArticleService.getAllRows();
    }

    @RequestMapping(value = "/search.json", method = RequestMethod.POST)
    @ResponseBody
    public List<NewsArticle> doSearch(@RequestBody LinkedHashMap<String, Object> data) {
    	String text = (String) data.get("text");
        if (StringUtils.isEmpty(text) || solrTemplate == null) {
            return this.getList();
        }
        
        int endStr = text.indexOf(" ")-1 >0 ? text.indexOf(" "): text.length();
        
        String queryString = String.format("title:%s", text.substring(0, endStr));//Get only the first word
        Boolean onSelectNewsArticle = (Boolean) data.get("onSelectNewsArticle");
        if(!onSelectNewsArticle)
        	queryString = String.format("title:*%s* OR text_content:*%s*", text, text);//Get all
        
        Query query = new SimpleQuery(new SimpleStringCriteria(queryString));
        return solrTemplate.queryForPage(query, NewsArticle.class).getContent();
    }

    @RequestMapping(value = "/autocomplete.json", method = RequestMethod.POST)
    @ResponseBody
    public List<String> doTypeahead(@RequestBody String text) {
        if (StringUtils.isEmpty(text) || solrTemplate == null) {
            return Collections.emptyList();
        }

        String queryString = String.format("title:*%s*", text);
        Query query = new SimpleQuery(new SimpleStringCriteria(queryString));
        List<NewsArticle> content = solrTemplate.queryForPage(query, NewsArticle.class).getContent();
        List<String> result = new ArrayList<String>();

        for (NewsArticle nd : content) {
            result.add(nd.getTitle());
        }
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public void add(@RequestBody NewsArticle nd) {
        Long id = newsArticleService.add(nd);
        
        if (this.isUseSolr()) {
            solrTemplate.saveBean(new NewsArticle(newsArticleService.getById(id)));
            solrTemplate.commit();
        }
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("id") Long id) {
        newsArticleService.deleteById(id);

        if (this.isUseSolr()) {
            solrTemplate.deleteById(String.valueOf(id));
            solrTemplate.commit();
        }
    }

    @RequestMapping(value = "/removeAll", method = RequestMethod.DELETE)
    @ResponseBody
    public void removeAll() {
        newsArticleService.deleteAll();

        if (this.isUseSolr()) {
            solrTemplate.delete(new SimpleQuery(new SimpleStringCriteria("*:*")));
            solrTemplate.commit();
        }
    }

    @RequestMapping("/layout")
    public String getPartialPage() {
        if (this.isUseSolr()) {
            return "news/layout_search";
        }
        return "news/layout";
    }

    protected boolean isUseSolr() {
        return solrTemplate != null;
    }
}
