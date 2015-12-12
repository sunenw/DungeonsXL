package io.github.dre2n.dungeonsxl.sign;

import io.github.dre2n.dungeonsxl.dungeon.game.GameChest;
import io.github.dre2n.dungeonsxl.dungeon.game.GameWorld;

import org.bukkit.Material;
import org.bukkit.block.Sign;

public class ChestSign extends DSign {
	
	public static String name = "Chest";
	public String buildPermissions = "dxl.sign.chest";
	public boolean onDungeonInit = false;
	
	// Variables
	private double moneyReward;
	
	public ChestSign(Sign sign, GameWorld gWorld) {
		super(sign, gWorld);
	}
	
	@Override
	public boolean check() {
		String lines[] = getSign().getLines();
		if (lines[1].equals("")) {
			return false;
		}
		return true;
	}
	
	@Override
	public void onInit() {
		String lines[] = getSign().getLines();
		if ( !lines[1].equals("")) {
			moneyReward = Double.parseDouble(lines[1]);
		}
		for (int i = -1; i <= 1; i++) {
			if (getSign().getBlock().getRelative(i, 0, 0).getType() == Material.CHEST) {
				new GameChest(getSign().getBlock().getRelative(i, 0, 0), getGWorld(), moneyReward);
			}
			if (getSign().getBlock().getRelative(0, 0, i).getType() == Material.CHEST) {
				new GameChest(getSign().getBlock().getRelative(0, 0, i), getGWorld(), moneyReward);
			}
			if (getSign().getBlock().getRelative(0, i, 0).getType() == Material.CHEST) {
				new GameChest(getSign().getBlock().getRelative(0, i, 0), getGWorld(), moneyReward);
			}
		}
		
		getSign().getBlock().setType(Material.AIR);
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
