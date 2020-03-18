package sk.xpress.murdermystery;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.graymadness.minigame_api.api.IMinigame;
import net.graymadness.minigame_api.api.MinigameState;
import sk.xpress.murdermystery.handler.Chat;

public class MurderMystery implements IMinigame {
 
	@Override
	public boolean canJoinDuringWarmup() {
		return false;
	}

	@Override
	public @NotNull String getCodename() {
		return "murdermystery";
	}

	@Override
	public int getMaxPlayers() {
		return 16;
	}

	@Override
	public int getMinPlayers() {
		return 8;
	}

	@Override
	public @NotNull Map<String, List<Player>> getRoles() {
		return Main.getRoles();
	}

	@Override
	public @NotNull MinigameState getState() {
		return Main.getState();
	}

	@Override
	public void start() {
		
		Chat.print("START");
	}

	@Override
	public void stop() {
		Chat.print("STOP");
	}
		
}
