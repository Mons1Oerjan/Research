package com.example.research.ui;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.example.research.ResearchUI;
import com.example.research.backend.db.Project;
import com.example.research.backend.db.Researcher;
import com.example.research.backend.db.User;
import com.example.research.backend.db.testData.DummyDBValues;
import com.vaadin.addon.touchkit.ui.EmailField;
import com.vaadin.addon.touchkit.ui.NumberField;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;

public class RegisterUserView extends Popover implements Button.ClickListener, FieldGroupFieldFactory{

	private static final long serialVersionUID = -6753942433815980604L;
	private final Item userItem;
	private Button saveButton, cancelButton;
    private FieldGroup binder;
    private FormLayout editorForm;
    private TextField usernameField, realnameField;
    private PasswordField passwordField1, passwordField2;
    private EmailField emailField;
    private NumberField phoneField;
    private EntityManager em;
    private String name;
    private ComboBox projects;
    private Switch userType;
 
    
	public RegisterUserView(Item userItem){
		this.userItem = userItem;
    	em = Persistence.createEntityManagerFactory(ResearchUI.
    			PERSISTENCE_UNIT).createEntityManager();
    	
    	//create a new formLayout:
        editorForm = new FormLayout();
        editorForm.setImmediate(true);
        
        //initialize Fields:
        usernameField = new TextField("Username:");
        usernameField.setNullRepresentation("");
        
        passwordField1 = new PasswordField("Password:");
        passwordField1.setNullRepresentation("");
        passwordField1.setId("passField");
        
        passwordField2 = new PasswordField("Re-enter Password:");
        passwordField2.setNullRepresentation("");
        
        phoneField = new NumberField("Phone number:");
        phoneField.setNullRepresentation("");
        phoneField.setId("phoneID");
        
        emailField = new EmailField("Email:");
        emailField.setId("emailID");
        emailField.setNullRepresentation("");
        
        realnameField = new TextField("Given name:");
        realnameField.setNullRepresentation("");
        
        projects = new ComboBox("Select Project");
        projects.setId("combobox");
        projects.setFilteringMode(FilteringMode.CONTAINS);
        projects.addItems(Researcher.getProjects());
        
        userType = new Switch("Are you a researcher?");
        userType.addValueChangeListener(e -> hideProjectSelection());
        projects.setId("user_switch");
        
        for(Project p: Researcher.getProjects()){
        	projects.setItemCaption(p, p.getName());
        }
            
        //initialize buttons:
        saveButton = new Button("Create User", this);
        cancelButton = new Button("Cancel", this);
        
        //add bean validations:
        usernameField.addValidator(new BeanValidator(User.class, "username"));
        passwordField1.addValidator(new BeanValidator(User.class, "password"));
        passwordField2.addValidator(new BeanValidator(User.class, "password"));
        phoneField.addValidator(new BeanValidator(User.class, "phone"));
        emailField.addValidator(new BeanValidator(User.class, "email"));
        realnameField.addValidator(new BeanValidator(User.class, "name"));
        
        //add components to formLayout:
        editorForm.addComponents(userType, usernameField, passwordField1, passwordField2, 
        		phoneField, emailField, realnameField, projects, saveButton, cancelButton);
        
        //create binder and bind the fields:
        binder = new FieldGroup(userItem);
        binder.setFieldFactory(this);
        binder.setBuffered(true);
        binder.bind(usernameField, "username");
        binder.bind(passwordField1, "password");
        binder.bind(phoneField, "phone");
        binder.bind(emailField, "email");
        binder.bind(realnameField, "name");
        binder.bind(projects, "project");
        
        
        
        setWidth(40, UNITS_EM);
        setHeight(50, UNITS_EM);
        setContent(editorForm);
        setCaption("Create New User");
        
	}
	
	/**
     * Commit or discard the current new User, depending on if
     * the 'Save' or 'Cancel' button was clicked and if passwords match.
     * @param clickEvent Clicked Button.  */
    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == saveButton) {
			try {
				name = usernameField.getValue();
				if (isValidName()){
					binder.commit();
					if (passwordField1.getValue().equals(passwordField2.getValue())){
						if(userType.getValue()){
							DummyDBValues.addResearcherEntity(usernameField.getValue(), passwordField1.getValue(),
									phoneField.getValue(), emailField.getValue(), name);
						}
						else {
							DummyDBValues.addUserEntity(usernameField.getValue(), passwordField1.getValue(),
									phoneField.getValue(), emailField.getValue(), name, (Project) projects.getValue());
						}
						Notification.show("Successsfully created");
						fireEvent(new EditorSavedEvent(this, userItem));
					}
					else{
						Notification.show("Passwords must be equal.");
					}
				} else {
					Notification.show("Username already exists. Please try again.");
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

		private static final long serialVersionUID = -7174753157206756819L;
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
		List query = em.createQuery("SELECT u "
	            + "FROM User u "
	            + "WHERE u.username LIKE :username")
	            .setParameter("username", name)
	            .getResultList();
	        
	    return query.isEmpty();
	}

	/** Hide the Project selection combo box. */
	
	public void hideProjectSelection(){
		if (userType.getValue())
			projects.setVisible(false);
		else
			projects.setVisible(true);
	}
}
