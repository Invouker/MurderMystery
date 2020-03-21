package sk.xpress.murdermystery.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player)
		switch(e.getCause()) {
			case FALL:	
			case ENTITY_ATTACK:
			case CUSTOM:
			case FIRE:
			case PROJECTILE:
				e.setCancelled(true);
			default:break;
		}
	}

}
