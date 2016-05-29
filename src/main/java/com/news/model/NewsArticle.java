package com.news.model;

import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

/**
 * @author Robson Martins
 */
@SolrDocument(solrCoreName = "news")
public class NewsArticle {
    public NewsArticle() {
    }

    public NewsArticle(NewsArticle from) {
        this.id = from.id;
        this.title = from.title;
        this.text_content = from.text_content;
        this.createdTimestamp = from.createdTimestamp;
    }

    @Indexed
    private Long id;

    @Indexed
    private String title;

    @Indexed
    private String text_content;

    private Date createdTimestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText_content() {
		return text_content;
	}

	public void setText_content(String text_content) {
		this.text_content = text_content;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
