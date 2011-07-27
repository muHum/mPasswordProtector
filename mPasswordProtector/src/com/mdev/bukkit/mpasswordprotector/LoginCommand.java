package com.mdev.bukkit.mpasswordprotector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class LoginCommand implements CommandExecutor {

	private final mPasswordProtector plugin;
	
	public LoginCommand(mPasswordProtector plugin){
		this.plugin = plugin;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {  
		if(args.length != 1)
			return false;
		
		if(sender instanceof Player){
			if(cmd.getName().equals("login")){
				Player player = (Player) sender;		        
		        if(plugin.unauthorisedPlayers.containsKey(player)) { 
			        String password = args[0];
	
	                if (password.equals(plugin.getPwd())) {
	                    player.sendMessage(ChatColor.GREEN + "Server password accepted, you're now allowed to play");
	                    plugin.unauthorisedPlayers.remove(player);
	                    
	                    return true;
	                } else {
	                	int old = plugin.unauthorisedPlayers.get(player);
	                	String kb = plugin.getConfig().getProperty("kickorban").toString();
	                	
	                	int kob = (Integer.parseInt(plugin.getConfig().getProperty("kobafter").toString()) - old);
	                	
	                	if(kob > 0){
	                		player.sendMessage(ChatColor.RED + "Server password incorrect! " + kob + " attempts left...");
	                		old++;
	                		plugin.unauthorisedPlayers.put(player, old);
	                	} else if(kob <= 0) {
	                		if(kb.equals("k")){
	                			player.sendMessage(ChatColor.RED + "Server password incorrect! You're will be kicked by now...");
	                			player.kickPlayer("Too many wrong attempts...");
	                		} else if(kb.equals("b")){
	                			player.sendMessage(ChatColor.RED + "Server password incorrect! You're will be banned by now...");
	                			player.kickPlayer("And banned..."); //TODO write player into ban.txt
	                		}
	                	}                	
	                	
	                    return true;
	                }		             
		        } else {
		        	player.sendMessage(ChatColor.GREEN + "You're already logged in!");
		        	return true;
		        }
			} 
		}
		
		return false;
	}
}
