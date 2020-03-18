package sk.xpress.murdermystery.listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.helper.ChatInfo;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.md_5.bungee.api.ChatColor;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.Chat;

public class JoinQuit implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		double x = Main.getInstance().getConfig().getDouble("murdermystery.join.position.x");
		double y = Main.getInstance().getConfig().getDouble("murdermystery.join.position.y");
		double z = Main.getInstance().getConfig().getDouble("murdermystery.join.position.z");
		String world = Main.getInstance().getConfig().getString("murdermystery.join.position.world");
		
		double yaw = Main.getInstance().getConfig().getDouble("murdermystery.join.position.yaw");
		double pitch = Main.getInstance().getConfig().getDouble("murdermystery.join.position.pitch");
		World w = Bukkit.getWorld(world);
		
		if(w != null) {
			Location loc = new Location(w, x,y,z);
			loc.setYaw((float) yaw);
			loc.setPitch((float) pitch);
			e.getPlayer().teleport(loc);
		}
		
		e.setJoinMessage(null);
		
		for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, 
				ComponentBuilder
				.text("Hráè " + e.getPlayer().getName() + " sa pripojil do hry (").color(ChatColor.YELLOW)
				.extra(ComponentBuilder.text(Bukkit.getOnlinePlayers().size() + "").color(ChatColor.RED).build())
				.extra(ComponentBuilder.text("/").color(ChatColor.YELLOW).build())
				.extra(ComponentBuilder.text(API.getMinigame().getMaxPlayers() + "").color(ChatColor.RED).build())
				.extra(ComponentBuilder.text(")").build())
				.build());
		
		if(Bukkit.getOnlinePlayers().size() > API.getMinigame().getMinPlayers() || true) { // ak je viac hráèov ako minimálne, spustí warmup 
			BukkitTask task = new BukkitRunnable() {
				int i = 10;
				@Override
				public void run() {
					Chat.print("I: " + i);
					switch(i) {
						case 5:
						case 4: {
							// MESSAGE
							ChatInfo.GENERAL_INFO.send(e.getPlayer(), 
									ComponentBuilder
									.text("Hra sa zaène o " + i)
									.color(ChatColor.YELLOW)
									.build());
							break;
						}
					
						case 3:
						case 2:
						case 1: {
							// TITLE
							e.getPlayer().sendTitle("§e§lHra zaène o", "§a" + i, 20, 40, 20);
							break;
						}
						
						case 0:{ // START
							Random rand = new Random();
							int random = rand.nextInt(Main.getWarmUpLocation().size()-1);
							Location loc = Main.getWarmUpLocation().get(random);
							e.getPlayer().teleport(loc);
							
							Main.getInstance().setGameState(MinigameState.Warmup);
							Main.getInstance().taskCancel("ToWarmUp");
						}
					}
					
					i--;
				}
			}.runTaskTimer(Main.getInstance(), 0L, 20L);
			
			Main.getTasks().put("ToWarmUp", task);
			//Main.getInstance().setGameState(MinigameState.Warmup);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, 
				ComponentBuilder
				.text("Hráè " + e.getPlayer().getName() + " sa odpojil z hry (").color(ChatColor.YELLOW)
				.extra(ComponentBuilder.text(Bukkit.getOnlinePlayers().size() + "").color(ChatColor.RED).build())
				.extra(ComponentBuilder.text("/").color(ChatColor.YELLOW).build())
				.extra(ComponentBuilder.text(API.getMinigame().getMaxPlayers() + "").color(ChatColor.RED).build())
				.extra(ComponentBuilder.text(")").build())
				.build());
		
		if(Bukkit.getOnlinePlayers().size() < API.getMinigame().getMinPlayers()) { // ak je menej hráèov ako je minimálny poèet, zruší celú hru a èaká sa na hráèov...
			if(Main.getTasks().containsKey("ToWarmUp")){
				Main.getTasks().get("ToWarmUp").cancel();
				
				ChatInfo.ERROR.send(e.getPlayer(),
						ComponentBuilder
						.text("Nieje dostatok hráèov pre spustenie minihry!").color(ChatColor.RED)
						.build()
						);
			}
		}
	}
}
