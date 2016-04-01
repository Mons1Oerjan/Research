package com.example.research.backend;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import com.example.research.ResearchUI;

/** 
 * A validator to check given login credentials against database of users and researchers.  */
public class LoginValidator {
	
	private String username;	
	private String password;
	private EntityManager em;
	
	/**
	 * Class constructor that also creates a new EntityManager for querying.
	 * @param username Login field's user name.
	 * @param password Login field's password.
	 */
	public LoginValidator(String username, String password){
		this.username = username;
		this.password = password;
		em = Persistence.createEntityManagerFactory(ResearchUI.PERSISTENCE_UNIT).createEntityManager();
	}
	
	public void setPassword(String password){ this.password = password; }
	
	public String getUsername(){ return this.username; }
	public String getPassword(){ return this.password; }
	
	/** 
	 * Search Researcher JPA DB Table to confirm the given credentials match a Researcher.  
	 * @return True if user name and password values match a Researcher table entry.  */
	public boolean isValidResearcher(){	
		List query = em.createQuery("SELECT r "
			+ "FROM Researcher r "
		    + "WHERE r.username LIKE :username AND r.password LIKE :password")
			.setParameter("username", username).setParameter("password",password)
			.setMaxResults(1).getResultList();
		
		return !query.isEmpty();
	}
	
	/** Search User JPA DB Table to confirm given credentials match a User.
	 *  @return True if user name and password values match a User table entry.  */
	public boolean isValidUser(){
		List query = em.createQuery("SELECT u "
			+ "FROM User u "
			+ "WHERE u.username LIKE :username AND u.password LIKE :password")
			.setParameter("username", username).setParameter("password",password)
			.setMaxResults(1).getResultList();
		
		return !query.isEmpty();
	}
}