package com.serveic_provider.service_provider.classes.java;

public class RateList {
	private int average_rate;
	private int id;
	private int provider_id;

	public RateList(int average_rate, int id, int provider_id) {
		this.average_rate = average_rate;
		this.id = id;
		this.provider_id = provider_id;
	}

	public void addRate() {
	}

	public void calculateRate() {
	}

	public int getAverage_rate() {
		return average_rate;
	}

	public void setAverage_rate(int average_rate) {
		this.average_rate = average_rate;
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
}