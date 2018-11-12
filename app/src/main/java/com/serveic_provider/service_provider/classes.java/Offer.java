package com.serveic_provider.service_provider.classes.java;

public class Offer {
	private int id;
	private int service_id;
	private String status;

	public Offer(int id, int service_id, String status) {
		this.id = id;
		this.service_id = service_id;
		this.status = status;
	}

	public void accept() {
	}

	public void get() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getService_id() {
		return service_id;
	}

	public void setService_id(int service_id) {
		this.service_id = service_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}