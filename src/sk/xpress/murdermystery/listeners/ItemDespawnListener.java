package sk.xpress.murdermystery.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;

public class ItemDespawnListener implements Listener {
	
	@EventHandler
	public void onItemDespawnEvent(ItemDespawnEvent  e) {
		switch(e.getEntity().getItemStack().getType()) {
			case BOW: 
			case GOLD_INGOT:
				e.setCancelled(true);
		default: break;
		}
	}

}
