package sk.xpress.murdermystery.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import sk.xpress.murdermystery.Main;

public class ProjectileHit implements Listener {
	
	@EventHandler
	public void onArrowHit(ProjectileHitEvent e){
		if(e.getEntity() instanceof Arrow){
			Arrow arrow = (Arrow) e.getEntity();
			arrow.remove();
			Player p = null, target = null;
			
			if(e.getEntity() instanceof Player) p = (Player) e.getEntity();
			if(e.getHitEntity() instanceof Player) target = (Player) e.getHitEntity();
			if(p == null && target == null) return;
			
			if(Main.getInstance().isPlayerInnocent(target)) {
				// ZABIL INNOCENTA 
				// ?!?!? Èo sa opovažuje, buzerant :D
			}
		}
	}
}
