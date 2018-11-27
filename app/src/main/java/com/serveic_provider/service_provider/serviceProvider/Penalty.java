package com.serveic_provider.service_provider.serviceProvider;

public class Penalty {
	private int id;
	private int period;
	private int type;
	private int weigth;

	public Penalty(int id, int period, int type, int weigth) {
		this.id = id;
		this.period = period;
		this.type = type;
		this.weigth = weigth;
	}

	public void delete() {
	}

	public void getDetails() {
	}

	public void update() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getWeigth() {
		return weigth;
	}

	public void setWeigth(int weigth) {
		this.weigth = weigth;
	}
}