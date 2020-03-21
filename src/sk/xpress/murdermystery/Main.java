package sk.xpress.murdermystery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.api.IMinigame;
import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.event.MinigameStateChangedEvent;
import sk.xpress.murdermystery.handler.Chat;
import sk.xpress.murdermystery.handler.DetectiveBow;
import sk.xpress.murdermystery.handler.EntityDamageListener;
import sk.xpress.murdermystery.handler.FoodLevelChangeListener;
import sk.xpress.murdermystery.handler.Roles;
import sk.xpress.murdermystery.listeners.ASyncChatListener;
import sk.xpress.murdermystery.listeners.CauldronListener;
import sk.xpress.murdermystery.listeners.ItemDespawnListener;
import sk.xpress.murdermystery.listeners.JoinQuit;
import sk.xpress.murdermystery.listeners.MinigameEndedListener;
import sk.xpress.murdermystery.listeners.MinigameStart;
import sk.xpress.murdermystery.listeners.MinigameStateChangeListener;
import sk.xpress.murdermystery.listeners.PlayerDropItemListener;
import sk.xpress.murdermystery.listeners.PlayerHitEvent;
import sk.xpress.murdermystery.listeners.PlayerItemConsumeListener;
import sk.xpress.murdermystery.listeners.PlayerPickupItem;
import sk.xpress.murdermystery.listeners.ProjectileHit;
import sk.xpress.murdermystery.listeners.Test;
import sk.xpress.murdermystery.listeners.ThrowableSword;


public class Main extends JavaPlugin {

	/*
	 * MinigameEndedEvent endEvent  = new MinigameEndedEvent(API.getMinigame());
       Bukkit.getPluginManager().callEvent(endEvent);
	 * */
	
	private static Main instance;
	public static Main getInstance() { 
		return instance; 
	}
	private MinigameState state = MinigameState.Lobby;
	public MinigameState getState() {
		return state;
	}
	
	private static Map<String, List<Player>> roles = new HashMap<>();
	public static Map<String, List<Player>> getRoles() {
		return roles;
	}
	
	private static Map<String, BukkitTask> tasks = new HashMap<>();
	public static Map<String, BukkitTask> getTasks(){
		return tasks;
	}
	
	private static List<Location> warmUpLocation = new ArrayList<>();
	public static List<Location> getWarmUpLocation(){
		return warmUpLocation;
	}
	
	private static Location arenaMin;
	public static void setArenaMin(Location loc) {
		arenaMin = loc;
	}
	public static Location getArenaMin() {
		return arenaMin;
	}
	
	private static Location arenaMax;
	public void setArenaMax(Location loc) {
		arenaMax = loc;
	}
	public static Location getArenaMax() {
		return arenaMax;
	}
	
	private int goldSpawned;
	public void setGoldSpawned(int spawned) {
		this.goldSpawned = spawned;
	}
	public int getGoldSpawned() {
		return this.goldSpawned;
	}
	
	private static List<Cauldron> cauldrons = new ArrayList<Cauldron>();
	public static List<Cauldron> getCauldrons(){
		return cauldrons;
	}
	
	private static DetectiveBow detectiveSword;
	
	@Override
	public void onEnable() {
		instance = this;
		
		this.saveDefaultConfig();
		
		Chat.print("Registring Listeners...");
		listeners();
		
		Chat.print("Registrings Commands...");
		commandManager();
		
		initializePositions();
				
		API.registerMinigame(new MurderMystery());
		
		detectiveSword = new DetectiveBow();
		
		for(Player p : Bukkit.getOnlinePlayers()) JoinQuit.playerJoin(p);	
		
		loadCauldrons();
		
		API.getMinigame().getRoles().put(Roles.SPECTATOR.getName(), new ArrayList<Player>());
		API.getMinigame().getRoles().put(Roles.ALIVE.getName(), new ArrayList<Player>());
	}
	
	public void onDisable() {
		for(Map.Entry<String, BukkitTask> entry : Main.getTasks().entrySet()) { // vypnuù vöetk˝ tasks
			BukkitTask task = entry.getValue();
			if(task != null) task.cancel();
		}
		
		detectiveSword.destroy();
		
		for(Player p : Bukkit.getOnlinePlayers()) JoinQuit.playerLeave(p);
		
		for(Cauldron caul : cauldrons) caul.getArmorStand().remove();
	}

	public void listeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Test(), this);
		pm.registerEvents(new JoinQuit(), this);
		
		pm.registerEvents(new PlayerPickupItem(), this);
		pm.registerEvents(new PlayerDropItemListener(), this);
		pm.registerEvents(new ItemDespawnListener(), this);
		pm.registerEvents(new EntityDamageListener(), this);
		pm.registerEvents(new FoodLevelChangeListener(), this);
		pm.registerEvents(new ProjectileHit(), this);
		pm.registerEvents(new PlayerHitEvent(), this);
		
		pm.registerEvents(new CauldronListener(), this);
		pm.registerEvents(new PlayerItemConsumeListener(), this);
		
		pm.registerEvents(new MinigameStateChangeListener(), this);
		pm.registerEvents(new MinigameStart(), this);
		pm.registerEvents(new MinigameEndedListener(), this);
		
		pm.registerEvents(new ASyncChatListener(), this);
		
		pm.registerEvents(new ThrowableSword(), this);
		
	}
	
	public void loadCauldrons() {
		ConfigurationSection cs = Main.getInstance().getConfig().getConfigurationSection("murdermystery.arena.cauldrons");
		for(String s : cs.getKeys(false)) {
			
			double x = Main.getInstance().getConfig().getDouble("murdermystery.arena.cauldrons." + s + ".x");
			double y = Main.getInstance().getConfig().getDouble("murdermystery.arena.cauldrons." + s + ".y");
			double z = Main.getInstance().getConfig().getDouble("murdermystery.arena.cauldrons." + s + ".z");		
			String world = Main.getInstance().getConfig().getString("murdermystery.arena.cauldrons." + s + ".world");
			World w = Bukkit.getWorld(world);

			Location loc = new Location(w, x, y, z);
			Location asLoc = loc.clone();
			ArmorStand as = (ArmorStand) w.spawnEntity(asLoc.add(0.5, -0.5, 0.5), EntityType.ARMOR_STAND);
			
			as.setCustomName("ßeCOST ßl2 GOLDS");
			as.setCustomNameVisible(true);
			as.setVisible(false);
			as.setInvulnerable(true);
			
			cauldrons.add(new Cauldron(loc, as));
		}
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
		IMinigame game = API.getMinigame();
		if(game == null) throw new RuntimeException("Api - GetMinigame cannot be initialized and called event!");
		MinigameStateChangedEvent stateChangeEvent = new MinigameStateChangedEvent(game, gameState);
		
		Bukkit.getPluginManager().callEvent(stateChangeEvent);
	}
	
	public void taskCancel(String task) {
		if(tasks.containsKey(task)) {
			if(!tasks.get(task).isCancelled()) {
				tasks.get(task).cancel();
				tasks.remove(task);
			}
		}
	}
	
	public static Roles playerDetectRole(Player p) {
		if(Main.isPlayerDetective(p)) return Roles.DETECTIVE;
		if(Main.isPlayerInnocent(p)) return Roles.INNOCENT;
		if(Main.isPlayerMurder(p)) return Roles.MURDER;
		if(Main.isPlayerSpectator(p)) return Roles.SPECTATOR;
	
		return Roles.NONE;
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
	
	public static boolean isPlayerSpectator(Player p) {
		List<Player> spectators = API.getMinigame().getRoles().get(Roles.SPECTATOR.getName());
		if(spectators != null) 	{
			for(Player spectator : spectators) 
				if(spectator == p) return true;					
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
	
	public static DetectiveBow getDetectiveBow() {
		return detectiveSword;
	}
	
	public static void removeAllDroppedGold(String worldName) {
		World w = Bukkit.getWorld(worldName);
		for(Entity ent : w.getEntities()) {
			if(ent instanceof Item) {
				Item item = (Item) ent;
				ItemStack is = item.getItemStack();
				if(is.getType() == Material.GOLD_INGOT) {
					ent.remove();
				}
			}
		}
	}
}
