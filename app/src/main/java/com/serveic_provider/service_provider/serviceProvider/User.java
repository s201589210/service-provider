package com.serveic_provider.service_provider.serviceProvider;

public class User {
	//private String email;
	private int is_baned;
	//private String password;
	private String phone_number;
	private String username;
	private String type;
	private String profession;
	private int age;
	private int available;
	private String image;
	private int serviceCounter;

	public User(int is_baned, String phone_number, String user_name, String type, String profession) {

		this.is_baned = is_baned;
		this.phone_number = phone_number;
		this.username = user_name;
		this.type = type;
		this.profession = profession;
		this.serviceCounter = serviceCounter;
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

	public void setProfession(String profession) {
		if (this.type.equals("provider"))
			this.profession = profession;
	}

	public String getProfession() {
		return profession;
	}

	/*public String getEmail() {
		return email;
	}*/

	/*public void setEmail(String email) {
		this.email = email;
	}*/

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

	/*public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}*/

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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}