package com.example.research.backend;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.example.research.ResearchUI;
import com.example.research.backend.db.User;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

public class UserDBAccessControl {

	private static JPAContainer<User> userContainer = JPAContainerFactory.make(
			User.class, ResearchUI.PERSISTENCE_UNIT);
	
	/**
	 * Reset the JPAContainer by creating a new one
	 * and setting it to itself  */
	public static void reset(){
		userContainer = JPAContainerFactory.make(
				User.class, ResearchUI.PERSISTENCE_UNIT);
	}

	/**
	 * Get JPAContainer of User Entities, which can be assigned to UI elements for display.
	 * @return Project Container.  */
	public static JPAContainer<User> getUserContainer() {
		return userContainer;
	}
	
	/**
	 * Get User entity with matching username.
	 * @param username ID of user to find.
	 * @return Corresponding user.  */
	public static User getUser(String u_name){
		EntityManager em = Persistence.createEntityManagerFactory(ResearchUI.PERSISTENCE_UNIT).createEntityManager();
		List query = em.createQuery("SELECT u "
				+ "FROM User u "
				+ "WHERE u.username LIKE :u_name")
				.setParameter("u_name", u_name)
				.setMaxResults(1).getResultList();
		if (!query.isEmpty())
			return (User) query.get(0);
		else
			return null;
	}
	
}
