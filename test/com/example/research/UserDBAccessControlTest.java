package com.example.research;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.example.research.backend.UserDBAccessControl;
import com.example.research.backend.db.User;
import com.example.research.backend.db.testData.DummyDBValues;
import com.vaadin.addon.jpacontainer.JPAContainer;

public class UserDBAccessControlTest {

	private static JPAContainer<User> testContainer;
	
	@BeforeClass
	public static void Init(){
		DummyDBValues.createData();
		testContainer = UserDBAccessControl.getUserContainer();
	}
	
	@Test
	//Unit Test 035: Confirm JPAContainer<User> initialization.
	public void containerInitTest() {
		assertTrue(testContainer != null);
	}
	
	@Test
	//Unit Test 036: Confirm JPAContainer<User> when calling getUserContainer().
	public void userContainerTest() {
		
		UserDBAccessControl.reset();
		UserDBAccessControl.getUserContainer();
		assertTrue(testContainer.size() >= 7);	
	}
	
	@Test
	//Unit Test 037: Confirm getUser returns user correctly
	public void getUserTest(){
		User u = UserDBAccessControl.getUser("mons02");
		assertTrue(u != null);
	}

}
