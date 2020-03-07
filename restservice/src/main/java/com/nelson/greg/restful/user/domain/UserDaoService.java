package com.nelson.greg.restful.user.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {

	private static List<User> users = new ArrayList<>();

	private static int usersCount = 4;

	static {
		users.add(new User(1, "Greg", new Date()));
		users.add(new User(2, "Traci", new Date()));
		users.add(new User(3, "Nick", new Date()));
		users.add(new User(4, "Tyler", new Date()));
	}

	public List<User> findAll() {
		return users;
	}

	public User save(User user) {

		if (user.getId() == null) {
			user.setId(++usersCount);
		}

		users.add(user);

		return user;
	}

	public User findOne(int id) {
		for (User user : users) {
			if (user.getId() == id) {
				return user;
			}
		}
		return null;
	}

	public User deleteById(int id) {
		Iterator<User> iterator = users.iterator();
		while (iterator.hasNext()) {
			User user = iterator.next();
			if (user.getId() == id) {
				iterator.remove();
				return user;
			}
		}
		return null;
	}
	
}
