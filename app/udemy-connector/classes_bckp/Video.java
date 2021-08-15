package com.udemy.model;

public class Video {

	private String type;
	private String label;
	private String file;

	public Video() {
	}

	public Video(String type, String label, String file) {
		super();
		this.type = type;
		this.label = label;
		this.file = file;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
