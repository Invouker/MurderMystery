package sk.xpress.murdermystery.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.event.MinigameStartedEvent;
import net.graymadness.minigame_api.event.MinigameStateChangedEvent;
import net.graymadness.minigame_api.helper.ChatInfo;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.TeamManager;
import sk.xpress.murdermystery.handler.Roles;

public class MinigameStateChangeListener implements Listener  {

	@EventHandler
	public void onGameStateChange(MinigameStateChangedEvent e) {
		if(e.getState() == MinigameState.Warmup) {
			Main.getInstance().taskCancel("ToWarmUp");
			
			Random rand = new Random();
			
			List<Player> detectives = new ArrayList<>();
			List<Player> murders = new ArrayList<>();
			
			List<Player> players = new ArrayList<>();
			for(Player p : Bukkit.getOnlinePlayers()) {
				players.add(p);
				TeamManager.addTeamToPlayer(p, Roles.INNOCENT);
			}
			
			API.getMinigame().getRoles().put(Roles.ALIVE.getName(), new ArrayList<Player>(players));	
			//Chat.print("StateChane: ALIVES: " + API.getMinigame().getRoles().get(Roles.ALIVE.getName()));
			
			int playerCount = players.size();
			
			
			if(playerCount >= 1) {
				Player detective = players.get(rand.nextInt(playerCount-1)); // -1 - prida
				players.remove(detective);
				detectives.add(detective);
				detective.sendTitle("§eTVOJA ROLA", "§9§lDETECTIVE",20,40,20);
				
				detective.setPlayerListName("§9" + detective.getName());
			}
			
			if(playerCount >= 1) {
				Player murder = players.get(rand.nextInt(playerCount-1)); // -1
				players.remove(murder);
				murders.add(murder);
				murder.sendTitle("§eTVOJA ROLA", "§c§lMURDER",20,40,20);
				murder.setPlayerListName("§e" + murder.getName());
			}
			

			for(Player p : players) {
				p.setPlayerListName("§e" + p.getName());
				p.sendTitle("§eTVOJA ROLA", "§a§lINNOCENT",20,40,20);
			}
			
			/*Chat.print("DETECTIVES: " + detectives);
			Chat.print("MUDERS: " + murders);
			Chat.print("INNOCENTS: " + players);
			Chat.print("ALIVES: " + API.getMinigame().getRoles().get(Roles.ALIVE.getName()));
			*/
			API.getMinigame().getRoles().put(Roles.DETECTIVE.getName(), detectives);
			API.getMinigame().getRoles().put(Roles.INNOCENT.getName(), players);
			API.getMinigame().getRoles().put(Roles.MURDER.getName(), murders);		
			
			
			///
			
			BukkitTask task = new BukkitRunnable() {
				int time = 10;
				@Override
				public void run() {
					//Chat.print("TIME: " + time);
					
					switch(time) {
						case 20:{
							for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, ComponentBuilder.text("Vrah dostane svoj meè o " + time + " sekúnd").build());
							break;
						}
						case 10: {
							for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, ComponentBuilder.text("Vrah dostane svoj meè o " + time + " sekúnd").build());
							break;
						}
						case 5: {
							for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, ComponentBuilder.text("Vrah dostane svoj meè o " + time + " sekúnd").build());
							break;
						}
						case 3: 
						case 2:
						case 1:{
							for(Player p: Bukkit.getOnlinePlayers()) {
								ChatInfo.GENERAL_INFO.send(p, ComponentBuilder.text("Vrah dostane svoj meè o §c" + time + "§e sekúnd").build());
							}
							break;
						}
						case 0:{ // START HRY					
							MinigameStartedEvent startedEvent = new MinigameStartedEvent(API.getMinigame());
							Bukkit.getPluginManager().callEvent(startedEvent);
							
							for(Player p: Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.3F, 1F);
							
							Main.getInstance().setGameState(MinigameState.InProgress);
							Main.getInstance().taskCancel("ToStartUp");
						}
					}

					time --;
				}		
			}.runTaskTimer(Main.getInstance(), 0L, 20L);			
			Main.getTasks().put("ToStartUp", task);
		}
	}
}
