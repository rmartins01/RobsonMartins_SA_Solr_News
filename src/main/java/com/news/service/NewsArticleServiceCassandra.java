package com.news.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.news.model.NewsArticle;
import com.news.model.NewsArticleNoSQL;

/**
 * @author Robson Martins
 */
public class NewsArticleServiceCassandra implements NewsArticleService {
    @Autowired
    private CassandraTemplate cassandraTemplate;

    private AtomicLong nextId = new AtomicLong(1);

    @Override
    public List<NewsArticle> getAllRows() {
        List<NewsArticle> result = new ArrayList<NewsArticle>();
        result.addAll(cassandraTemplate.selectAll(NewsArticleNoSQL.class));

        for (NewsArticle obj : result) {
            while (obj.getId() > nextId.longValue()) {
                nextId.compareAndSet(nextId.longValue(), obj.getId());
            }
        }

        return result;
    }

    @Override
    public NewsArticleNoSQL getById(Long id) {
        Select select = QueryBuilder.select().from(NewsArticleNoSQL.class.getSimpleName());
        select.where(QueryBuilder.eq("id", id));
        NewsArticleNoSQL from = cassandraTemplate.selectOne(select, NewsArticleNoSQL.class);
        return new NewsArticleNoSQL(from);
    }

    public Long add(NewsArticle nd) {
    	NewsArticleNoSQL obj = new NewsArticleNoSQL();
    	obj.setId(nd.getId());
    	obj.setTitle(nd.getTitle());
    	obj.setText_content(nd.getText_content());
    	obj.setCreatedTimestamp(nd.getCreatedTimestamp());
    	return add((obj));
    }
    @Override
    public Long add(NewsArticleNoSQL nd) {
        this.getAllRows();

        NewsArticleNoSQL item = new NewsArticleNoSQL(nd);

        Long id = this.getNextSequence();
        Date createdTimestamp = new Date();

        item.setId(id);
        item.setObjectId(UUID.randomUUID().toString());
        item.setCreatedTimestamp(createdTimestamp);

        nd.setId(id);
        nd.setCreatedTimestamp(createdTimestamp);

        cassandraTemplate.insert(item);

        return id;
    }

    @Override
    public void deleteById(Long id) {
    	NewsArticleNoSQL item = this.getById(id);
        cassandraTemplate.delete(item);
    }

    @Override
    public void deleteAll() {
        cassandraTemplate.deleteAll(NewsArticleNoSQL.class);
    }

    private Long getNextSequence() {
        return nextId.incrementAndGet();
    }
}
