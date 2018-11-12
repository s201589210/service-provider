package com.serveic_provider.service_provider.classes.java;

public class Profile {
	private int age;
	private int available;
	private int id;
	private String image;
	private String location;
	private String name;
	private int status;
	private int user_id;

	public Profile(int age, int available, int id, String image, String location, String name, int status,
			int user_id) {
		this.age = age;
		this.available = available;
		this.id = id;
		this.image = image;
		this.location = location;
		this.name = name;
		this.status = status;
		this.user_id = user_id;
	}

	public void addInformation() {
	}

	public void create() {
	}

	public void getAvalibletime() {
	}

	public void getInformation() {
	}

	public void getPenalties() {
	}

	public void updateInformation() {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
}