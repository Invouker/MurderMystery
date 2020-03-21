package sk.xpress.murdermystery.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import sk.xpress.murdermystery.Main;

public class PlayerItemConsumeListener implements Listener {

	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
		if(e.getItem().getType() == Material.POTION || e.getItem().getType() == Material.GLASS_BOTTLE) {
			new BukkitRunnable() {

				@Override
				public void run() {
					e.getPlayer().getInventory().setItem(2, new ItemStack(Material.AIR));
				}
				
			}.runTaskAsynchronously(Main.getInstance());
			
		}
	}
}
