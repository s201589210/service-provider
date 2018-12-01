package com.serveic_provider.service_provider.serviceProvider;

public class Comment {
	private String content;
	private String title;

	public Comment(String content, String title) {
		this.content = content;
		this.title = title;
	}

	public void delete() {
	}

	public void update() {
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}