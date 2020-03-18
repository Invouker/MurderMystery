package sk.xpress.murdermystery.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.event.MinigameStateChangedEvent;
import net.graymadness.minigame_api.helper.ChatInfo;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.Chat;

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
			
		}
	}
}
