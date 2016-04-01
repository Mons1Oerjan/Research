package com.example.research.ui;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.example.research.ResearchUI;
import com.example.research.backend.ProjectDBAccessControl;
import com.example.research.backend.db.Project;
import com.example.research.backend.db.testData.DummyDBValues;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/** A window containing a 'Project Creation' form, for an active
 *  Researcher to add a new project to the database. */
public class ProjectEditor extends Popover implements Button.ClickListener, FieldGroupFieldFactory{

	private static final long serialVersionUID = -3523643586121250385L;
	private final Item projectItem;
	private Button saveButton, cancelButton;
    private FieldGroup binder;
    private FormLayout editorForm, checkboxForm;
    private TextField nameField;
    private DateField startField, endField;
    private String name;
    private EntityManager em;
    private CheckBox speedBox, longitudeBox, latitudeBox, accuracyBox, directionBox;
    private TextArea projectInfo;
    
    /**
     * Constructor that builds the popup window and connects it to the database.
     * @param projectItem new Project Entity to be initialized. The form values bind to this.  */
    @SuppressWarnings("deprecation")
	public ProjectEditor(Item projectItem){
    	this.projectItem = projectItem;
    	em = Persistence.createEntityManagerFactory(ResearchUI.PERSISTENCE_UNIT).createEntityManager();
    	
    	//create a new formLayout:
        editorForm = new FormLayout();
        editorForm.setImmediate(true);
        checkboxForm = new FormLayout();
        
        //initialize textField and DateFields:
        nameField = new TextField("Project name:");
        nameField.setNullRepresentation("");
        startField = new DateField("Start date:"); 
        endField = new DateField("End date:");
    	
        //initialize buttons:
        saveButton = new Button("Start Project", this);
        cancelButton = new Button("Cancel", this);
        
        //initialize test parameter check boxes:
        speedBox = new CheckBox("Speed");
        longitudeBox = new CheckBox("Longitude");
        latitudeBox = new CheckBox("Latitude");
        accuracyBox = new CheckBox("Accuracy");
        directionBox = new CheckBox("Direction");
        
        //initialize info text area:
        projectInfo = new TextArea("Project Info:");
        projectInfo.setNullRepresentation("");
       
        //add bean validations:
        nameField.addValidator(new BeanValidator(Project.class, "name"));
        startField.addValidator(new BeanValidator(Project.class, "start"));
        endField.addValidator(new BeanValidator(Project.class, "end"));
        
        //add components to formLayout:
        editorForm.addComponents(nameField, startField, endField, projectInfo);
        checkboxForm.addComponents(speedBox, longitudeBox, latitudeBox, accuracyBox, directionBox);
        checkboxForm.setCaption("Research topic:");
        editorForm.addComponent(checkboxForm);
        editorForm.addComponents(saveButton, cancelButton);
               
        //create binder and bind the fields:
        binder = new FieldGroup(projectItem);
        binder.setFieldFactory(this);
        binder.setBuffered(true);
        binder.bind(nameField, "name");
        binder.bind(startField, "start");
        binder.bind(endField, "end");
        binder.bind(projectInfo, "projectInfo");
        
        //bind checkboxes:
        binder.bind(speedBox, "speed");
        binder.bind(longitudeBox, "longitude");
        binder.bind(latitudeBox, "latitude");
        binder.bind(accuracyBox, "accuracy");
        binder.bind(directionBox, "heading");
         
        setWidth(40, UNITS_EM);
        setHeight(50, UNITS_EM);
        setContent(editorForm);
        setCaption("Create New Project");
  
    }
    
	/**
     * Commit or discard the current new Project, depending on if
     * the 'Save' or 'Cancel' button was clicked.
     * @param clickEvent Clicked Button.  */
    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == saveButton) {
			try {
				endField.setRangeStart(startField.getValue());
				name = nameField.getValue();
				if (isValidName()){
					binder.commit();
					addProject();
					Notification.show("Successsfully added");
					fireEvent(new EditorSavedEvent(this, projectItem));
				} else {
					Notification.show("Project name already exists");
				}
			} catch (CommitException e) {
				Notification.show("Invalid input");
			}
        } else if (event.getButton() == cancelButton) {
            binder.discard();
        }
        close();
    }
    
    /** Event handler to save the newly-created Project.  */
    public static class EditorSavedEvent extends Component.Event {

		private static final long serialVersionUID = 900399188108904333L;
		private Item savedItem;

        public EditorSavedEvent(Component source, Item savedItem) {
            super(source);
            this.savedItem = savedItem;
        }

        public Item getSavedItem() {
            return savedItem;
        }
    }
    
    /** Event Listener for saved Projects.  */
    public interface EditorSavedListener extends Serializable {
        public void editorSaved(EditorSavedEvent event);
    }
    
	@SuppressWarnings("rawtypes")
	@Override
	public <T extends Field> T createField(Class<?> dataType, Class<T> fieldType) {
		return null;
	}
	
	/** Check for duplicate names in DB.
	 * @return True if no duplicate name exists.  */
	public boolean isValidName(){
		List query = em.createQuery("SELECT p "
	            + "FROM Project p "
	            + "WHERE p.name LIKE :name")
	            .setParameter("name", name)
	            .getResultList();
	        
	    return query.isEmpty();
	}
	
	/** Add project to database. Make the active Researcher the owner of the Project.  */
	public void addProject(){
		JPAContainer<Project> projectContainer = ProjectDBAccessControl.getProjectContainer();
		DummyDBValues.addProjectEntity(projectContainer.getItem(projectContainer.firstItemId())
				.getEntity().getResearcher(), name, startField.getValue(), endField.getValue(),
				speedBox.getValue(), accuracyBox.getValue(), longitudeBox.getValue(),
				latitudeBox.getValue(), directionBox.getValue(), projectInfo.getValue());
    	projectContainer.refresh();
	}	
}
