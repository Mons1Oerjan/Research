package com.example.research.backend;

import com.example.research.ResearchUI;
import com.example.research.backend.db.*;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;

/**
 * Business logic for access to DB values in the 'Data' Entity.
 * The 'Data' Entity is accessed by both Researchers and Participants.  */
public class DataDBAccessControl {

	private JPAContainer<Data> dataContainer;
	
	/**
	 * Initialize data container using JPAContainerFactory.  */
	public DataDBAccessControl(){
		dataContainer = JPAContainerFactory.make(Data.class, ResearchUI.PERSISTENCE_UNIT);
	}
	
	/**
	 * Get JPAContainer of Data Entities, which can be assigned to UI elements for display.
	 * @return Data Container.  */
	public JPAContainer<Data> getDataContainer() {
		return dataContainer;
	}
	
	/**
	 * Remove all filters from the container.  */
	public void reset() {
		dataContainer.removeAllContainerFilters();
	}
	
	/**
	 * Container filtered to show only data of given user.
	 * @param username User name of the given user.
	 * @return Filtered Container.  */
	public void getUserDataContainer(String username){
		dataContainer.addNestedContainerProperty("user.username");
		Filter filter = new Compare.Equal("user.username", username);
		dataContainer.addContainerFilter(filter);
	}
	
	/**
	 * Container filtered to show only data of given projectID.
	 * @param projectID ID# of the project.
	 * @return Filtered Container.  */
	public void getProjectDataContainer(int projectID){
		dataContainer.addNestedContainerProperty("user.username");
		dataContainer.addNestedContainerProperty("user.project.p_Id");
		Filter filter = new Compare.Equal("user.project.p_Id.", projectID);
		dataContainer.addContainerFilter(filter);
	}
}
