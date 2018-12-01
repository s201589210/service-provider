package com.serveic_provider.service_provider.serviceProvider;

public class Service {
	private String date;
	private String description;
	private int location;
	private String provider_id;
	private int rate;
	//private int requester_id;
	private String status;
	//private int time;
	private int providersCounter;

	public Service(String date, String description, int location, String provider_id, int rate,
			String status) {
		this.date = date;
		this.description = description;

		this.location = location;
		this.provider_id = provider_id;
		this.rate = rate;

		this.status = status;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public String getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(String provider_id) {
		this.provider_id = provider_id;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

/*	public int getRequester_id() {
		return requester_id;
	}

	public void setRequester_id(int requester_id) {
		this.requester_id = requester_id;
	}*/

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/*public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}*/
}