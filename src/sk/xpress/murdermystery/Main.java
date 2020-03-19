package sk.xpress.murdermystery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import lombok.Getter;
import lombok.Setter;
import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.event.MinigameStateChangedEvent;
import sk.xpress.murdermystery.handler.Chat;
import sk.xpress.murdermystery.handler.Roles;
import sk.xpress.murdermystery.listeners.JoinQuit;
import sk.xpress.murdermystery.listeners.MinigameEvents;
import sk.xpress.murdermystery.listeners.PlayerPickupItem;
import sk.xpress.murdermystery.listeners.Test;


public class Main extends JavaPlugin {

	@Getter private static Main instance;
	@Getter private static MinigameState state = MinigameState.Lobby;
	@Getter private static Map<String, List<Player>> roles = new HashMap<>();
	
	@Getter private static Map<String, BukkitTask> tasks = new HashMap<>();
	
	@Getter private static List<Location> warmUpLocation = new ArrayList<>();
	
	@Getter @Setter private static Location arenaMin;
	@Getter @Setter private static Location arenaMax;
	
	@Getter @Setter private int goldSpawned;
	@Getter private static ProtocolManager protocolManager;
	
	@Override
	public void onEnable() {
		instance = this;
		protocolManager = ProtocolLibrary.getProtocolManager();
		
		this.saveDefaultConfig();
		
		Chat.print("Registring Listeners...");
		listeners();
		
		Chat.print("Registrings Commands...");
		commandManager();
		
		initializePositions();
				
		API.registerMinigame(new MurderMystery());

	}
	
	public void onDisable() {
		
	}

	public void listeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Test(), this);
		pm.registerEvents(new JoinQuit(), this);
		pm.registerEvents(new MinigameEvents(), this);
		pm.registerEvents(new PlayerPickupItem(), this);
	}
	
	public void commandManager() {
		
	}
	
	public void initializePositions() {
		//ARENA MIN/MAX LOADING POSITION
		Location max = loadPosition("murdermystery.arena.min");
		Location min = loadPosition("murdermystery.arena.max");
		World minMaxW = max.getWorld();
		double minX = Math.min(max.getX(), min.getX());
		double minY = Math.min(max.getY(), min.getY());
		double minZ = Math.min(max.getZ(), min.getZ());
		
		double maxX = Math.max(max.getX(), min.getX());
		double maxY = Math.max(max.getY(), min.getY());
		double maxZ = Math.max(max.getZ(), min.getZ());
		
		setArenaMin(new Location(minMaxW, minX, minY, minZ));
		setArenaMax(new Location(minMaxW, maxX, maxY, maxZ));
		
		
		//WARMUP LOADING POSITION
		String world = getConfig().getString("murdermystery.warmup.world");
		World w = Bukkit.getWorld(world);
		if(w == null) throw new NullPointerException("Warmup positions, world cannot be null!");
			
		ConfigurationSection cs = getConfig().getConfigurationSection("murdermystery.warmup.positions");
		for(String s : cs.getKeys(false)) {
			
			double x = getConfig().getDouble("murdermystery.warmup.positions." + s + ".x");
			double y = getConfig().getDouble("murdermystery.warmup.positions." + s + ".y");
			double z = getConfig().getDouble("murdermystery.warmup.positions." + s + ".z");		
			
			Location loc = new Location(w, x, y, z);
			getWarmUpLocation().add(loc);
		}
	}
	
	public Location loadPosition(String path) {
		String world = getConfig().getString(path + ".world");
		double x = getConfig().getDouble(path + ".x");
		double y = getConfig().getDouble(path + ".y");
		double z = getConfig().getDouble(path + ".z");
		World w = Bukkit.getWorld(world);
		
		if(w == null) throw new NullPointerException("World cannot be null!");
		return new Location(w, x, y, z);
	}
	
	public void setGameState(MinigameState gameState) {	
		state = gameState;
		MinigameStateChangedEvent stateChangeEvent = new MinigameStateChangedEvent(API.getMinigame(), gameState);
		Bukkit.getPluginManager().callEvent(stateChangeEvent);
	}
	
	public void taskCancel(String task) {
		if(tasks.containsKey(task)) {
			if(tasks.get(task) != null) {
				tasks.get(task).cancel();
				tasks.remove(task);
			}
		}
	}
	
	public static boolean isPlayerDetective(Player p) {
		List<Player> detectives = API.getMinigame().getRoles().get(Roles.DETECTIVE.getName());
		if(detectives != null) 	{
			for(Player detective : detectives) 
				if(detective == p) return true;					
		}
		return false;
	}
	
	public static boolean isPlayerInnocent(Player p) {
		List<Player> innocents = API.getMinigame().getRoles().get(Roles.INNOCENT.getName());
		if(innocents != null) 	{
			for(Player innocent : innocents) 
				if(innocent == p) return true;					
		}
		
		return false;
	}
	
	public static boolean isPlayerMurder(Player p) {
		List<Player> murders = API.getMinigame().getRoles().get(Roles.MURDER.getName());
		if(murders != null) 	{
			for(Player murder : murders) 
				if(murder == p) return true;					
		}
		return false;
	}
}
