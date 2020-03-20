package sk.xpress.murdermystery.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.graymadness.minigame_api.helper.item.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import sk.xpress.murdermystery.ActionBar;
import sk.xpress.murdermystery.Cooldown;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.Chat;
import sk.xpress.murdermystery.handler.Roles;

public class PlayerPickupItem implements Listener {
	
	@EventHandler
	public void onPlayerPickupItem(EntityPickupItemEvent e) {

		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			ItemStack is = e.getItem().getItemStack();
				
			switch(is.getType()) {
				case GOLD_INGOT: {
					e.setCancelled(true);
					//e.setCancelled(true);
					
					//if(Main.isPlayerDetective(p)) return;
					if(Main.isPlayerInnocent(p) || Main.isPlayerDetective(p)) {
						
						
						
						
						
						int amountGolds = 0;
						if(p.getInventory().getItem(4) != null) amountGolds = p.getInventory().getItem(4).getAmount();
						
						Main.getInstance().setGoldSpawned(Main.getInstance().getGoldSpawned()-1);
						
						if(amountGolds >= 64) {
							ActionBar.sendActionBar(p, ComponentBuilder.text("�cU� nem��e� zobra� �al�� gold ingot!").color(ChatColor.RED).build());
							e.setCancelled(true);
						}else {
							ItemStack gold_ingot = new ItemBuilder(Material.GOLD_INGOT).setAmount(amountGolds+1).build();
							p.getInventory().setItem(4, gold_ingot);
							
							e.getItem().teleport(
									new Location(p.getLocation().getWorld(), 
									p.getLocation().getBlockX(), 
									-1, 
									p.getLocation().getBlockZ()));
							
							p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 6f, 6f);
						}

					
						if(Main.isPlayerDetective(p)) return; // aby detekt�v nedostal druh� luk...
						
						if(p.getInventory().getItem(4) != null && p.getInventory().getItem(4).getAmount() >= 10) { // na pridanie luku hr��ovi...
							p.getInventory().setItem(8, new ItemBuilder(Material.BOW).build());
							
							if(p.getInventory().getItem(7) == null) p.getInventory().setItem(7, new ItemBuilder(Material.ARROW).setAmount(1).build());
							else p.getInventory().addItem(new ItemBuilder(Material.ARROW).setAmount(1).build());
							
							p.getInventory().getItem(4).setAmount(p.getInventory().getItem(4).getAmount()-10);
						}
						
						return;
					}
					
					e.setCancelled(true);		
					break;
				}
				
				case BOW: {
					if(Main.isPlayerInnocent(p)) { //return; // DOROBIT, �E SA STANE DETECTIVOM
						
						if(Cooldown.hasCooldown("DetectiveDropSword"+p.getName())) {
							e.setCancelled(true);
							return;
						}
						
						Main.getDetectiveBow().destroy();
						
						API.getMinigame().getRoles().get(Roles.DETECTIVE.getName()).add(p);
						Chat.print("Hr�� " + p.getName() + " sa stal detektivom!");
						
						ItemStack bow = new ItemBuilder(Material.BOW).setName(ComponentBuilder.text("Detective's bow").build()).addEnchant(Enchantment.ARROW_INFINITE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build();
						ItemStack arrow = new ItemBuilder(Material.ARROW).build();
						
						p.getInventory().setItem(8, bow);
						p.getInventory().setItem(35, arrow);
					}
					
					e.setCancelled(true);
					break;
				}
				
				case ARROW: { // aby ne�li zbiera� ��py zo zeme..
					e.setCancelled(true);
					break;
				}
				default: return;
			}
		}
	}	
	
}
