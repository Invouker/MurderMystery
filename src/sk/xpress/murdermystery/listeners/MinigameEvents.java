package sk.xpress.murdermystery.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
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
import sk.xpress.murdermystery.handler.Roles;

public class MinigameEvents implements Listener  {

	@EventHandler
	public void onGameStateChange(MinigameStateChangedEvent e) {
		if(e.getState() == MinigameState.Warmup) {
			BukkitTask task = new BukkitRunnable() {
				int time = 20;
				@Override
				public void run() {
					Chat.print("TIME: " + time);
					switch(time) {
						case 20:{
							for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, ComponentBuilder.text("Hra zaËne za " + time + " sek˙nd").build());
							break;
						}
						case 10: {
							for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, ComponentBuilder.text("Hra zaËne za " + time + " sek˙nd").build());
							break;
						}
						case 5: {
							for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, ComponentBuilder.text("Hra zaËne za " + time + " sek˙nd").build());
							break;
						}
						case 3: 
						case 2:
						case 1:{
							for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, ComponentBuilder.text("Hra zaËne za " + time + " sek˙nd").build());
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
			Random rand = new Random();
			
			List<Player> detectives = new ArrayList<Player>();
			List<Player> murders = new ArrayList<Player>();
			
			Chat.print("IN PROGRESS");
			Collection<? extends Player> _players = Bukkit.getOnlinePlayers();
			List<Player> players = new ArrayList<Player>();
			for(Player p : _players) players.add(p);
			
			int playerCount = players.size();
			
			
			Player detective = players.get(rand.nextInt(playerCount)); // -1 - pridaù
			players.remove(detective);
			detectives.add(detective);
			detective.sendTitle("ßeYou are", "ß9ßlDETECTIVE",20,40,20);
			
			
			Player murder = players.get(rand.nextInt(playerCount)); // -1
			players.remove(murder);
			murders.add(murder);
			detective.sendTitle("ßeYou are", "ßcßlMURDER",20,40,20);
			
			
			for(Player p : players) p.sendTitle("ßeYou are", "ßaßlINNOCENT",20,40,20);
			API.getMinigame().getRoles().put(Roles.DETECTIVE.getName(), detectives);
			API.getMinigame().getRoles().put(Roles.INNOCENT.getName(), players);
			API.getMinigame().getRoles().put(Roles.MURDER.getName(), murders);
		}
	}
}
