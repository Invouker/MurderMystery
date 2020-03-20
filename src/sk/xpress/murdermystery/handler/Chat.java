package sk.xpress.murdermystery.handler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import sk.xpress.murdermystery.DefaultFontInfo;

public class Chat {
	private final static String cPrefix = "[MurderMystery] �f";
	
	public static void print(String msg) {
		Bukkit.getConsoleSender().sendMessage(cPrefix + msg);
	}
	
	public static void sendCentredMessage(Player player, String message) {
	    if(message == null || message.equals("")) {
	        player.sendMessage("");
	        return;
	    }
	    message = ChatColor.translateAlternateColorCodes('&', message);
	 
	    int messagePxSize = 0;
	    boolean previousCode = false;
	    boolean isBold = false;
	 
	    for(char c : message.toCharArray()){
	        if(c == '�'){
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
	    player.sendMessage(sb.toString() + message);
	}
}
