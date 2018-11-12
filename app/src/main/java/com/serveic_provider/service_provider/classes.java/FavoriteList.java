package com.serveic_provider.service_provider.classes.java;

public class FavoriteList {
private int id;
private int requester_id;

public FavoriteList(int id,int requester_id){
	this.id=id;
	this.requester_id=requester_id;
}

public void addProvider() {}
public int getId(){return id;}
public void setId(int id){this.id=id;}
public int getRequester_id(){return requester_id;}
public void setRequester_id(int requester_id){this.requester_id=requester_id;}
}