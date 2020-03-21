package sk.xpress.murdermystery.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.graymadness.minigame_api.api.API;
import net.graymadness.minigame_api.event.MinigameStartedEvent;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.graymadness.minigame_api.helper.item.ItemBuilder;
import sk.xpress.murdermystery.Main;
import sk.xpress.murdermystery.handler.GoldGenerator;
import sk.xpress.murdermystery.handler.Roles;

public class MinigameStart implements Listener {

	@EventHandler
	public void onMinigameStartedEvent(MinigameStartedEvent e) {
		// SAMOTNE JADRO HRY SPUSTIç....
		// ROZDELIT ROLE - INNOCENT/DETECTIVE/MURDER
		Main.getInstance().taskCancel("ToStartUp");
					
		goldSpawner();
					
		///////
		
		Player detective = API.getMinigame().getRoles().get(Roles.DETECTIVE.getName()).get(0);
		Player murder = API.getMinigame().getRoles().get(Roles.MURDER.getName()).get(0);
		//List<Player> innocents = API.getMinigame().getRoles().get(Roles.INNOCENT.getName());
		
		ItemStack sword = new ItemBuilder(Material.IRON_SWORD).setName(ComponentBuilder.text("ßcMurder's Sword").build()).build();
		murder.getInventory().setItem(1, sword);
		
		ItemStack bow = new ItemBuilder(Material.BOW).setName(ComponentBuilder.text("ß9Detective's bow").build()).addEnchant(Enchantment.ARROW_INFINITE, 1).setUnbreakable().addItemFlag(ItemFlag.HIDE_UNBREAKABLE).addItemFlag(ItemFlag.HIDE_ENCHANTS).build();
		ItemStack arrow = new ItemBuilder(Material.ARROW).build();
		
		detective.getInventory().setItem(8, bow);
		detective.getInventory().setItem(35, arrow);
	}

	private void goldSpawner() {
		int respawnTime = Main.getInstance().getConfig().getInt("murdermystery.options.gold-spawner-rate");
		String world = Main.getInstance().getConfig().getString("murdermystery.options.gold-spawner-world");
		World w = Bukkit.getWorld(world);
		
		if(w == null) throw new NullPointerException("Gold spawner world cannot be null or world doesnt exists.");
		
		BukkitTask task = new BukkitRunnable() {
			int goldSpawnedOnce = Main.getInstance().getConfig().getInt("murdermystery.options.max-gold-spawned-once");
			
			
			@Override
			public void run() {
				int goldSpawned = Main.getInstance().getGoldSpawned();
				if(goldSpawned < goldSpawnedOnce) {
					new GoldGenerator(w);
					Main.getInstance().setGoldSpawned(goldSpawned+1);
				}
			}
			
		}.runTaskTimer(Main.getInstance(), 0L, respawnTime*20L);
		Main.getTasks().put("GoldSpawner", task);
	}
	
}
