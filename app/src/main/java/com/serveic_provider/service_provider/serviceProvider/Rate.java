package com.serveic_provider.service_provider.serviceProvider;

public class Rate {
	private int date;
	private int id;
	private int value;

	public Rate(int date, int id, int value) {
		this.date = date;
		this.id = id;
		this.value = value;
	}

	public void addComment() {
	}

	public void delete() {
	}

	public void getComment() {
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}