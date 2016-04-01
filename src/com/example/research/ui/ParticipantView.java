package com.example.research.ui;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Property.ValueChangeListener;
import java.util.Date;

import com.example.research.backend.DataDBAccessControl;
import com.example.research.backend.ProjectDBAccessControl;
import com.example.research.backend.UserDBAccessControl;
import com.example.research.backend.db.*;
import com.example.research.backend.db.testData.DummyDBValues;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

/** 
 * Application Main UI screen for Participant users. This screen
 * shows the current Participant's data and allows them to upload
 * newly collected data from their device.  */
public class ParticipantView extends NavigationView {

	private static final long serialVersionUID = -8432740342275185026L;

	private TextField filter;	
	private JPAContainer<Data> dataContainer;
	private DataDBAccessControl access;
    private Grid dataGrid;
    private Button sendData, refresh, logout;
    private Label indicator;
    private MapView map;
	public static boolean trackingOn = true;
	

	@Override
    public void attach() {
        super.attach();
        configureComponents();
        buildUi();
    };

    /** Configure the filter, grid, and send button components.  */
    private void configureComponents() {
    	filter = new TextField();
        filter.setInputPrompt("Filter data...");
        
        access = new DataDBAccessControl();
        access.getUserDataContainer((String)getSession().getAttribute("user"));
        dataContainer= access.getDataContainer();
        
        dataGrid = new Grid(null, dataContainer);
        dataGrid.removeColumn("user");
        dataGrid.setColumnOrder("user.username", "d_id", "heading", "speed", "longitude", "latitude", "accuracy");
        
        
        sendData = new Button("Send Data");
        sendData.addClickListener(e -> sendData()); 
        
        refresh = new Button("Refresh");
        refresh.addClickListener(e -> refreshTable());
        logout = new Button("Logout");
        logout.addClickListener(e -> {
        	super.getUI().getPage().setLocation("/Research");
        	getSession().close();
        });
    }
    
    /** Initialize UI components and add them to the view layout.  */
    private void buildUi() {
    	
    	//create and set some components:
    	Label name = new Label("Person Name: " + getSession().getAttribute("user"));
    	Label project = new Label ("Project: " + UserDBAccessControl.getUser((String)getSession().getAttribute("user")).getProject().getName());
	    Label location = new Label("Current Location: Dal");
	    Label time = new Label("Time: " + new Date());
	    indicator = new Label("ON");
	    
	    if (indicator.getValue().equals("ON"))
	    	Notification.show("Tracker is ON");
	    else
	    	Notification.show("Tracker is OFF");
	    
	    Switch myswitch = new Switch("Collect Data");
    	myswitch.setValue(true);
    	myswitch.setId("projectSwitch");
    	trackingOn = true;
    	
    	//checking for switch value change
    	myswitch.addValueChangeListener(new ValueChangeListener(){
    		@Override
    		public void valueChange(
    				final com.vaadin.data.Property.ValueChangeEvent event) {
    			trackingOn = (boolean)event.getProperty().getValue();
    		}
    	
    	});
	    
    	//create componentgroup for previous window
    	VerticalComponentGroup previousComponent = new VerticalComponentGroup("Data");
    	previousComponent.addComponents(name, project, time, indicator, myswitch);
    	previousComponent.setHeight("60%");
	    
    	//create componentgroup for current window
    	VerticalComponentGroup currentComponent = new VerticalComponentGroup();
    	currentComponent.setWidth("100%");
    	
    	//create buttongroup (since toolbar is not working)
	    HorizontalButtonGroup buttons = new HorizontalButtonGroup();
	    buttons.setWidth("100%");
	    buttons.addComponents(sendData, refresh, logout);
    	currentComponent.addComponent(buttons); 
    	currentComponent.addComponents(filter, dataGrid);
    	filter.setWidth("100%");
    	dataGrid.setSizeFull();
    	
    	//create 3 views:
    	NavigationView previousView = new NavigationView(previousComponent);//account info
    	NavigationView currentView = new NavigationView(currentComponent);  //data grid
    	map = new MapView();
    	NavigationView nextView = new NavigationView(map);   		//map
    	previousView.setCaption("User Account Info");
    	currentView.setCaption("Data Collection");
    	nextView.setCaption("Data Location");
    	// set Id's for test purposes:
    	previousView.setId("infoView");
    	currentView.setId("mainView");
    	nextView.setId("mapView");
    	
    	//create the view manager:
    	NavigationManager manager = new NavigationManager();
    	manager.setPreviousComponent(previousView);
    	manager.setCurrentComponent(currentView);
    	manager.setNextComponent(nextView);

    	//set up navigationbuttons and functionality with navigationbars:
    	NavigationButton currentToNext = new NavigationButton("Map", nextView);
    	currentToNext.setId("mapNavButton"); //test purposes
    	currentView.getNavigationBar().setRightComponent(currentToNext);
    	NavigationButton previousToCurrent = new NavigationButton("Data Collection", currentView);
    	previousView.getNavigationBar().setRightComponent(previousToCurrent);
    	getNavigationBar().setCaption(""+getSession().getAttribute("user"));
    	nextView.getNavigationBar().getLeftComponent().setId("dataCollectionNavButton");
    	
	    setContent(manager);
	    map.updateMarkers();
    }
    
    /** If current Participant's project is active, then add new data to the DB and refresh the page.  */
    private void sendData(){
    	if (ProjectDBAccessControl.isProjectOn((String)getSession().getAttribute("user"), new Date()) && trackingOn){	 
	    	DummyDBValues.addGeoUserDataEntity((String)getSession().getAttribute("user"));
	    	dataContainer.refresh();
	    	refreshTable();
    	} else {
    		Notification.show("project is off" + UserDBAccessControl.getUser((String)getSession().getAttribute("user")).getProject().getName() );
    	}
    	
    	map.updateMarkers();
    }
         
    void refreshTable() {
        refreshTable(filter.getValue());
    }

    /** 
     * Refresh the table with provided filters.
     * @param stringFilter User-provided table filter.  */
    private void refreshTable(String stringFilter) {
        dataGrid.setContainerDataSource(dataContainer);
    }
    
    /** 
     * Access the current Participant's project start and end date to check
     * if project activity indicator  should read "ON" or "OFF".
     * @return Indicator status.  */
    public String checkProjectDate(){
    	Date cur = new Date();

    	if (ProjectDBAccessControl.isProjectOn((String)getSession().getAttribute("user"), cur ))
    		return "ON";
    	else 
    		return "OFF";
    }


}
