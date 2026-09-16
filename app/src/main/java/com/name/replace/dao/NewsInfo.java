package com.name.replace.dao;

import org.litepal.crud.DataSupport;

/**
* 资讯消息
*/
public class NewsInfo extends DataSupport {
	private String joke_id;
	private String author;
	private String comment_num;
	private String like_num;
	private String title;
	private String summary;
	private String logofile;
	private String url;
	private String publishdate;
	private boolean isLike;
	private boolean isCollected;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLogofile() {
		return logofile;
	}

	public void setLogofile(String logofile) {
		this.logofile = logofile;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPublishdate() {
		return publishdate;
	}

	public void setPublishdate(String publishdate) {
		this.publishdate = publishdate;
	}

	public String getJoke_id() {
		return joke_id;
	}

	public void setJoke_id(String joke_id) {
		this.joke_id = joke_id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getComment_num() {
		return comment_num;
	}

	public void setComment_num(String comment_num) {
		this.comment_num = comment_num;
	}

	public String getLike_num() {
		return like_num;
	}

	public void setLike_num(String like_num) {
		this.like_num = like_num;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean like) {
		isLike = like;
	}

	public boolean isCollected() {
		return isCollected;
	}

	public void setCollected(boolean collected) {
		isCollected = collected;
	}
}

