package com.mdev.bukkit.mpasswordprotector;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;


/**
 * mPasswordProtector block listener
 * @author muHum
 */
public class mppBlockListener extends BlockListener {
    private final mPasswordProtector plugin;

    public mppBlockListener(mPasswordProtector plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) 
            return;        
        
        Player player = event.getPlayer();
        if (plugin.unauthorisedPlayers.containsKey(player)) {
            event.setBuild(false);
            event.setCancelled(true);
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) 
            return;        

        Player player = event.getPlayer();
        if (plugin.unauthorisedPlayers.containsKey(player)) {
            event.setCancelled(true);
        }
    }
}
