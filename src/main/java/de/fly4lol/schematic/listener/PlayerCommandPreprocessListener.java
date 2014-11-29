package de.fly4lol.schematic.listener;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.sk89q.worldedit.InvalidToolBindException;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.tools.DistanceWand;

import de.fly4lol.schematic.Main;


public class PlayerCommandPreprocessListener implements Listener{
	private Main plugin;
	
	public PlayerCommandPreprocessListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void playerCommandHandler(PlayerCommandPreprocessEvent event) {
		String message = event.getMessage().toLowerCase();
		message = message.replace("/worldedit:", "");
		if ((message.startsWith("//schem ") || message.startsWith("//schematic "))) {	
			if (message.contains("load")) {
				load(event);
			} else if (message.contains("save")) {
				save(event);
			} else if (message.contains("list")) {
				list(event);
			} 
		}
		
	}
	
	
	private void load(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		if(!player.hasPermission("schematic.load.other")){
			event.setCancelled(true);
			String message = event.getMessage();
			String[] args = message.split(" ");	
		if(player.hasPermission("schematic.load.own")){
			if(args.length == 3){
				String schem = player.getName() + "/" + args[2];
				
				player.performCommand("/schematic load " + schem);
			} else if(args.length == 4 ){
				if(player.hasPermission("schematic.load.path." + args[2])){
					String schem = player.getName() + "/" + args[2] + "/" + args[3];
					
					player.performCommand("/schematic load " + schem);
				}
			} else {
				player.sendMessage("Nutze //schematic load <schematic>");
			}
			
		} else {
			player.sendMessage("§4Du Hast keine Berechtigung um dies zu tuhen!");
		}
		
		}
	}
	
	private void save(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		if(!player.hasPermission("schematic.save.other")){
			if(player.hasPermission("schematic.save.own")){
				event.setCancelled(true);
				
				String message = event.getMessage();
				String[] args = message.split(" ");	
				
				String schem = player.getName() + "/" + args[2];
				
				player.performCommand("/schematic save " + schem);	
			}
		}
	}
	
	private void list(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		if(!player.hasPermission("schematic.list.other")){
			event.setCancelled(true);
			
			String message = event.getMessage();
			String[] args = message.split(" ");	
		if(player.hasPermission("schematic.list.own")){
			if(args.length == 2){
				WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
				LocalConfiguration config = we.getWorldEdit().getConfiguration();
				File saveDir = new File(config.saveDir, player.getName());
					
				this.listSchematics( saveDir, player);
			} else if(args.length == 3){
				if(player.hasPermission("schematic.list.path." + args[2])){
					WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
					LocalConfiguration config = we.getWorldEdit().getConfiguration();
					File saveDir = new File(config.saveDir, player.getName() + args[2]);
					
					this.listSchematics( saveDir, player);
				}
			} else {
				player.sendMessage("Nutze //schematic list");
			}
	
		
		} else {
			player.sendMessage("§4Du Hast keine Berechtigung um dies zu tuhen!");
		}
		}
	}
	
	private void listSchematics(File path, Player player){
		File saveDir = null;
		saveDir = path;
		saveDir.mkdirs();
		File[] files = saveDir.listFiles();
        player.sendMessage(ChatColor.AQUA+"=== Schematics ===");
        for (File file : files)
        {
            if (file.getName().toLowerCase().contains("schematic") && !file.isDirectory()) {
               player.sendMessage(ChatColor.BLUE.toString()+file.getName());
            }
        }
	}
}
