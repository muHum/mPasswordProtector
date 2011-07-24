package com.mdev.bukkit.mpasswordprotector;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SetPwdCommand implements CommandExecutor {

private final mPasswordProtector plugin;
	
	public SetPwdCommand(mPasswordProtector plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(args.length != 2)
			return false;
		
		if(sender.isOp()){
			if(plugin.getPwd().equals(args[0])){
				if(plugin.setPassword(args[1])){
					sender.sendMessage(ChatColor.GREEN + "Password succsessfully changed!"); 
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "Password could not be changed!"); 
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Wrong password, bastard! " + args[0] + " " + args[1]);
				return true;
			}
		} else if(sender instanceof Player){
			sender.sendMessage(ChatColor.RED + "Buhh... Only for OPs, bastard!");
			return true;
		}
		
		return false;
	}

}
