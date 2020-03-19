package sk.xpress.murdermystery.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import net.graymadness.minigame_api.helper.item.ItemBuilder;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.Chat;

public class PlayerPickupItem implements Listener {
	
	@EventHandler
	public void onPlayerPickupItem(EntityPickupItemEvent e) {

		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			ItemStack is = e.getItem().getItemStack();
				
			switch(is.getType()) {
				case GOLD_INGOT: {
					
					//e.setCancelled(true);
					
					if(Main.isPlayerDetective(p)) return;
					if(Main.isPlayerInnocent(p)) {
						
						p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 2f, 2f);
						e.getItem().teleport(new Location(p.getLocation().getWorld(), p.getLocation().getBlockX(), -1, p.getLocation().getBlockZ()));
						
						int getItem = 0;
						if(p.getInventory().getItem(4) != null) getItem = p.getInventory().getItem(4).getAmount();
		
						p.getInventory().setItem(4, new ItemBuilder(Material.GOLD_INGOT).setAmount(getItem+1).build());
						
						Main.getInstance().setGoldSpawned(Main.getInstance().getGoldSpawned()-1);
					
						if(p.getInventory().getItem(4) != null && p.getInventory().getItem(4).getAmount() >= 10) {
							p.getInventory().setItem(8, new ItemBuilder(Material.BOW).build());
							
							if(p.getInventory().getItem(7) == null) p.getInventory().setItem(7, new ItemBuilder(Material.ARROW).setAmount(1).build());
							else p.getInventory().addItem(new ItemBuilder(Material.ARROW).setAmount(1).build());
							
							p.getInventory().getItem(4).setAmount(p.getInventory().getItem(4).getAmount()-10);
						}
							
						e.setCancelled(true);
						
						return;
					}
					
					e.setCancelled(true);		
					break;
				}
				
				case BOW: {
					if(Main.isPlayerInnocent(p)) return;
					
					e.setCancelled(true);
					break;
				}
				
				case ARROW: { // aby neöli zbieraù öÌpy zo zeme..
					e.setCancelled(true);
					break;
				}
				default: return;
			}
		}
	}	
	
}
