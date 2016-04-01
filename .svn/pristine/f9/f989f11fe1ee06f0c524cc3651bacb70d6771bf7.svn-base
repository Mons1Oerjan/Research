package com.example.research.backend.db;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * A User entity in the JPA database. Stores contact and 
 * login information for a Participant User. 
 * User has a 1-M relationship with the Data entity, 
 * and a M-1 relationship with the Project entity.  */
@Entity
public class User {

	@Id
	private String username;
	
	@NotNull
	@Size(min = 6, max = 49, message = "Password must be between 6-49 characters")
	private String password;
	
	@Size(max=20, message = "phone number too long")
	@Pattern(regexp="^\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$")
	private String phone;
	
	@Size(max=49, message = "email too long")
	@Pattern(regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
	private String email;
	
	@NotNull
	@Size(min = 2, max = 49, message = "Name must be between 2-49 characters")
	private String name;
	
	
	private boolean admin;
	
	@ManyToOne
	private Project project;
	
	@OneToMany(mappedBy="user", cascade = CascadeType.PERSIST)
	private Set<Data> data;
	
	/**
	 * Empty constructor required for JPA
	 */
	public User(){}
	
	/**
	 * Constructor to initialize all User parameters.
	 * @param username User's login name.
	 * @param password User's login password.
	 * @param phone User's phone number.
	 * @param email User's email address.
	 * @param name User's name.
	 * @param admin Administrator.  */
	public User(String username, String password, String phone, String email
			, String name, boolean admin){
		this.username = username;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.name = name;
		this.admin = admin;
		data = new HashSet<Data>();
	}

	/**
	 * Gets the username of the user
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username of the user
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * Gets the password of the user
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of the user
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the phone number of the user
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the phone number of the user
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the email of the user
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of the user
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the name of the user
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the user
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Checks if the user is an admin
	 * @return
	 */
	public boolean isAdmin() {
		return admin;
	}

	/**
	 * Sets admin status (true or false) for the user
	 * @param admin
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/**
	 * Gets the project related to the user
	 * @return
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * Sets the project related to the user
	 * @param project
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * Adds data to this user
	 * @param data
	 */
	public void addData(Data data){
		this.data.add(data);
		data.setUser(this);
	}
	
	/**
	 * Gets the whole set of data related to this user
	 * @return
	 */
	public Set<Data> getData(){
		return data;
	}
}
