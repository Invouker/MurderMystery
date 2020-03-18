package sk.xpress.murdermystery.handler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.graymadness.minigame_api.helper.ComponentBuilder;
import net.graymadness.minigame_api.helper.item.ItemBuilder;
import net.graymadness.minigame_api.menu.ItemMenu;

public class TestInventory extends ItemMenu {

	public TestInventory() {
		super(Type.Chest3, ComponentBuilder.text("test").bold(true).build());
	}

	@Override
	protected void onClick(@NotNull Player p, int item, @Nullable ItemStack is, @Nullable ItemStack cursorIs,@NotNull InventoryClickEvent e) {
		Chat.print("CLICK" + is.getItemMeta().getDisplayName());
		e.setCancelled(true);
	}

	@Override
	protected void onClose(@NotNull Player p) {
		Chat.print("Close");
	}

	@Override
	protected void onOpen(@NotNull Player p) {
		this.getInventory().addItem(new ItemBuilder(Material.ACACIA_BOAT).setName(ComponentBuilder.text("Ahoj").build()).build());
		Chat.print("Open");		
	}
}
