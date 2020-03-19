package sk.xpress.murdermystery.handler;

public enum Roles {

	INNOCENT("Innocent"),
	DETECTIVE("Detective"),
	MURDER("Murder"),
	SPECTATOR("Spectator"),
	NONE("");
	
	private String name;
	
	Roles(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
