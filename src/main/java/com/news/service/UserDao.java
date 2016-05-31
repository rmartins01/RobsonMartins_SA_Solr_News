package com.news.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.news.dao.Dao;
import com.news.model.User;

public interface UserDao extends Dao<User, Long>, UserDetailsService {

	User findByName(String name);

}