package sk.xpress.murdermystery.handler;

import lombok.Getter;
import lombok.Setter;

public enum Roles {

	INNOCENT("Innocent"),
	DETECTIVE("Detective"),
	MURDER("Murder");
	
	@Getter @Setter private String name;
	
	Roles(String name) {
		this.name = name;
	}
}
