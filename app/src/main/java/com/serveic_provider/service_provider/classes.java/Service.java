package com.serveic_provider.service_provider.classes.java;

public class Service {
	private int date;
	private String description;
	private int id;
	private int location;
	private int provider_id;
	private int rate;
	private int requester_id;
	private String status;
	private int time;

	public Service(int date, String description, int id, int location, int provider_id, int rate, int requester_id,
			String status, int time) {
		this.date = date;
		this.description = description;
		this.id = id;
		this.location = location;
		this.provider_id = provider_id;
		this.rate = rate;
		this.requester_id = requester_id;
		this.status = status;
		this.time = time;
	}

	public void addLovcation() {
	}

	public void addRate() {
	}

	public void changeStatus() {
	}

	public void create() {
	}

	public void getInformation() {
	}

	public void sendOffer() {
	}

	public void update() {
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
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

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(int provider_id) {
		this.provider_id = provider_id;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getRequester_id() {
		return requester_id;
	}

	public void setRequester_id(int requester_id) {
		this.requester_id = requester_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}