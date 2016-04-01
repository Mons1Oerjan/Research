package com.example.research.backend.db;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Researcher entity in the JPA database. Stores contact and 
 * login information for a Researcher User. 
 * Researcher has a 1-M relationship with the Project entity, as
 * Researchers can create multiple projects.  */
@Entity
public class Researcher {
	
	@Id
	private String username;
	
	@NotNull
	@Size(min = 6, max = 49, message = "Password must be between 6-49 characters")
	private String password;
	
	@Size(max = 20, message = "Phone number too long")
	@Pattern(regexp="^\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$")
	private String phone;
	
	@Size(max = 49, message = "email too long")
	@Pattern(regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
	private String email;
	
	@NotNull
	@Size(min = 2, max = 49, message = "Name must be between 2-49 characters")
	private String name;
	
	@NotNull
	private boolean admin;
	
	@OneToMany(mappedBy="researcher")
	private static Set<Project> projects;
	
	/**
	 * empty constructor required by JPA
	 */
	public Researcher(){}
	
	/**
	 * Constructor to initialize all Researcher parameters.
	 * @param username  Researcher's login name.
	 * @param password  Researcher's login password.
	 * @param phone Researcher's phone number.
	 * @param email Researcher's Email address.
	 * @param name Researcher's Name.
	 * @param admin Administrator.  */
	public Researcher(String username, String password, String phone,
			String email, String name, boolean admin){
		this.username = username;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.name = name;
		this.admin = admin;
		projects = new HashSet<Project>();
	}

	/**
	 * Gets the username of the researcher.
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username of the researcher.
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Gets the password of the researcher.
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of the researcher.
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the phone number of the researcher.
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the phone number of the researcher.
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the email of the researcher.
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of the researcher.
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the name of the researcher.
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the researcher.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Checks if the researcher is an admin.
	 * @return
	 */
	public boolean isAdmin() {
		return admin;
	}

	/**
	 * Sets whether or not the researcher is an admin.
	 * @param admin
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/**
	 * Adds a project to the researcher.
	 * @param project
	 */
	public void addProject(Project project) {
		this.projects.add(project);
		project.setResearcher(this);
	}
	
	/**
	 * Gets the set of all projects associated with
	 * the researcher.
	 * @return
	 */
	public static Set<Project> getProjects(){
		return projects;
	}
	
}
