package com.serveic_provider.service_provider.serviceProvider;

public class User {
	private String email;
	private int id;
	private int is_baned;
	private String password;
	private String phone_number;
	private String username;
	private String type;
	private String profissin;

	public User(String email, int id, int is_baned, String password, String phone_number, String user_name, String type) {
		this.email = email;
		this.id = id;
		this.is_baned = is_baned;
		this.password = password;
		this.phone_number = phone_number;
		this.username = user_name;
		this.type = type;
	}
	public User(){

    }
	public void login(){

	}
	public void create() {
	}

	public void delete() {
	}

	public void edit() {
	}

	public void getProfile() {
	}

	public void logout() {
	}

	public void setProfissin(String profissin) {
		if (this.type.equals("provider"))
			this.profissin = profissin;
	}

	public String getProfissin() {
		return profissin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getIs_baned() {
		return is_baned;
	}

	public void setIs_baned(int is_baned) {
		this.is_baned = is_baned;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}