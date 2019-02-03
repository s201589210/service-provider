package com.serveic_provider.service_provider.serviceProvider;

public class Service {
	private String date;
	private String description;
	private String city;
	private String neighbor;
	private int building;
	private String provider_id;
	private int rate;
	//private int requester_id;
	private String status;
	private String job;
	private String startTime;
	private String endTime;
	private int id = NO_IMAGE_PROVIDED;
	private static final int NO_IMAGE_PROVIDED =-1;

	private int providersCounter;

	public Service(){}
	public Service(String date, String description, String city, String neighbor,
			int building, String startTime, String endTime, String provider_id, int rate, String status) {
		this.date = date;
		this.description = description;
		this.city = city;
		this.neighbor = neighbor;
		this.building = building;
		this.startTime = startTime;
		this.endTime = endTime;
		this.provider_id = provider_id;
		this.rate = rate;
		this.status = status;
	}

	public void create() {
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNeighbor() {
		return neighbor;
	}

	public void setNeighbor(String neighbor) {
		this.neighbor = neighbor;
	}

	public int getBuilding() {
		return building;
	}

	public void setBuilding(int building) {
		this.building = building;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static int getNoImageProvided() {
		return NO_IMAGE_PROVIDED;
	}

	public int getProvidersCounter() {
		return providersCounter;
	}

	public void setProvidersCounter(int providersCounter) {
		this.providersCounter = providersCounter;
	}

	public boolean hasImage() {
		return id != NO_IMAGE_PROVIDED;
	}
}