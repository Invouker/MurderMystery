package sk.xpress.murdermystery.handler;

import org.bukkit.Bukkit;

public class Chat {
	private final static String cPrefix = "[MurderMystery] §f";
	
	public static void print(String msg) {
		Bukkit.getConsoleSender().sendMessage(cPrefix + msg);
	}
}
