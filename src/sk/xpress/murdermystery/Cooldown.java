package sk.xpress.murdermystery;

import java.util.HashMap;
import java.util.Map;

public class Cooldown {

	private static Map<String, Long> cd = new HashMap<String, Long>();
	
	public Cooldown(String key, int time) {
		if(!cd.containsKey(key)) {
			cd.put(key, System.currentTimeMillis() + (time*1000));
		}
	}
	
	public static boolean hasCooldown(String key) {
		
		if(cd.containsKey(key)) {
			if(System.currentTimeMillis() <= cd.get(key)) return true;
			else cd.remove(key);
		}
		
		return false;
	}
	
	public static int getTimeCooldown(String key) {
		if(hasCooldown(key)) {
			return (int)(cd.get(key) - System.currentTimeMillis())/1000;
		}
		return 0;
	}
	
	public static Map<String, Long> getCooldown(){
		return cd;
	}
	
	public static Long getCooldown(String key) {
		return cd.get(key);
	}
}
