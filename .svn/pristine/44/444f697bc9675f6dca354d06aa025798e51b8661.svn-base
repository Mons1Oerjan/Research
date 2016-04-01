package com.example.research.ui;

import java.util.Date;

import com.example.research.backend.ProjectDBAccessControl;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.UI;
import com.example.research.backend.db.*;
import com.example.research.ui.ProjectEditor;

/**
 * The dashboard of the Logged-in Researcher. The dashboard shows active projects in a table
 * and allows the Researcher to select those projects, or create a new project.  */
public class ResearcherDashboardView extends NavigationView {
	
	private static final long serialVersionUID = -2141629117848213957L;
	private Button newProjectButton, logoutButton;  
	private JPAContainer<Project> projectContainer;
    private Table dataTable;
	
	@Override
    public void attach() {
        super.attach();
        	configureComponents();
            buildUi();
    };

    /** Configure the add button and Table of selectable projects.  */
    private void configureComponents() {
    	
    	//Button to open New Project sub-window.
    	newProjectButton = new Button("Create New Project");
    	newProjectButton.addClickListener(e -> accessProjectEditor());
    	
    	//Content of project list.
    	projectContainer = ProjectDBAccessControl.getProjectContainer((String)getSession().getAttribute("user"));
    	
    	//Set table to contents of project list.
    	dataTable = new Table(null, projectContainer);
    	dataTable.setVisibleColumns("name", "start", "end");   	
    	dataTable.addItemClickListener(e-> showNewProjectView(e)); 
    	logoutButton = new Button("Logout");
    	
        logoutButton.addClickListener(e -> {
        	ProjectDBAccessControl.reset();
        	super.getUI().getPage().setLocation("/Research");
        	getSession().close();
        });
        
    }
    
    /** Display a new project creation window.  */
    private void accessProjectEditor() {
    	final BeanItem<Project> newProjectItem = new BeanItem<Project>(new Project());
        ProjectEditor popup = new ProjectEditor(newProjectItem);
        UI.getCurrent().addWindow(popup);
    }
    
    /** Initialize UI components and add them to the view layout.  */
    private void buildUi() {
		VerticalLayout superLayout = new VerticalLayout();

	    Label currentTime = new Label("Current Time: " + new Date());
	    
	    getNavigationBar().setLeftComponent(logoutButton);
	    getNavigationBar().setCaption(getSession().getAttribute("user")+"");
		HorizontalLayout actions = new HorizontalLayout(newProjectButton);
	    actions.setWidth("100%");
	
	    VerticalLayout main = new VerticalLayout(actions, dataTable);
	    main.setSizeFull();
	    dataTable.setSizeFull();
	    main.setExpandRatio(dataTable, 1);
	 
	    superLayout.addComponents(currentTime);
	    superLayout.addComponent(main);

	    setContent(superLayout);
    }
    
    /**
     * When the project in the table is clicked, open the corresponding view.
     * @param e Item Event trigger.  */
    private void showNewProjectView(ItemClickEvent e){
    	getSession().setAttribute("project", e.getItemId());
    	getUI().setContent(new ResearcherView());
    }
}