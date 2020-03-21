package sk.xpress.murdermystery.handler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.graymadness.minigame_api.helper.ChatInfo;
import net.graymadness.minigame_api.helper.ComponentBuilder;
import sk.xpress.murdermystery.DefaultFontInfo;

public class Chat {
	private final static String cPrefix = "[MurderMystery] §f";
	
	public static void print(String msg) {
		Bukkit.getConsoleSender().sendMessage(cPrefix + msg);
	}
	
	public static String getCentredMessage(String message) {
	    if(message == null || message.equals("")) return "";
	    
	    message = ChatColor.translateAlternateColorCodes('&', message);
	 
	    int messagePxSize = 0;
	    boolean previousCode = false;
	    boolean isBold = false;
	 
	    for(char c : message.toCharArray()){
	        if(c == '§'){
	            previousCode = true;
	        }else if(previousCode){
	            previousCode = false;
	            isBold = c == 'l' || c == 'L';
	        }else{
	            DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
	            messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
	            messagePxSize++;
	        }
	    }
	    int CENTER_PX = 154;
	    int halvedMessageSize = messagePxSize / 2;
	    int toCompensate = CENTER_PX - halvedMessageSize;
	    int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
	    int compensated = 0;
	    StringBuilder sb = new StringBuilder();
	    while(compensated < toCompensate){
	        sb.append(" ");
	        compensated += spaceLength;
	    }
	    return (sb.toString() + message);
	}
	
	
	public static void sendInnocentWinMessage(String detective, String murderer, String hero) {
		for(Player p: Bukkit.getOnlinePlayers()) {
			p.sendMessage("");
			p.sendMessage("§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			p.sendMessage("");
			ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§f§lMURDER MYSTERY")).build());
			p.sendMessage("");
			ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§e§lWinner: §7INNOCENT")).build());
			p.sendMessage("");
			if(!detective.equals("")) ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§9§lDetective: §7" + detective)).build());
			ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§c§lMurderer: §7" + murderer)).build());
			if(!hero.equals("")) ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§6§lHero: §7" + hero)).build());
			p.sendMessage("");
			
			p.sendMessage("§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		}
	}
	
	public static void sendMurderWinMessage(String detective, String murderer) {
		for(Player p: Bukkit.getOnlinePlayers()) {
			p.sendMessage("");
			p.sendMessage("§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			p.sendMessage("");
			ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§f§lMURDER MYSTERY")).build());
			p.sendMessage("");
			ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§e§lWinner: §7MURDER")).build());
			p.sendMessage("");
			if(!detective.equals("")) ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§9§lDetective: §7" + detective)).build());
			ChatInfo.NULL.send(p, ComponentBuilder.text(Chat.getCentredMessage("§c§lMurderer: §7" + murderer)).build());
			p.sendMessage("");
			
			p.sendMessage("§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		}
	}
}
