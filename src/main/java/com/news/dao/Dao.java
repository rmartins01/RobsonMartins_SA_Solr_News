package com.news.dao;

import java.util.List;

import com.news.model.Entity;

public interface Dao<T extends Entity, I> {

	List<T> findAll();

	T find(I id);

	T save(T newsEntry);

	void delete(I id);

}