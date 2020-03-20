package sk.xpress.murdermystery.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.event.MinigameStateChangedEvent;
import net.graymadness.minigame_api.helper.ChatInfo;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.Chat;
import sk.xpress.murdermystery.handler.GoldGenerator;
import sk.xpress.murdermystery.handler.Roles;

public class MinigameEvents implements Listener  {

	@EventHandler
	public void onGameStateChange(MinigameStateChangedEvent e) {
		if(e.getState() == MinigameState.Warmup) {
			//Main.getTasks().get("ToWarmUp")
			Main.getInstance().taskCancel("ToWarmUp");
			
			BukkitTask task = new BukkitRunnable() {
				int time = 10;
				@Override
				public void run() {
					Chat.print("TIME: " + time);
					switch(time) {
						case 20:{
							for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, ComponentBuilder.text("Rozdelenie rolÌ o " + time + " sek˙nd").build());
							break;
						}
						case 10: {
							for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, ComponentBuilder.text("Rozdelenie rolÌ o " + time + " sek˙nd").build());
							break;
						}
						case 5: {
							for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, ComponentBuilder.text("Rozdelenie rolÌ o " + time + " sek˙nd").build());
							break;
						}
						case 3: 
						case 2:
						case 1:{
							for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, ComponentBuilder.text("Rozdelenie rolÌ o " + time + " sek˙nd").build());
							break;
						}
						case 0:{ // START HRY
							Main.getInstance().setGameState(MinigameState.InProgress);
							this.cancel();
						}
					}
					time --;
				}
				
			}.runTaskTimer(Main.getInstance(), 0L, 20L);
			
			Main.getTasks().put("ToStartUp", task);
		}
		
		
		if(e.getState() == MinigameState.InProgress) {
			// SAMOTNE JADRO HRY SPUSTIç....
			// ROZDELIT ROLE - INNOCENT/DETECTIVE/MURDER
			Main.getInstance().taskCancel("ToStartUp");
			
			goldSpawner();
			
			Random rand = new Random();
			
			List<Player> detectives = new ArrayList<Player>();
			List<Player> murders = new ArrayList<Player>();
			
			Chat.print("IN PROGRESS");
			
			List<Player> players = new ArrayList<Player>();
			for(Player p : Bukkit.getOnlinePlayers()) players.add(p);
			
			int playerCount = players.size();
			
			if(players.size() > 1) {
				Player detective = players.get(rand.nextInt(playerCount)); // -1 - pridaù
				players.remove(detective);
				detectives.add(detective);
				detective.sendTitle("ßeYou are", "ß9ßlDETECTIVE",20,40,20);
			}
			
			if(players.size() > 1) {
				Player murder = players.get(rand.nextInt(playerCount)); // -1
				players.remove(murder);
				murders.add(murder);
				murder.sendTitle("ßeYou are", "ßcßlMURDER",20,40,20);
			}
			
			for(Player p : players) p.sendTitle("ßeYou are", "ßaßlINNOCENT",20,40,20);
			
			Chat.print("DETECTIVES: " + detectives);
			Chat.print("MUDERS: " + murders);
			Chat.print("INNOCENTS: " + players);
			
			API.getMinigame().getRoles().put(Roles.DETECTIVE.getName(), detectives);
			API.getMinigame().getRoles().put(Roles.INNOCENT.getName(), players);
			API.getMinigame().getRoles().put(Roles.MURDER.getName(), murders);		
		}	
	}
	
	public void goldSpawner() {
		int respawnTime = Main.getInstance().getConfig().getInt("murdermystery.options.gold-spawner-rate");
		String world = Main.getInstance().getConfig().getString("murdermystery.options.gold-spawner-world");
		World w = Bukkit.getWorld(world);
		
		if(w == null) throw new NullPointerException("Gold spawner world cannot be null or world doesnt exists.");
		
		BukkitTask task = new BukkitRunnable() {
			int goldSpawnedOnce = Main.getInstance().getConfig().getInt("murdermystery.options.max-gold-spawned-once");
			
			
			@Override
			public void run() {
				int goldSpawned = Main.getInstance().getGoldSpawned();
				if(goldSpawned < goldSpawnedOnce) {
					new GoldGenerator(w);
					Main.getInstance().setGoldSpawned(goldSpawned+1);
				}
			}
			
		}.runTaskTimer(Main.getInstance(), 0L, respawnTime*20L);
		Main.getTasks().put("GoldSpawner", task);
	}
}
