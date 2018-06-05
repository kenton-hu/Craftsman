package com.ktn.craftsman.bean;

public class HttpResult {
	private String type;
	private String content;
	private Object results;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Object getResults() {
		return results;
	}

	public void setResults(Object results) {
		this.results = results;
	}

}
