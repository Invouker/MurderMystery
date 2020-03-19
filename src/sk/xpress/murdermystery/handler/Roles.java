package sk.xpress.murdermystery.handler;

public enum Roles {

	INNOCENT("Innocent"),
	DETECTIVE("Detective"),
	MURDER("Murder");
	
	private String name;
	
	Roles(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
