package com.udemy.model;

import java.util.List;

public class PageResponse<T> {
	private Integer count;
	private String next;
	private String previous;
	private List<T> results;

	public PageResponse() {

	}

	public PageResponse(Integer count, String next, String previous, List<T> results) {
		super();
		this.count = count;
		this.next = next;
		this.previous = previous;
		this.results = results;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}
}
