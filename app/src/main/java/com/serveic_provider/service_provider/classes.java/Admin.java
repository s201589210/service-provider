package com.serveic_provider.service_provider.classes.java;

public class Admin {
	private int id;
	private int privileged;

	public Admin(int id,int privileged ){
		this.id=id;
		this.privileged = privileged;
	}

	public void changePrivilege() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPrivileged() {
		return privileged;
	}

	public void setPrivileged(int privileged) {
		this.privileged = privileged;
	}
}