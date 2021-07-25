package com.frager.oreport.mailserver.model;

import javax.validation.constraints.NotNull;

public class SendStatus {

	private Status status;

	private String text;

	private SendStatus() {
		super();
	}

	public static SendStatus ok() {
		return fromStatus(Status.OK, Status.OK.name());
	}

	public static SendStatus fromStatus(@NotNull Status status, @NotNull String text) {
		SendStatus ss = new SendStatus();
		ss.status = status;
		ss.text = text;
		return ss;
	}

	public static SendStatus error(@NotNull String text) {
		return fromStatus(Status.ERROR, text);
	}

	public Status getStatus() {
		return status;
	}

	public String getText() {
		return text;
	}
}
