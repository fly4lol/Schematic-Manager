package de.fly4lol.schematic;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.fly4lol.schematic.listener.PlayerCommandPreprocessListener;

public class Main extends JavaPlugin {

	@Override
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessListener(this), this);
	}
}
