package sk.xpress.murdermystery.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.event.MinigameEndedEvent;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.Roles;

public class MinigameEndedListener implements Listener {
	
	@EventHandler
	public void onMinigameEnded(MinigameEndedEvent e) {
		Main.removeAllDroppedGold("world");
		Main.getInstance().taskCancel("GoldSpawner");
		
		for(Player player : API.getMinigame().getRoles().get(Roles.ALIVE.getName())) player.getInventory().clear();
	}

}
