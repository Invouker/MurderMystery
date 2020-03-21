package sk.xpress.murdermystery.listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.graymadness.minigame_api.helper.item.ItemBuilder;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.Chat;

public class CauldronListener implements Listener {
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if(e.getHand() != EquipmentSlot.HAND) return;
		
		Action a = e.getAction();
		if(a == Action.RIGHT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			if(b.getType() == Material.CAULDRON) {
				//Chat.print("LOCATION OF CAULDRON IS: " + b.getLocation());
				
				Player p = e.getPlayer();
				// AK KLIKOL NA CAULDRON...
				if(Main.isPlayerDetective(p) || Main.isPlayerInnocent(p)) {
		
					if(!isCauldronValidLocation(b)) return;
					if(p.getInventory().getItem(4) != null && p.getInventory().getItem(4).getType() == Material.GOLD_INGOT) {
						
						
						ItemStack is = p.getInventory().getItem(4);
						if(is.getAmount() >= 2) {
							is.setAmount(is.getAmount()-2);
							p.getInventory().setItem(2, giveRandomPotion(p));
							// DO RANDOM POTION EFFECT
						}
					}
				}
				
			}
		}
		
	}
	
	public ItemStack giveRandomPotion(Player p) {
		ItemStack is = new ItemBuilder(Material.POTION).setName(ComponentBuilder.text("§dRandom Effect").build()).addItemFlag(ItemFlag.HIDE_POTION_EFFECTS).build();
		PotionEffect pe;
		Random rand = new Random();
		switch(rand.nextInt(5)) {
			case 0: {
				pe = new PotionEffect(PotionEffectType.INVISIBILITY, getRandomTime(10, 20), 0);
				break;
			}
			case 1: {
				pe = new PotionEffect(PotionEffectType.SPEED, getRandomTime(10, 20), 0);
				break;
			}
			case 2: {
				pe = new PotionEffect(PotionEffectType.SLOW, getRandomTime(10, 20), 0);
				break;
			}
			case 3: {
				pe = new PotionEffect(PotionEffectType.JUMP, getRandomTime(10, 20), 0);
				break;
			}
			case 4: {
				pe = new PotionEffect(PotionEffectType.LEVITATION, getRandomTime(10, 20), 0);
				break;
			}
			default: {
				pe = new PotionEffect(PotionEffectType.CONFUSION, getRandomTime(10, 20), 0);
				break;
			}
		}
		
		PotionMeta pm = (PotionMeta) is.getItemMeta();
		pm.addCustomEffect(pe, true);
		is.setItemMeta(pm);
		return is;
	}
	
	public int getRandomTime(int min, int max) {
		return (int) ((Math.random()*(max-min))+min)*20;
	}
	
	public boolean isCauldronValidLocation(Block b) {
		Location bLoc = b.getLocation();
		ConfigurationSection cs = Main.getInstance().getConfig().getConfigurationSection("murdermystery.arena.cauldrons");
		for(String s : cs.getKeys(false)) {
			
			double x = Main.getInstance().getConfig().getDouble("murdermystery.arena.cauldrons." + s + ".x");
			double y = Main.getInstance().getConfig().getDouble("murdermystery.arena.cauldrons." + s + ".y");
			double z = Main.getInstance().getConfig().getDouble("murdermystery.arena.cauldrons." + s + ".z");		
			String world = Main.getInstance().getConfig().getString("murdermystery.arena.cauldrons." + s + ".world");
			World w = Bukkit.getWorld(world);

			Location loc = new Location(w, x, y, z);
			
			if(loc.getBlockX() == bLoc.getBlockX()) {
				if(loc.getBlockY() == bLoc.getBlockY()) {
					if(loc.getBlockZ() == bLoc.getBlockZ()) {
						return true;
					}
				} 
			} 			
		}		
		return false;
	}

}
