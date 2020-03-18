package sk.xpress.murdermystery.handler;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.graymadness.minigame_api.helper.item.ItemBuilder;
import sk.xpress.murdermystery.Main;

public class GoldGenerator {
	private World w;
	private static final ItemStack item = new ItemBuilder(Material.GOLD_INGOT).build();

	public GoldGenerator(World w) {
		this.w = w;
		
		generateLocation();
	}
	
	private void generateLocation() {
		new BukkitRunnable() {
			@Override
			public void run() {
				Location generatedLoc = new Location(w, 
						randomMinMax((int)Main.getArenaMin().getX(), (int)Main.getArenaMax().getX()), 
						randomMinMax((int)Main.getArenaMin().getY(), (int)Main.getArenaMax().getY()), 
						randomMinMax((int)Main.getArenaMin().getZ(), (int)Main.getArenaMax().getZ()));
				
				if(w.getBlockAt(generatedLoc).getType() == Material.AIR) {
					
					Location underItem = new Location(w, generatedLoc.getX(), generatedLoc.getY()-1, generatedLoc.getZ());
					if(w.getBlockAt(underItem).getType() != Material.AIR) {
						
						dropAt(generatedLoc, item);
						
						this.cancel();
					}
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0L, 1L);
	}
	
	private void dropAt(Location loc, ItemStack is) {
		w.dropItem(loc, is);
	}
	
	private int randomMinMax(int min, int max) {
		Random rand = new Random();
		return (rand.nextInt(max - min) + min);
	}
}
