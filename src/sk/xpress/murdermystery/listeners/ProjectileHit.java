package sk.xpress.murdermystery.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.api.MinigameState;
import net.graymadness.minigame_api.helper.ChatInfo;
import net.graymadness.minigame_api.helper.ComponentBuilder;
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
			Chat.print("3");
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
				String heroName = null;
				
				Chat.print("DETECTIVE: " + detective.getName() + " MURDER: " + murder.getName() + " HERO: ");
				
				if(Main.isPlayerInnocent(p)) heroName = p.getName();
				else heroName = "";
				sendMessage(detective, detective.getName(), murder.getName(),"INNOCENTS", heroName);
				detective.sendTitle("§aVYHRAL SI!", "§6Zabili ste vraha!", 20,40,20);
				
				for(Player player : API.getMinigame().getRoles().get(Roles.INNOCENT.getName())) {
					sendMessage(player, detective.getName(), murder.getName(),"INNOCENTS", heroName);
					player.sendTitle("§aVYHRAL SI!", "§6Zabili ste vraha!", 20,40,20);
				}
				
				Main.removeAllDroppedGold("world");
				Main.getInstance().taskCancel("GoldSpawner");
				
				sendMessage(murder, detective.getName(), murder.getName(),"INNOCENTS", heroName);
				murder.sendTitle("§aPREHRAL SI!", "§6Zabili ta!", 20,40,20);
				
				for(Player player : API.getMinigame().getRoles().get(Roles.ALIVE.getName())) player.getInventory().clear();
				
			}
		}
	}
	
	public void sendMessage(Player p, String detective, String murderer, String winner, String hero) {
		p.sendMessage("");
		p.sendMessage("§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		p.sendMessage("");
		ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§c§lMURDER MYSTERY")).build());
		p.sendMessage("");
		ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§e§lWinner: §a" + winner)).build());
		p.sendMessage("");
		if(detective != null) ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§9§lDetective: §f" + detective)).build());
		ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§c§lMurderer: §f" + murderer)).build());
		if(!hero.equals("")) ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§e§lHero: §f" + hero)).build());
		p.sendMessage("");
		
		p.sendMessage("§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
	}
	
}
