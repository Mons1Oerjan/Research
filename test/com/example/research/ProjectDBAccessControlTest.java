package com.example.research;

import static org.junit.Assert.*;

import java.util.Date;


import org.junit.Test;

import com.example.research.backend.ProjectDBAccessControl;
import com.example.research.backend.db.Project;
import com.example.research.backend.db.testData.DummyDBValues;

/** Tests for the business logic of the Project entity and JPA access.  */
public class ProjectDBAccessControlTest {

	@Test
	//Unit Test 021: Confirm JPAContainer<Data> initialization.
	public void ContainerInitTest() {
		assertTrue(ProjectDBAccessControl.getProjectContainer() != null);
	}
	
	@Test
	//Unit Test 038: Confirm JPAContainer<Data> initialization when we pass in a username.
	public void ContainerInitTest2() {
		assertTrue(ProjectDBAccessControl.getProjectContainer("mons02") != null);
	}
	
	@Test
	//Unit Test 029: Confirm isProjectOn() tests project dates correctly
	public void isProjectOnTest() {
		String username = "mons02";
		Date current = new Date();
		assertFalse(ProjectDBAccessControl.isProjectOn(username, current));
		current = new Date(System.currentTimeMillis() + DummyDBValues.startTime + 5000);
		assertTrue(ProjectDBAccessControl.isProjectOn(username, current));
		current = new Date(System.currentTimeMillis() + DummyDBValues.endTime + 5000);
		assertFalse(ProjectDBAccessControl.isProjectOn(username, current));
	}
	
	@Test
	//Unit Test 030: Confirm isProjectOn() tests project dates correctly
	public void isProjectOnOverloadTest() {
		int projectID = 1;
		Date current = new Date();
		assertFalse(ProjectDBAccessControl.isProjectOn(projectID, current));
		current = new Date(System.currentTimeMillis() + DummyDBValues.startTime + 5000);
		assertTrue(ProjectDBAccessControl.isProjectOn(projectID, current));
		current = new Date(System.currentTimeMillis() + DummyDBValues.endTime + 5000);
		assertFalse(ProjectDBAccessControl.isProjectOn(projectID, current));
	}
	
	@Test
	//Unit Test 031: Confirm getProject() returns project correctly.
	public void getProjectTest(){
		Project p = ProjectDBAccessControl.getProject(1);
		assertTrue(p != null);
	}
	
	
}
