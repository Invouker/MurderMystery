package sk.xpress.murdermystery.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.helper.item.ItemBuilder;
import sk.xpress.murdermystery.Cooldown;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.TeamManager;
import sk.xpress.murdermystery.handler.Chat;
import sk.xpress.murdermystery.handler.Roles;

public class PlayerDropItemListener implements Listener {

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		
		switch(e.getItemDrop().getItemStack().getType()) {
			case GOLD_INGOT: 
			case IRON_SWORD: 
			case ARROW: {
				e.setCancelled(true);
				break;
			}
			
			case BOW: {
				if(Main.isPlayerInnocent(p)) {
					e.setCancelled(true);
				}
				
				if(Main.isPlayerDetective(p)) { // DROPNE ITEM, A ZMENI SA NA INNOCENTA		
					
					Main.getDetectiveBow().setLocation(e.getItemDrop().getLocation());	
					Main.getDetectiveBow().spawn();
					
					API.getMinigame().getRoles().get(Roles.DETECTIVE.getName()).remove(p);
					API.getMinigame().getRoles().get(Roles.INNOCENT.getName()).add(p);
					Chat.print("Hr�� " + p.getName() + " sa stal innocentom!");
					
					p.setPlayerListName("�e" + p.getName());
					
					TeamManager.removeTeam(p);
					TeamManager.addTeamToPlayer(p, Roles.INNOCENT);

					new Cooldown("DetectiveDropSword"+p.getName(), 2);
					
					new BukkitRunnable() {
						@Override
						public void run() {
							p.getInventory().setItem(8, new ItemBuilder(Material.AIR).build());
							p.getInventory().setItem(35, new ItemBuilder(Material.AIR).build());
						}
						
					}.runTaskLater(Main.getInstance(), 1L);
					e.setCancelled(true);
				}
				break;
			}

			default:break;
		}
	}
}
