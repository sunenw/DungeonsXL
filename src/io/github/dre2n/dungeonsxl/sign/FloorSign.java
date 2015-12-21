package io.github.dre2n.dungeonsxl.sign;

import io.github.dre2n.dungeonsxl.DungeonsXL;
import io.github.dre2n.dungeonsxl.dungeon.game.GameWorld;
import io.github.dre2n.dungeonsxl.player.DPlayer;
import io.github.dre2n.dungeonsxl.trigger.InteractTrigger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class FloorSign extends DSign {
	
	public static String name = "Floor";
	public String buildPermissions = "dxl.sign.floor";
	public boolean onDungeonInit = false;
	
	private String floor;
	
	public FloorSign(Sign sign, GameWorld gWorld) {
		super(sign, gWorld);
	}
	
	@Override
	public boolean check() {
		return true;
	}
	
	@Override
	public void onInit() {
		String[] lines = getSign().getLines();
		if ( !lines[1].equals("")) {
			floor = lines[1];
		}
		
		if (getTriggers().isEmpty()) {
			InteractTrigger trigger = InteractTrigger.getOrCreate(0, getSign().getBlock(), getGWorld());
			if (trigger != null) {
				trigger.addListener(this);
				addTrigger(trigger);
			}
			getSign().setLine(0, ChatColor.DARK_BLUE + "############");
			getSign().setLine(1, ChatColor.DARK_GREEN + "ENTER");
			if (floor == null) {
				getSign().setLine(2, ChatColor.DARK_GREEN + "NEXT FLOOR");
			} else {
				getSign().setLine(2, ChatColor.DARK_GREEN + floor.replaceAll("_", " "));
			}
			getSign().setLine(3, ChatColor.DARK_BLUE + "############");
			getSign().update();
		} else {
			getSign().getBlock().setType(Material.AIR);
		}
	}
	
	@Override
	public boolean onPlayerTrigger(Player player) {
		DPlayer dplayer = DPlayer.get(player);
		if (dplayer != null) {
			if ( !dplayer.isFinished) {
				dplayer.finishFloor(floor);
			}
		}
		return true;
	}
	
	@Override
	public void onTrigger() {
		for (DPlayer dplayer : DungeonsXL.getPlugin().getDPlayers()) {
			dplayer.finish();
		}
	}
	
	@Override
	public String getPermissions() {
		return buildPermissions;
	}
	
	@Override
	public boolean isOnDungeonInit() {
		return onDungeonInit;
	}
	
}
