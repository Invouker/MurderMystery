package sk.xpress.murdermystery.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.helper.ChatInfo;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.md_5.bungee.api.ChatColor;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.TeamManager;

public class JoinQuit implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {	
		Player p = e.getPlayer();
		if(API.getMinigame().getState() != MinigameState.Lobby) {
			p.kickPlayer("T·to hra uû sa hr·!");
			return;
		}
		
		e.setJoinMessage(null);
		playerJoin(p);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		playerLeave(e.getPlayer());
		e.setQuitMessage(null);
	}
	
	public static void playerJoin(Player player) {
		player.getInventory().clear();
		player.getInventory().setHeldItemSlot(0);
		player.setGameMode(GameMode.ADVENTURE);
		player.setPlayerListName("ßr" + player.getName());
		
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(2);
		TeamManager.registerTeamsToPlayer(player);

		Location loc = Main.getInstance().loadPosition("murdermystery.join.position");
		double yaw = Main.getInstance().getConfig().getDouble("murdermystery.join.position.yaw");
		double pitch = Main.getInstance().getConfig().getDouble("murdermystery.join.position.pitch");
		
		loc.setYaw((float) yaw);
		loc.setPitch((float) pitch);
		player.teleport(loc);
		
		for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, 
				ComponentBuilder
				.text("Hr·Ë " + player.getName() + " sa pripojil do hry (").color(ChatColor.YELLOW)
				.extra(ComponentBuilder.text(Bukkit.getOnlinePlayers().size() + "").color(ChatColor.RED).build())
				.extra(ComponentBuilder.text("/").color(ChatColor.YELLOW).build())
				.extra(ComponentBuilder.text(API.getMinigame().getMaxPlayers() + "").color(ChatColor.RED).build())
				.extra(ComponentBuilder.text(")").build())
				.build());
		
		if(Bukkit.getOnlinePlayers().size() > API.getMinigame().getMinPlayers()) {
			API.getMinigame().start();
		}
	}

	public static void playerLeave(Player p) {
		TeamManager.removeTeam(p);
		
		for(Player player: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(player, 
				ComponentBuilder
				.text("Hr·Ë " + p.getName() + " sa odpojil z hry (").color(ChatColor.YELLOW)
				.extra(ComponentBuilder.text(String.valueOf(Bukkit.getOnlinePlayers().size()-1)).color(ChatColor.RED).build())
				.extra(ComponentBuilder.text("/").color(ChatColor.YELLOW).build())
				.extra(ComponentBuilder.text(API.getMinigame().getMaxPlayers() + "").color(ChatColor.RED).build())
				.extra(ComponentBuilder.text(")").build())
				.build());
		
		if(Bukkit.getOnlinePlayers().size() < API.getMinigame().getMinPlayers()) { // ak je menej hr·Ëov ako je minim·lny poËet, zruöÌ cel˙ hru a Ëak· sa na hr·Ëov...
			if(Main.getTasks().containsKey("ToWarmUp")){
				Main.getTasks().get("ToWarmUp").cancel();
				
				for(Player player: Bukkit.getOnlinePlayers()) ChatInfo.ERROR.send(player, ComponentBuilder.text("Nieje dostatok hr·Ëov pre spustenie minihry!").color(ChatColor.RED).build() );
			}
		}
	}
}
