package sk.xpress.murdermystery.listeners;

import org.bukkit.Bukkit;
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

import net.graymadness.minigame_api.helper.item.ItemBuilder;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.Chat;

public class ThrowableSword implements Listener {
	
	@EventHandler
	public void onThrowSword(PlayerInteractEvent e) {
		Action a = e.getAction();
		Player p = e.getPlayer();
		ItemStack is = e.getItem();
		
		if(a != Action.RIGHT_CLICK_AIR) return;
		if(is == null) return;	
		if(is.getType() != Material.IRON_SWORD) return;
		if(Main.getTasks().get("Sword") != null) return;
		
		Location loc = p.getLocation();
		
		Vector dir = p.getEyeLocation().getDirection();
		
		ArmorStand as = loc.getWorld().spawn(loc, ArmorStand.class);
		as.setItemInHand(new ItemBuilder(Material.IRON_SWORD).build());
		
		as.setGravity(false);
		as.setCanPickupItems(false);
		as.setArms(true);
		
		as.setRightArmPose(new EulerAngle(6.15, 0, 4.73));	
		
		
		
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				as.teleport(as.getLocation().add(dir));
				
				if(as.getLocation().add(dir).getBlockY() >= 200) {
					Chat.print("ARMORSTAND IS ABOVE Y 200");
					Main.getInstance().taskCancel("Sword");
					as.remove();
				}
				
				Block block = as.getLocation().add(dir).getBlock();
				Location topHalfAsLoc = block.getLocation().add(0, 1, 0);
				if (block.getWorld().getBlockAt(topHalfAsLoc).getType() == Material.AIR) {
					for (Player p1 : Bukkit.getOnlinePlayers()) {
                   	 if (p1 != p && p1.getLocation().distance(as.getLocation()) <= 1D) {
                        Chat.print("Trafil si hr��a! " + p1.getName());
                        
                        
                        Chat.print("HIT");
                        Main.getInstance().taskCancel("Sword");
    					as.remove();
    					
    					for(Player _p : Bukkit.getOnlinePlayers()) _p.playSound(_p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_NODAMAGE, 2f, 2f);
                     }
                   }
				}else {
					Chat.print("HITTED BLOCK");
					Main.getInstance().taskCancel("Sword");
					as.remove();
				}
			}
			
		}.runTaskTimer(Main.getInstance(), 0L, 2L);
		Main.getTasks().put("Sword", task);
		
	}

}
