package sk.xpress.murdermystery.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.api.MinigameState;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.TeamManager;
import sk.xpress.murdermystery.handler.Chat;
import sk.xpress.murdermystery.handler.Roles;

public class ProjectileHit implements Listener {
	
	@EventHandler
	public void onArrowHit(ProjectileHitEvent e){
		if(e.getEntity() instanceof Arrow){
			Arrow arrow = (Arrow) e.getEntity();
			arrow.remove();
			
			if(!(arrow.getShooter() instanceof Player) && !(e.getHitEntity() instanceof Player )) return;
			Player p = (Player) arrow.getShooter();
			Player target = (Player) e.getHitEntity();

			Chat.print("DETECT ROLE OF PLAYER: " + Main.playerDetectRole(p).getName());
			Chat.print("DETECT ROLE OF TARGET: " + Main.playerDetectRole(target).getName());
			
			if(Main.isPlayerInnocent(target)) {
				Chat.print("4");
				p.setGameMode(GameMode.SPECTATOR);
				target.setGameMode(GameMode.SPECTATOR);
				
				if(Main.isPlayerDetective(p)) {
					Main.getDetectiveBow().setLocation(p.getLocation().add(0, 1, 0));	
					Main.getDetectiveBow().spawn();
				}
				
				API.getMinigame().getRoles().get(Roles.SPECTATOR.getName()).add(target);
				API.getMinigame().getRoles().get(Roles.SPECTATOR.getName()).add(p);
				
				API.getMinigame().getRoles().get(Main.playerDetectRole(p).getName()).remove(p);
				API.getMinigame().getRoles().get(Main.playerDetectRole(target).getName()).remove(target);
				
				TeamManager.removeTeam(p);
				TeamManager.removeTeam(target);
		
				
				// ZABIL INNOCENTA 
				// ?!?!? Čo sa opovažuje, buzerant :D
			}
			
			
			if(Main.isPlayerMurder(target)) { // WIN THE GAME, OMEGALUL
				
				Main.getInstance().setGameState(MinigameState.PostGame);
				
				Player detective = API.getMinigame().getRoles().get(Roles.DETECTIVE.getName()).get(0);
				Player murder = API.getMinigame().getRoles().get(Roles.MURDER.getName()).get(0);
				
				
				
				if(Main.isPlayerInnocent(p)) {
					String detectiveName;
					if(detective == null) detectiveName = "";
					else detectiveName = detective.getName();
					
					Chat.sendInnocentWinMessage(detectiveName, murder.getName(), p.getName());
				}
				
				if(Main.isPlayerDetective(p)) {
					Chat.sendInnocentWinMessage(p.getName(), murder.getName(), "");
				}

				
				
			}
		}
	}
	

}
