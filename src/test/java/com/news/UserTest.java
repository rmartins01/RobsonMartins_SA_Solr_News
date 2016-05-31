package com.news;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.news.model.Role;
import com.news.model.User;
import com.news.service.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:app-config_test.xml")
@TransactionConfiguration(defaultRollback = true, transactionManager = "transactionManager")
public class UserTest {

	@Autowired
	private UserDao userDao;

	@Test
	public void testCreateUserDetails() {

		User adminUser = userDao.findByName("admin");
		if(!exists("admin")){
			adminUser = new User("admin", "admin");
			adminUser.addRole(Role.USER);
			adminUser.addRole(Role.ADMIN);
			userDao.save(adminUser);
		}
		
		assertNotNull(adminUser);
	}
	
	private boolean exists(String name){
		User emp = userDao.findByName(name);
		if(emp == null || emp.getId() == null){
			return false;
		}
		return true;
	}

}