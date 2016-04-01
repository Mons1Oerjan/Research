package com.example.research;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.BeforeClass;
import org.junit.Test;

import com.example.research.backend.LoginValidator;
import com.example.research.backend.db.Researcher;
import com.example.research.backend.db.User;
import com.example.research.backend.db.testData.DummyDBValues;


public class LoginValidatorTest {
	
	private static EntityManager em;
	
	/**
	 * set up the database and entity manager
	 */
	@BeforeClass
	public static void setup(){
		DummyDBValues.createData();
		em = Persistence.createEntityManagerFactory(ResearchUI.PERSISTENCE_UNIT).createEntityManager();
	}
	
	@Test
	//Unit Test 017: LoginValidator Constructor succeeds.
	public void constructorTest() {
		LoginValidator testLogin = new LoginValidator("Test", "Test");
		assertTrue(testLogin != null);
	}
	
	@Test
	//Unit Test 018: isValidResearcher() distinguishes valid & invalid researchers from DB.
	public void isValidResearcherTest() {
		Researcher r = (Researcher)em.createQuery("SELECT r FROM Researcher r").getResultList().get(0);
		LoginValidator testLogin = new LoginValidator(r.getUsername(), r.getPassword());
		assertTrue(testLogin.isValidResearcher());
		
		testLogin.setPassword("_NOT_PASSWORD_");
		assertFalse(testLogin.isValidResearcher());
	}
	
	@Test
	//Unit Test 019: isValidUser() distinguishes valid & invalid users from DB.
	public void isValidUserTest() {
		User u = (User)em.createQuery("SELECT u FROM User u").getResultList().get(0);
		LoginValidator testLogin = new LoginValidator(u.getUsername(), u.getPassword());
		assertTrue(testLogin.isValidUser());
		
		testLogin.setPassword("_NOT_PASSWORD_");
		assertFalse(testLogin.isValidUser());
	}

}
