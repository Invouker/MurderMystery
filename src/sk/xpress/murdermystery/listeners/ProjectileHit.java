package sk.xpress.murdermystery.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import net.graymadness.minigame_api.api.API;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.Roles;

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

			if(Main.isPlayerInnocent(target)) {
				target.setGameMode(GameMode.SPECTATOR);
				p.setGameMode(GameMode.SPECTATOR);
				
				API.getMinigame().getRoles().get(Roles.SPECTATOR.getName()).add(target);
				API.getMinigame().getRoles().get(Roles.SPECTATOR.getName()).add(p);
				
				Roles pRole = Main.playerDetectRole(p);
				if(pRole == Roles.NONE) throw new IllegalArgumentException("Illegal role for player!");
				API.getMinigame().getRoles().get(pRole.getName()).remove(p);
				
				API.getMinigame().getRoles().get(Roles.INNOCENT.getName()).remove(p);
				// ZABIL INNOCENTA 
				// ?!?!? Èo sa opovažuje, buzerant :D
			}
		}
	}
}
