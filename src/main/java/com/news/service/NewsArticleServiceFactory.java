package com.news.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.news.dbconfig.CassandraConfig;

/**
 * @author Robson Martins
 * 
 * Factory Pattern
 */
public class NewsArticleServiceFactory {
    private static NewsArticleServiceFactory service = new NewsArticleServiceFactory();

    @Autowired
    private Environment env;

    public NewsArticleService createService() {

        if (env.getProperty(CassandraConfig.CASSANDRA_URL) != null) {
            return new NewsArticleServiceCassandra();
        }
        
        //.. others databases

        throw new UnsupportedOperationException("Unknown Database Type");
    }

    public static NewsArticleServiceFactory createFactory() {
        return service;
    }
}
