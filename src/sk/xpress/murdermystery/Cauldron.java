package sk.xpress.murdermystery;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class Cauldron {

	private Location loc;
	private ArmorStand as;
	
	public Cauldron(Location loc, ArmorStand as) {
		this.loc = loc;
		this.as = as;
	}
	
	public Location getLocation() {
		return this.loc;
	}
	
	public ArmorStand getArmorStand() {
		return this.as;
	}
}
