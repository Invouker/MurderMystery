package sk.xpress.murdermystery.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.event.MinigameEndedEvent;
import net.graymadness.minigame_api.helper.ChatInfo;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.md_5.bungee.api.ChatColor;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.Chat;
import sk.xpress.murdermystery.handler.Roles;

public class PlayerHitEvent implements Listener {
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Entity hittedEntity = e.getEntity();
		Entity hitterEntity = e.getDamager();
		
		if(hittedEntity instanceof Player && hitterEntity instanceof Player) {
			Player p = (Player) hitterEntity;
			Player target = (Player) hittedEntity;
			
			Chat.print("INSTANCEOF");
			Chat.print("DETECT ROLE TARGET: " + Main.playerDetectRole(target));
			Chat.print("DETECT ROLE PLAYER: " + Main.playerDetectRole(p));
			
			//Chat.print("TARGET: " + target.getName());
			//Chat.print("PLAYER: " + p.getName());
			
			if(Main.isPlayerInnocent(p) && Main.isPlayerInnocent(target)) {
				Chat.print("INNOCENT = INNOCENT");
				e.setCancelled(true);
				return;
			}
			
			if(Main.isPlayerDetective(p) && Main.isPlayerInnocent(target)) {
				Chat.print("DETECTIVE = INNOCENT");
				e.setCancelled(true);
				return;
			}
			
			if(Main.isPlayerDetective(target) && Main.isPlayerInnocent(p)) {
				Chat.print("INNOCENT = DETECTIVE");
				e.setCancelled(true);
				return;
			}
			
			if(Main.isPlayerMurder(p)) {
				if(Main.isPlayerInnocent(target)) {
					Chat.print("MURDER = INNOCENT");
					API.getMinigame().getRoles().get(Roles.INNOCENT.getName()).remove(target);   
           			API.getMinigame().getRoles().get(Roles.SPECTATOR.getName()).add(target);
           			target.getInventory().clear();
           			
           			API.getMinigame().getRoles().get(Roles.ALIVE.getName()).remove(target);   
           			target.setGameMode(GameMode.SPECTATOR);
           			ChatInfo.GENERAL_INFO.send(target, ComponentBuilder.text("§c§lZOMREL SI!§e Bol si zabitý vrahom!").build());
           			target.sendTitle("§c§lZOMREL SI", "", 20,40,20);
					
					e.setCancelled(true);
				}
				
				if(Main.isPlayerDetective(target)) {
					Chat.print("MURDER = DETECTIVE");
					API.getMinigame().getRoles().get(Roles.DETECTIVE.getName()).remove(target);                  			
           			API.getMinigame().getRoles().get(Roles.SPECTATOR.getName()).add(target);
           			API.getMinigame().getRoles().get(Roles.ALIVE.getName()).remove(target);   
           			API.getMinigame().getRoles().get(Roles.DETECTIVE.getName()).remove(target);   
           			
           			target.getInventory().clear();
           			

           			Main.getDetectiveBow().setLocation(target.getLocation().add(0, 1, 0));	
					Main.getDetectiveBow().spawn();
					
					target.setGameMode(GameMode.SPECTATOR);
					
					for(Player player : Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(player, ComponentBuilder.text("Detektiv bol zabitý!").color(ChatColor.BLUE).build());
					ChatInfo.GENERAL_INFO.send(target, ComponentBuilder.text("§c§lZOMREL SI!§e Bol si zabitý vrahom!").build());
					target.sendTitle("§c§lZOMREL SI", "", 20,40,20);
					for(Player player: Bukkit.getOnlinePlayers()) player.sendTitle("§9DETECETIVE", "Bol zabitý", 20, 40, 20);
					
					e.setCancelled(true);
				}

				if(API.getMinigame().getRoles().get(Roles.ALIVE.getName()).size() <= 1) {
					String detectiveName;
					if(API.getMinigame().getRoles().get(Roles.DETECTIVE.getName()).size() >= 1 && API.getMinigame().getRoles().get(Roles.DETECTIVE.getName()).get(0) != null) 
						detectiveName = API.getMinigame().getRoles().get(Roles.DETECTIVE.getName()).get(0).getName();
					else detectiveName = "";
       				
					Chat.sendMurderWinMessage(detectiveName, p.getName());        				
					
					MinigameEndedEvent endEvent  = new MinigameEndedEvent(API.getMinigame());
				    Bukkit.getPluginManager().callEvent(endEvent);
				}	
			}
			
		}
	}

}
