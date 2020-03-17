package sk.xpress.murdermystery;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import sk.xpress.murdermystery.handler.Chat;


public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		Chat.print("Registring Listeners...");
		listeners();
		
		Chat.print("Registrings commands...");
		commandManager();
	}
	
public void onDisable() {
		
	}

	public void listeners() {
		PluginManager pm = Bukkit.getPluginManager();
		//pm.registerEvents(new test(), this);
	}
	
	public void commandManager() {
		
	}
	
}
