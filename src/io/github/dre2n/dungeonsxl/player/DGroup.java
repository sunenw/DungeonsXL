package io.github.dre2n.dungeonsxl.player;

import io.github.dre2n.dungeonsxl.DungeonsXL;
import io.github.dre2n.dungeonsxl.dungeon.Dungeon;
import io.github.dre2n.dungeonsxl.dungeon.WorldConfig;
import io.github.dre2n.dungeonsxl.dungeon.game.GameWorld;
import io.github.dre2n.dungeonsxl.global.GroupSign;
import io.github.dre2n.dungeonsxl.util.MessageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.Player;

public class DGroup {
	
	static DungeonsXL plugin = DungeonsXL.getPlugin();
	
	private CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<Player>();
	private String dungeonName;
	private String mapName;
	private List<String> unplayedFloors = new ArrayList<String>();
	private GameWorld gWorld;
	private boolean playing;
	private int floorCount;
	
	public DGroup(Player player, String identifier, boolean multiFloor) {
		plugin.getDGroups().add(this);
		
		this.players.add(player);
		if (multiFloor) {
			this.dungeonName = identifier;
			this.mapName = plugin.getDungeons().getDungeon(dungeonName).getConfig().getStartFloor();
			this.unplayedFloors = plugin.getDungeons().getDungeon(dungeonName).getConfig().getFloors();
		} else {
			this.mapName = identifier;
		}
		this.playing = false;
		this.floorCount = 0;
	}
	
	// Getters and setters
	
	/**
	 * @return the players
	 */
	public CopyOnWriteArrayList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * @param player
	 * the player to add
	 */
	public void addPlayer(Player player) {
		// Send message
		for (Player groupPlayer : getPlayers()) {
			MessageUtil.sendMessage(groupPlayer, DungeonsXL.getPlugin().getDMessages().get("Player_JoinGroup", player.getName()));
		}
		
		// Add player
		getPlayers().add(player);
	}
	
	/**
	 * @param player
	 * the player to remove
	 */
	public void removePlayer(Player player) {
		getPlayers().remove(player);
		GroupSign.updatePerGroup(this);
		
		// Send message
		for (Player groupPlayer : getPlayers()) {
			MessageUtil.sendMessage(groupPlayer, DungeonsXL.getPlugin().getDMessages().get("Player_LeftGroup", player.getName()));
		}
		
		// Check group
		if (isEmpty()) {
			remove();
		}
	}
	
	/**
	 * @return the gWorld
	 */
	public GameWorld getGWorld() {
		return gWorld;
	}
	
	/**
	 * @param gWorld
	 * the gWorld to set
	 */
	public void setGWorld(GameWorld gWorld) {
		this.gWorld = gWorld;
	}
	
	/**
	 * @return the dungeonName
	 */
	public String getDungeonName() {
		return dungeonName;
	}
	
	/**
	 * @param dungeonName
	 * the dungeonName to set
	 */
	public void setDungeonName(String dungeonName) {
		this.dungeonName = dungeonName;
	}
	
	/**
	 * @return the dungeon (saved by name only)
	 */
	public Dungeon getDungeon() {
		return plugin.getDungeons().getDungeon(dungeonName);
	}
	
	/**
	 * @param dungeon
	 * the dungeon to set (saved by name only)
	 */
	public void setDungeon(Dungeon dungeon) {
		dungeonName = dungeon.getName();
	}
	
	/**
	 * @return if the group is playing
	 */
	public String getMapName() {
		return mapName;
	}
	
	/**
	 * @param name
	 * the name to set
	 */
	public void setMapName(String name) {
		this.mapName = name;
	}
	
	/**
	 * @return the unplayedFloors
	 */
	public List<String> getUnplayedFloors() {
		return unplayedFloors;
	}
	
	/**
	 * @param unplayedFloor
	 * the unplayedFloor to add
	 */
	public void addUnplayedFloor(String unplayedFloor) {
		unplayedFloors.add(unplayedFloor);
	}
	
	/**
	 * @param unplayedFloor
	 * the unplayedFloor to add
	 */
	public void removeUnplayedFloor(String unplayedFloor) {
		if (getDungeon().getConfig().isRemoveWhenPlayed()) {
			unplayedFloors.remove(unplayedFloor);
		}
	}
	
	/**
	 * @return if the group is playing
	 */
	public boolean isPlaying() {
		return playing;
	}
	
	/**
	 * @param playing
	 * set if the group is playing
	 */
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	
	/**
	 * @return the floorCount
	 */
	public int getFloorCount() {
		return floorCount;
	}
	
	/**
	 * @param floorCount
	 * the floorCount to set
	 */
	public void setFloorCount(int floorCount) {
		this.floorCount = floorCount;
	}
	
	/**
	 * @return whether there are players in the group
	 */
	public boolean isEmpty() {
		return players.isEmpty();
	}
	
	public void remove() {
		plugin.getDGroups().remove(this);
		GroupSign.updatePerGroup(this);
	}
	
	public void startGame() {
		playing = true;
		gWorld.startGame();
		floorCount++;
		
		double fee;
		File file = new File(plugin.getDataFolder() + "/maps/" + mapName + "/config.yml");
		fee = new WorldConfig(file).getFee();
		
		for (Player player : getPlayers()) {
			DPlayer dplayer = DPlayer.get(player);
			dplayer.respawn();
			if ( !DungeonsXL.getPlugin().getMainConfig().enableEconomy()) {
				continue;
			}
			
			if (plugin.economy != null) {
				DungeonsXL.getPlugin().economy.withdrawPlayer(player, fee);
			}
		}
		
		GroupSign.updatePerGroup(this);
	}
	
	// Statics
	
	public static DGroup get(Player player) {
		for (DGroup dgroup : plugin.getDGroups()) {
			if (dgroup.getPlayers().contains(player)) {
				return dgroup;
			}
		}
		return null;
	}
	
	public static DGroup get(GameWorld gWorld) {
		for (DGroup dgroup : plugin.getDGroups()) {
			if (dgroup.getGWorld() == gWorld) {
				return dgroup;
			}
		}
		return null;
	}
	
	public static void leaveGroup(Player player) {
		for (DGroup dgroup : plugin.getDGroups()) {
			if (dgroup.getPlayers().contains(player)) {
				dgroup.getPlayers().remove(player);
			}
		}
	}
	
}