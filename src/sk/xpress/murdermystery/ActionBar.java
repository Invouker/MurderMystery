package sk.xpress.murdermystery;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.ChatType;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public final class ActionBar {

	public static void sendActionBar(Player p, BaseComponent cmp) {
		ProtocolManager pm = ProtocolLibrary.getProtocolManager();
		PacketContainer chatPacket = pm.createPacket(PacketType.Play.Server.CHAT);
		
		chatPacket.getChatComponents().write(0, fromBaseComponent(cmp));
		chatPacket.getChatTypes().write(0, ChatType.GAME_INFO);
		
		try {
            pm.sendServerPacket(p, chatPacket);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
	}
	
	private static WrappedChatComponent fromBaseComponent(BaseComponent... components) {
		return WrappedChatComponent.fromJson(ComponentSerializer.toString(components));
	}
}
