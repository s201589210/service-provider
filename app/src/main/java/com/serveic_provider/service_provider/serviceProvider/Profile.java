package com.serveic_provider.service_provider.serviceProvider;

public class Profile {
	private String location;
	private String name;
	private int status;
	private int user_id;

	public Profile(String location, String name, int status,
			int user_id) {
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