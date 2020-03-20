package sk.xpress.murdermystery.handler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.graymadness.minigame_api.helper.item.ItemBuilder;

public class DetectiveBow {
	
	private ArmorStand as;
	private Item item;
	private Location loc;
	
	private final ItemStack droppedItem = new ItemBuilder(Material.BOW).setName(ComponentBuilder.text("Detective's bow").build()).addEnchant(Enchantment.ARROW_INFINITE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build();
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	public void spawn() {
		spawnItem();
		spawnArmorStand();
	}
	
	private void spawnItem() {
		item = loc.getWorld().dropItem(loc, droppedItem);
		item.setVelocity(new Vector(0,0,0));
	}
	
	private void spawnArmorStand() {
		as = (ArmorStand) loc.getWorld().spawnEntity(loc.subtract(0,1.4,0), EntityType.ARMOR_STAND);
		
		as.setGravity(false);
		as.setCustomNameVisible(true);
		as.setCustomName("§9§lDetective's bow");
		
		as.setInvulnerable(true);
		as.setBasePlate(false);
		as.setSmall(true);
		
		as.setVisible(false);
	}
	
	
	public void destroy() {
		if(item != null) item.remove();
		if(as != null) as.remove();
	}
}
