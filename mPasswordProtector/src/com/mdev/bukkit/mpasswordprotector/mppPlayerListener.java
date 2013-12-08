package com.mdev.bukkit.mpasswordprotector;

//import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;

/**
 * Handle events for all Player related events
 * @author muHum
 */
public class mppPlayerListener extends PlayerListener {
    private final mPasswordProtector plugin;

    public mppPlayerListener(mPasswordProtector instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // TODO add specified location for unauthorized players but for first testings this will do it 
        if(!plugin.whitelistPlayerNames.contains(player.getDisplayName())) {    
        	plugin.unauthorisedPlayers.put(player, 0);
        	sendPwdReqMsg(player);
        } else {
        	player.sendMessage(ChatColor.GREEN + "You're on the whitelist.");
        }
    }
    
    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        String k = plugin.getConfig().getProperty("allowMovement").toString();
        
        if (k.equalsIgnoreCase("true")) {
            return;
        }
        
        Player player = event.getPlayer();
        if (plugin.unauthorisedPlayers.containsKey(player)) {
                event.setCancelled(true);
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        if (plugin.unauthorisedPlayers.containsKey(player)) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        if (plugin.unauthorisedPlayers.containsKey(player)) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    	Player player = event.getPlayer();
        String message = event.getMessage();
        
        if (plugin.unauthorisedPlayers.containsKey(player) && !message.startsWith("/login")) {
            sendPwdReqMsg(player);            
            event.setCancelled(true);       
        }
    }
    
    public void sendPwdReqMsg(Player player) {
        player.sendMessage(ChatColor.YELLOW + "Login with " + ChatColor.GREEN + "/login " + ChatColor.RED + " <password>" + ChatColor.YELLOW + " to play");
    }
}
