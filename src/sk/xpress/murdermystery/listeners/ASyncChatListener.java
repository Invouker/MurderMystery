package sk.xpress.murdermystery.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ASyncChatListener implements Listener {

	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		if(p.isOp()) msg = ChatColor.translateAlternateColorCodes('&', msg);
		e.setFormat("§e" + p.getName() + " §8»§7 " + msg);
	}
}
