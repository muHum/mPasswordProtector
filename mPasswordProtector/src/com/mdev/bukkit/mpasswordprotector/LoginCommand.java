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
		        if(plugin.unauthorisedPlayers.contains(player)) { 
			        String password = args[0];
	
	                if (password.equals(plugin.getPwd())) {
	                    player.sendMessage(ChatColor.GREEN + "Server password accepted, you're now allowed to play");
	                    plugin.unauthorisedPlayers.remove(player);
	                    
	                    return true;
	                } else {
	                    player.sendMessage(ChatColor.RED + "Server password incorrect, now fuck off!");
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
