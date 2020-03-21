package sk.xpress.murdermystery.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.event.MinigameEndedEvent;
import net.graymadness.minigame_api.helper.ChatInfo;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.graymadness.minigame_api.helper.item.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import sk.xpress.murdermystery.ActionBar;
import sk.xpress.murdermystery.Cooldown;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.Chat;
import sk.xpress.murdermystery.handler.Roles;

public class ThrowableSword implements Listener {
	
	private final int swordCooldown = Main.getInstance().getConfig().getInt("murdermystery.options.murder.sword-cooldown");
	
	@EventHandler
	public void onThrowSword(PlayerInteractEvent e) {
		Action a = e.getAction();
		Player p = e.getPlayer();
		ItemStack is = e.getItem();
		
		if(a != Action.RIGHT_CLICK_AIR) return;
		if(is == null) return;	
		if(is.getType() != Material.IRON_SWORD) return;
		if(Main.getTasks().get("Sword") != null) return;
		if(Cooldown.hasCooldown("Sword")) return;
		else Main.getInstance().taskCancel("Cooldown:Sword:"+p.getName());
		
		
		//if(!Main.isPlayerMurder(p)) return; // v komentárí, kvôli testovaniu!
		
		Location loc = p.getLocation();
		
		Vector dir = p.getEyeLocation().getDirection();
		
		ArmorStand as = loc.getWorld().spawn(loc, ArmorStand.class);
		as.setItemInHand(new ItemBuilder(Material.IRON_SWORD).build());
		
		as.setGravity(false);
		as.setCanPickupItems(false);
		as.setArms(true);
		as.setInvulnerable(true);
		as.setVisible(false);
		
		as.setRightArmPose(new EulerAngle(6.15, 0, 4.73));	
		
		new Cooldown("Sword", swordCooldown);
		
		BukkitTask swordTask = new BukkitRunnable() {
			@Override
			public void run() {
				int cooldown = Cooldown.getTimeCooldown("Sword");
				if(cooldown <= 0) {
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 8F, 8F);
					Main.getInstance().taskCancel("Cooldown:Sword:"+p.getName());
					this.cancel();
				}
				ActionBar.sendActionBar(p, ComponentBuilder.text(calculateCooldown(cooldown)).build());
			}
			
		}.runTaskTimer(Main.getInstance(), 0L, 10L);
		Main.getTasks().put("Cooldown:Sword:"+p.getName(), swordTask);
		
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				as.teleport(as.getLocation().add(dir));
				
				if(as.getLocation().add(dir).getBlockY() >= 200) {
					Main.getInstance().taskCancel("Sword");
					as.remove();
				}
				
				Block block = as.getLocation().add(dir).getBlock();
				Location topHalfAsLoc = block.getLocation().add(0, 1, 0);
				if (block.getWorld().getBlockAt(topHalfAsLoc).getType() == Material.AIR) {
					
					List<Player> removeFromAlives = new ArrayList<Player>();
			
					for (Player target: API.getMinigame().getRoles().get(Roles.ALIVE.getName())) { //KONTROLA AK Už všetkých zabil !!!
					//Player target = it.next();
                   	 if (target != p && target.getLocation().distance(as.getLocation()) <= 1D) {

                   		 Chat.print("ALIVE SIZE: " + API.getMinigame().getRoles().get(Roles.ALIVE.getName()).size());
                   		 
                   		 if(Main.isPlayerDetective(target)) {
                   			API.getMinigame().getRoles().get(Roles.DETECTIVE.getName()).remove(target);                  			
                   			//it.remove();// REMOVE FROM ALIVE LIST
                   			
                   			API.getMinigame().getRoles().get(Roles.SPECTATOR.getName()).add(target);
                   			for(Player player : Bukkit.getOnlinePlayers()) ChatInfo.GENERAL_INFO.send(player, ComponentBuilder.text("Detektiv bol zabitý!").color(ChatColor.BLUE).build());
                   			API.getMinigame().getRoles().get(Roles.DETECTIVE.getName()).remove(target);   
                   			
                   			target.getInventory().clear();
                   			
                   			removeFromAlives.add(target);
                   			
                   			Main.getDetectiveBow().setLocation(target.getLocation().add(0, 1, 0));	
        					Main.getDetectiveBow().spawn();
        					
        					target.setGameMode(GameMode.SPECTATOR);
        					ChatInfo.GENERAL_INFO.send(target, ComponentBuilder.text("§c§lZOMREL SI!§e Bol si zabitý vrahom!").build());
        					target.sendTitle("§c§lZOMREL SI", "", 20,40,20);
        					for(Player player: Bukkit.getOnlinePlayers()) player.sendTitle("§9DETECETIVE", "Bol zabitý", 20, 40, 20);
                   		 }
                   		 
                   		 if(Main.isPlayerInnocent(target)) {
                   			API.getMinigame().getRoles().get(Roles.INNOCENT.getName()).remove(target);   
                   			API.getMinigame().getRoles().get(Roles.SPECTATOR.getName()).add(target);
                   			target.getInventory().clear();
                   			
                   			removeFromAlives.add(target);
                   			target.setGameMode(GameMode.SPECTATOR);
                   			ChatInfo.GENERAL_INFO.send(target, ComponentBuilder.text("§c§lZOMREL SI!§e Bol si zabitý vrahom!").build());
                   			target.sendTitle("§c§lZOMREL SI", "", 20,40,20);
                   		 }
                   		 
                        Main.getInstance().taskCancel("Sword");
    					as.remove();
    					
    					for(Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH, 2f, 2f);	
                     }
                   }
					
					// 
					
					for(Player t : removeFromAlives) {
						API.getMinigame().getRoles().get(Roles.ALIVE.getName()).remove(t);
						
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
					
					
				}else {
					Main.getInstance().taskCancel("Sword");
					as.remove();
				}
			}
			
		}.runTaskTimer(Main.getInstance(), 0L, 1L);
		Main.getTasks().put("Sword", task);
		
	}
	
	public String calculateCooldown(int perc) { // hodnoty od 1-10
		switch(perc%10) {
		  case 0: return "§7[§a▮▮▮▮▮▮▮▮▮▮§7]";
	      case 1: return "§7[§a▮▮▮▮▮▮▮▮▮§c▮§7]";
	      case 2: return "§7[§a▮▮▮▮▮▮▮▮§c▮▮§7]";
	      case 3: return "§7[§a▮▮▮▮▮▮▮§c▮▮▮§7]";
	      case 4: return "§7[§a▮▮▮▮▮▮§c▮▮▮▮§7]";
	      case 5: return "§7[§a▮▮▮▮▮§c▮▮▮▮▮§7]";
	      case 6: return "§7[§a▮▮▮▮§c▮▮▮▮▮▮§7]";
	      case 7: return "§7[§a▮▮▮§c▮▮▮▮▮▮▮§7]";
	      case 8: return "§7[§a▮▮§c▮▮▮▮▮▮▮▮§7]";
	      case 9: return "§7[§a▮§c▮▮▮▮▮▮▮▮▮§7]";
	      case 10: return "§7§l[§c▮▮▮▮▮▮▮▮▮▮§7§l]";
	      default: return "";
	    }
	}

}
