package com.serveic_provider.service_provider.classes.java;

public class Profession {
	private String description;
	private int id;
	private String title;

	public Profession(String description, int id, String title) {
		this.description = description;
		this.id = id;
		this.title = title;
	}

	public void delete() {
	}

	public void getDetails() {
	}

	public void update() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}