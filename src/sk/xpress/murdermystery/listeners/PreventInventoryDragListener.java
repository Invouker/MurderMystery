package sk.xpress.murdermystery.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PreventInventoryDragListener implements Listener {
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getClickedInventory() == e.getWhoClicked().getInventory()) {
			if(!e.getWhoClicked().isOp()) {
				e.setCancelled(true);
			}
		}
	}
}
