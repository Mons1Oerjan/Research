package com.example.research;

import static org.junit.Assert.*;

import java.util.Set;
import org.junit.BeforeClass;
import com.example.research.ResearchUI;
import com.example.research.backend.db.*;
import com.example.research.backend.db.testData.DummyDBValues;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

import org.junit.*;

/** Tests for initialization of Dummy data.  */
public class DBDummyTest {

	private static JPAContainer<Researcher> researchers;
	private static JPAContainer<Project> projects;
	private static JPAContainer<User> users;
	private static JPAContainer<Data> data;
	private static Researcher r1;
	private static Project p1;
	private static User u1;
	private static Data d1;

	/**
	 * addProjectData runs the createData method in DummyDBValues to generate some dummy data.
	 * Then it initializes the JPAContainers for the researchers, projects, users and data.
	 * This method is only being called once before the tests. 
	 * @BeforeClass
	 */
	@BeforeClass
    public static void addProjectDataTest() {
		DummyDBValues.createData();
		researchers = JPAContainerFactory.make(Researcher.class, ResearchUI.PERSISTENCE_UNIT);
		projects = JPAContainerFactory.make(Project.class, ResearchUI.PERSISTENCE_UNIT);
		users = JPAContainerFactory.make(User.class, ResearchUI.PERSISTENCE_UNIT);
		data = JPAContainerFactory.make(Data.class, ResearchUI.PERSISTENCE_UNIT);
		
		p1 = projects.getItem(projects.firstItemId()).getEntity();
		r1 = researchers.getItem(researchers.firstItemId()).getEntity();
		u1 = users.getItem(users.firstItemId()).getEntity();
		d1 = data.getItem(data.firstItemId()).getEntity();
    }
	
	@Test
	//Unit Test 010: Add data to Project table.
	public void dummyProjectDataTest(){	
		//Confirm dummy projects created.
		assertTrue(projects.size() > 0);		
		//Confirm first project instantiated.
		assertTrue(!p1.getName().isEmpty());	
	}
	
	@Test
	//Unit Test 011: Add data to Researcher table.
	public void dummyResearcherDataTest(){	
		//Confirm dummy researchers created.
		assertTrue(researchers.size() > 0);		
		//Confirm first researcher instantiated.
		assertTrue(!r1.getName().isEmpty());	
	}
	
	@Test
	//Unit Test 012: Add data to User table.
	public void dummyUserDataTest(){	
		//Confirm dummy users created.
		assertTrue(users.size() > 0);		
		//Confirm first user instantiated.
		assertTrue(!u1.getName().isEmpty());	
	}
	
	@Test
	//Unit Test 013: Add data to Data table.
	public void testDummyDataTest(){		
		//Confirm dummy data created.
		assertTrue(data.size() > 0);		
		//Confirm first data instantiated.
		assertTrue(d1.getSpeed() == 0.0);
		assertTrue(d1.getUser() != null);
	}
	
	@Test
	//Unit Test 014: Confirm table relationships exist.
	public void relationsTest(){	
		
		//Researcher-Project relation.
		Set<Project> RtoP = r1.getProjects();
		assertTrue(RtoP.size() != 0);
		
		//Project-User relation.
		Set<User> PtoU = p1.getUsers();
		assertTrue(PtoU.size() != 0);
		
		//User-Data relation.
		Set<Data> PtoD = u1.getData();
		assertTrue(PtoD.size() != 0);
	}
	
	@Test
	//Unit Test 016: Method addDummyEntity() creates a single new entity.
	public void addDummyEntityTest(){
		User u = users.getItem(users.firstItemId()).getEntity();
		
		int oldSize = u.getData().size();
		
		DummyDBValues.addDummyEntity(u);
		
		int newSize = u.getData().size();
		
		//make sure we are only adding one data value
		assertEquals(oldSize+1, newSize);
		
	}
	
	@Test
	//Unit Test 040: Method addUserEntity() adds user to project and project to user
	public void addUserEntityTest(){
		int oldSize = p1.getUsers().size();
		
		DummyDBValues.addUserEntity("newname", "password", "5555555555", "email@email.com", "New Name", p1);
		
		int newSize = p1.getUsers().size();
		assertEquals(oldSize+1, newSize);
		
	}

}

