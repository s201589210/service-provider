package com.serveic_provider.service_provider.classes.java;

public class Report {
	private int admin_id;
	private int id;
	private int provider_id;
	private int requester_id;
	private int service_id;
	private int status;

	public Report(int admin_id, int id, int provider_id, int requester_id, int service_id, int status) {
		this.admin_id = admin_id;
		this.id = id;
		this.provider_id = provider_id;
		this.requester_id = requester_id;
		this.service_id = service_id;
		this.status = status;
	}

	public void createReport() {
	}

	public void getDetails() {
	}

	public void update() {
	}

	public int getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(int admin_id) {
		this.admin_id = admin_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(int provider_id) {
		this.provider_id = provider_id;
	}

	public int getRequester_id() {
		return requester_id;
	}

	public void setRequester_id(int requester_id) {
		this.requester_id = requester_id;
	}

	public int getService_id() {
		return service_id;
	}

	public void setService_id(int service_id) {
		this.service_id = service_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}