package sk.xpress.murdermystery;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import sk.xpress.murdermystery.handler.Roles;

public class TeamManager {

	private static Team detective;
	private static Team innocent;
	
	public static void registerTeamsToPlayer(Player p) {
		Scoreboard sb;
		if(p.getScoreboard() == null) sb = Bukkit.getScoreboardManager().getNewScoreboard();
		else sb = p.getScoreboard();
		
		if(sb.getTeam(Roles.INNOCENT.getName()) == null) {
			
			innocent = sb.registerNewTeam(Roles.INNOCENT.getName());
			innocent.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
			innocent.setAllowFriendlyFire(true);
		}

		if(sb.getTeam(Roles.DETECTIVE.getName()) == null) {
			detective = sb.registerNewTeam(Roles.DETECTIVE.getName());
			detective.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
			//detective.setColor(ChatColor.BLUE);
		}
		
	}
	
	public static void addTeamToPlayer(Player p, Roles role) {
		Scoreboard sb = p.getScoreboard();
		if(p.getScoreboard() != null) {
			Team team = sb.getTeam(role.getName());
			if(team != null && !team.hasEntry(p.getName())) {
				team.addEntry(p.getName());
			}
		}
	}
	
	public static void removeTeam(Player p) {
		Scoreboard sb = p.getScoreboard();
		if(p.getScoreboard() != null) {
			
			Team teamInnocent = sb.getTeam(Roles.INNOCENT.getName());
			if(teamInnocent != null && teamInnocent.hasEntry(p.getName())) {
				teamInnocent.removeEntry(p.getName());
			}
			
			Team teamDetective = sb.getTeam(Roles.DETECTIVE.getName());
			if(teamDetective != null && teamDetective.hasEntry(p.getName())) {
				teamDetective.removeEntry(p.getName());
			}
		}
	}
}
