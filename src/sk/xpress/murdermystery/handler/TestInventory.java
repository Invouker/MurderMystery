package sk.xpress.murdermystery.handler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.graymadness.minigame_api.helper.item.ItemBuilder;
import net.graymadness.minigame_api.menu.ItemMenu;

public class TestInventory extends ItemMenu {

	public TestInventory() {
		super(Type.Chest3, ComponentBuilder.text("test").bold(true).build());
	}

	@Override
	protected void onClick( Player p, int item,  ItemStack is,  ItemStack cursorIs, InventoryClickEvent e) {
		Chat.print("CLICK" + is.getItemMeta().getDisplayName());
		e.setCancelled(true);
	}

	@Override
	protected void onClose(Player p) {
		Chat.print("Close");
	}

	@Override
	protected void onOpen(Player p) {
		this.getInventory().addItem(new ItemBuilder(Material.ACACIA_BOAT).setName(ComponentBuilder.text("Ahoj").build()).build());
		Chat.print("Open");		
	}
}
