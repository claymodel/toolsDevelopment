package com.pv.trjira;

public class ConfigException extends Exception {

	private static final long serialVersionUID = 1468867608485515350L;

	public ConfigException() {
		// TODO Auto-generated constructor stub
	}

	public ConfigException(String message) {
		super("Aw, snap! " + message);
		// TODO Auto-generated constructor stub
	}

	public ConfigException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ConfigException(String message, Throwable cause) {
		super("Aw, double snap! " + message, cause);
		// TODO Auto-generated constructor stub
	}

}
