package sk.xpress.murdermystery.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import sk.xpress.murdermystery.handler.TestInventory;


public class Test implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getItem() != null) {
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR) {
				ItemStack is = e.getItem();
				if(is.getType() == Material.STONE) {
					TestInventory cs = new TestInventory();
					cs.open(e.getPlayer());
				}
			}
		}
	}

}