package com.news.model;

import com.datastax.driver.core.DataType;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Table;

/**
 * @author Robson Martins
 */
@Table
public class NewsArticleNoSQL extends NewsArticle {
    public NewsArticleNoSQL() {
    }

    public NewsArticleNoSQL(NewsArticleNoSQL from) {
        super(from);
        this.objectId = from.objectId;
    }

    @Id
    private String objectId;

    @Override
    @CassandraType(type = DataType.Name.INT)
    public Long getId() {
        return super.getId();
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
