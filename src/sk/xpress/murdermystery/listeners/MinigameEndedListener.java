package sk.xpress.murdermystery.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.event.MinigameEndedEvent;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.Roles;

public class MinigameEndedListener implements Listener {
	
	@EventHandler
	public void onMinigameEnded(MinigameEndedEvent e) {
		Main.getInstance().setGameState(MinigameState.PostGame);
		Main.removeAllDroppedGold("world");
		Main.getInstance().taskCancel("GoldSpawner");
		
		for(Player player : API.getMinigame().getRoles().get(Roles.ALIVE.getName())) player.getInventory().clear();
		
		Main.getDetectiveBow().destroy();
		
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					JoinQuit.playerLeave(p);
					JoinQuit.playerJoin(p);
				}
				
				Main.getInstance().setGameState(MinigameState.Lobby);
			}
			
		}.runTaskLater(Main.getInstance(), 20*10L);
	}

}
