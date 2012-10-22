package com.dev.saurabh.blog.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BlogEntry implements Serializable{
	
	private static final long serialVersionUID = -4564354231231243L;
	
	private String blogId;
	
	private String title;
	
	private String body;
	
	private String userId;
	
	private long updateTime;
	
	private long creationTime;

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	
	
}
