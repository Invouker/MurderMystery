package sk.xpress.murdermystery.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Chat {
	private final static String prefix = "§eSERVER§7> §f";
	private final static String cPrefix = "[TestPlugin] §f";
	
	public static void print(String msg) {
		Bukkit.getConsoleSender().sendMessage(cPrefix + msg);
	}
	
	public static void sendAll(String msg) {
		for(Player p : Bukkit.getOnlinePlayers()) p.sendMessage(prefix + msg);
	}
	
	public static void send(Player p, String msg) {
		p.sendMessage(prefix + msg);
	}
}
