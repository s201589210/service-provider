package com.serveic_provider.service_provider.serviceProvider;

public class Job {
	private String description;
	private int price;
	private String title;

	public Job(String description, int price, String title) {
		this.description = description;
		this.price = price;
		this.title = title;
	}

	public Job() {
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}