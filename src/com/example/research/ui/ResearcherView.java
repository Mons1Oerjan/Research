package com.example.research.ui;

import java.util.Date;

import com.example.research.backend.DataDBAccessControl;
import com.example.research.backend.ProjectDBAccessControl;
import com.example.research.backend.db.Data;
import com.example.research.backend.db.Project;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/** 
 * Application screen for Researcher users to view the data of a selected project.  */
public class ResearcherView extends NavigationView {

	private static final long serialVersionUID = -2141629117848213957L;	
	private TextField filter = new TextField();
    private Button refresh = new Button("Refresh"), back = new Button("Back");
	private JPAContainer<Data> dataContainer;
	private DataDBAccessControl access;	
    private Grid dataGrid;
    private Label indicator;
    NavigationView view;
	
	@Override
    public void attach() {
        super.attach();
        	configureComponents();
            buildUi();
    };

    /** Configure the displayed grid, refresh button, and filter field components.  */
    private void configureComponents() {   	
    	access = new DataDBAccessControl();
    	access.getProjectDataContainer((Integer)getSession().getAttribute("project"));
    	
    	dataContainer = access.getDataContainer();
    	
    	createDataGrid();
    	
    	back.addClickListener(e -> back());
    	
        filter.setInputPrompt("Filter data...");
        
        refresh.addClickListener(e -> refreshTable());
    }
    
    /** Initialize UI components and add them to the view layout.  */
    private void buildUi() {
		view = new NavigationView();
		VerticalComponentGroup superLayout = new VerticalComponentGroup();
		superLayout.setSizeFull();
    	Project p =  ProjectDBAccessControl.getProject((int)getSession().getAttribute("project"));
    	view.setCaption(p.getName());
	    Label projectinfo = new Label(p.getProjectInfo());
    	Label startTime = new Label("Start time: " + p.getStart());
	    Label endTime = new Label("End time: " + p.getEnd());
	    indicator = new Label(checkProjectDate());
	    
	    filter.setWidth("100%");
	    view.getNavigationBar().setLeftComponent(back);
	    superLayout.addComponents(projectinfo, startTime, endTime, indicator, refresh, filter, dataGrid);
	    dataGrid.setSizeFull();
	    
	    view.setContent(superLayout);
	    setContent(view);
	    
    }
    
    void refreshTable() {
        refreshTable(filter.getValue());
    }

    /** 
     * Refresh the table with provided filters.
     * @param stringFilter User-provided table filter.  */
    private void refreshTable(String stringFilter) {
    	indicator.setValue(checkProjectDate());
    	dataContainer.refresh();
        dataGrid.setContainerDataSource(dataContainer);
    }
    
    /** 
     * Access the current Participant's project start and end date to check
     * if project activity indicator  should read "ON" or "OFF".
     * @return Indicator status.  */
    public String checkProjectDate(){
    	Date cur = new Date();
    	if (ProjectDBAccessControl.isProjectOn((int)getSession().getAttribute("project"), cur )){
    		return "ON";
    	} else {
    		return "OFF";
    	}
    }
    
    /**
     * Create the data grid and decide which data to show depending on
     * the test parameters selected.  */
    private void createDataGrid(){
    	Project p =  ProjectDBAccessControl.getProject((int)getSession().getAttribute("project"));
    	dataGrid = new Grid(null, dataContainer);
    	dataGrid.removeAllColumns();
    	dataGrid.addColumn("user.username");
    	if (p.isSpeed())
    		dataGrid.addColumn("speed");
    	if (p.isLongitude())
    		dataGrid.addColumn("longitude");
    	if (p.isLatitude())
    		dataGrid.addColumn("latitude");
    	if (p.isAccuracy())
    		dataGrid.addColumn("accuracy");
    	if (p.isHeading())
    		dataGrid.addColumn("heading");

    }
    
    /**
     * Navigates back to ResearcherDashboardView
     */
    public void back(){
    	view.detach();
    	setContent(new ResearcherDashboardView());
    	
    	
    }
    
}
