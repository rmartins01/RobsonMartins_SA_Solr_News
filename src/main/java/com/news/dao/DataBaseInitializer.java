package com.news.dao;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.news.model.Role;
import com.news.model.User;
import com.news.service.UserDao;

/**
 * Initialize the database with some test entries.
 */
public class DataBaseInitializer {

	private UserDao userDao;

	private PasswordEncoder passwordEncoder;

	protected DataBaseInitializer() {
		/* Default constructor for reflection instantiation */
	}

	public DataBaseInitializer(UserDao userDao, PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
	}

	public void initDataBase() {
		User userUser = new User("user", this.passwordEncoder.encode("user"));
		userUser.addRole(Role.USER);
		this.userDao.save(userUser);

		User adminUser = new User("admin", this.passwordEncoder.encode("admin"));
		adminUser.addRole(Role.USER);
		adminUser.addRole(Role.ADMIN);
		this.userDao.save(adminUser);

	}

}