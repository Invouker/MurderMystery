package sk.xpress.murdermystery;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.sun.istack.internal.NotNull;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.api.IMinigame;
import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.event.MinigameEndedEvent;
import net.graymadness.minigame_api.helper.ChatInfo;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.md_5.bungee.api.ChatColor;
import sk.xpress.murdermystery.handler.Chat;
import sk.xpress.murdermystery.listeners.JoinQuit;

public class MurderMystery implements IMinigame {
 
	@Override
	public boolean canJoinDuringWarmup() {
		return false;
	}

	@Override
	public @NotNull String getCodename() {
		return "murdermystery";
	}

	@Override
	public int getMaxPlayers() {
		return 16;
	}

	@Override
	public int getMinPlayers() {
		return 8;
	}

	@Override
	public @NotNull Map<String, List<Player>> getRoles() {
		return Main.getRoles();
	}

	@Override
	public @NotNull MinigameState getState() {
		return Main.getInstance().getState();
	}

	@Override
	public void start() {
		if(Main.getTasks().get("ToWarmUp") == null) { // ak ešte nebeží!!
			BukkitTask task = new BukkitRunnable() {
				int i = 10;
				@Override
				public void run() {
					Chat.print("I: " + i);
					switch(i) {
						case 5:
						case 4: {
							// MESSAGE
							for(Player p: Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(p, 
									ComponentBuilder
									.text("Hra sa začne o " + i + "sek")
									.color(ChatColor.YELLOW)
									.build());
							break;
						}
					
						case 3:
						case 2:
						case 1: {
							// TITLE
							for(Player p: Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 1F);
								p.sendTitle("§e§lHra začne", "§a" + i + "sek", 20, 40, 20);
							}
							break;
						}
						
						case 0:{ // START
							Random rand = new Random();
							
							for(Player p : Bukkit.getOnlinePlayers()) {
								int random = rand.nextInt(Main.getWarmUpLocation().size()-1);
								Location loc = Main.getWarmUpLocation().get(random);
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1F, 1F);
							}
							
							Main.getInstance().setGameState(MinigameState.Warmup);
						}
					}
					
					i--;
				}
			}.runTaskTimer(Main.getInstance(), 0L, 20L);
			
			Main.getTasks().put("ToWarmUp", task);
			//Main.getInstance().setGameState(MinigameState.Warmup);
		}
	}

	@Override
	public void stop() {
		if(API.getMinigame().getState() != MinigameState.Lobby || API.getMinigame().getState() != MinigameState.PostGame) {
			
			for(Map.Entry<String, BukkitTask> entry : Main.getTasks().entrySet()) { // vypnuť všetký tasks
				BukkitTask task = entry.getValue();
				if(task != null) task.cancel();
			}
			
		//	for(Player p : Bukkit.getOnlinePlayers()) JoinQuit.playerLeave(p);
			
			MinigameEndedEvent endEvent  = new MinigameEndedEvent(API.getMinigame());
		    Bukkit.getPluginManager().callEvent(endEvent);
		    
		    for(Player p : Bukkit.getOnlinePlayers()) ChatInfo.ERROR.send(p, ComponentBuilder.text("Hra bola predbežne stopnutá!").build());
		}
	}
		
}
