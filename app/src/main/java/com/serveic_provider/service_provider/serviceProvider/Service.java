package com.serveic_provider.service_provider.serviceProvider;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;

//note implementing serializable helps in moving object between content
public class Service implements Serializable {
	private String requester_id;
	private String provider_id;
	private String profession;
	private String date;
	private String description;
	private String status;
	private String city;
	private String location;
	private int rate;
	private String job;
	private String startTime;
	private String endTime;
	private int providersCounter;
	private String service_id;
	private Image image;
	private int id ;
	private ArrayList<String> potentialProvidersIds;



	public Service(){}
	public Service(String date, String description, String city, String location, String startTime, String endTime, String provider_id, int rate, String status) {
		this.date = date;
		this.description = description;
		this.city = city;
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
		this.provider_id = provider_id;
		this.rate = rate;
		this.status = status;
	}
	public Service(String provider_id){this.provider_id = provider_id;}
	public void create() { }
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
	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getProvider_id() { return provider_id; }
	public void setProvider_id(String provider_id) { this.provider_id = provider_id; }
	public int getRate() { return rate; }
	public void setRate(int rate) { this.rate = rate; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public String getJob() { return job; }
	public void setJob(String job) { this.job = job; }
	public String getStartTime() { return startTime; }
	public void setStartTime(String startTime) { this.startTime = startTime; }
	public String getEndTime() { return endTime; }
	public void setEndTime(String endTime) { this.endTime = endTime; }
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public int getProvidersCounter() { return providersCounter; }
	public void setProvidersCounter(int providersCounter) { this.providersCounter = providersCounter; }
	public String getProfession() { return profession; }
	public void setProfession(String profession) { this.profession = profession; }
	public String getRequester_id() { return requester_id;}
	public void setRequester_id(String requester_id) {this.requester_id = requester_id;}
	public Image getImage() { return image; }
	public void setImage(Image image) { this.image = image; }
	public ArrayList<String> getPotentialProvidersIds() {
		return potentialProvidersIds;
	}
	public void setPotentialProvidersIds(ArrayList<String> potentialProvidersIds) { this.potentialProvidersIds = potentialProvidersIds; }
	public String getService_id() {
		return service_id;
	}
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}
}