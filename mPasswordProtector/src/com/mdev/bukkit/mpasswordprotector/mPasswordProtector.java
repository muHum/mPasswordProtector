package com.mdev.bukkit.mpasswordprotector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.config.Configuration;


/**
* Password protection plugin for Bukkit
*
* @author muHum
*/
public class mPasswordProtector extends JavaPlugin {
    private final mppPlayerListener playerListener = new mppPlayerListener(this);
    private final mppBlockListener blockListener = new mppBlockListener(this);
    
    // config related
    static String mainDirectory = "plugins/mPasswordProtector"; 	
	static File cfile = new File(mainDirectory + File.separator + "config.yml"); 
	static Configuration conf = new Configuration(cfile);

    public HashMap<Player, Integer> unauthorisedPlayers = new HashMap<Player, Integer>();
    public ArrayList<String> whitelistPlayerNames;// = new ArrayList<String>();
    
    static String password;
    
    public void onDisable() {
    	PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( "[INFO] " + pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!" );
    }

    public void onEnable() {     	    	
    	loadConfig();
    	
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        
        // player related events
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.High, this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_DROP_ITEM, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Normal, this);
        
        // block related events
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);

        // Register our commands
        getCommand("login").setExecutor(new LoginCommand(this));
        getCommand("setpwd").setExecutor(new SetPwdCommand(this));
        getCommand("whitelist").setExecutor(new WhitelistCommand(this));
        
        PluginDescriptionFile pdfFile = this.getDescription();  
        System.out.println( "[mPasswordProtector] " + " v" + pdfFile.getVersion() + " enabled!" );
    }

    private void loadConfig() {
    	new File(mainDirectory).mkdir(); //makes the Zones directory/folder in the plugins directory
   	 
		if(!cfile.exists()){ 
			try { 
				cfile.createNewFile(); 				
			} catch (IOException ex) { 
				ex.printStackTrace(); 
			}
			conf.setProperty("password", "asdfasdf");
			conf.setProperty("whitelist", "");
			conf.setProperty("kickorban", "k");
			conf.setProperty("kobafter", "3");
			conf.setProperty("allowMovement", "false");
			conf.save();
		} else {
			conf.load();
		}
		
		if(!loadPassword()){
			System.out.println( "[mPasswordProtector] could not load password!!" );
		} 
		
		if(!loadWhitelist()){
			System.out.println( "[mPasswordProtector] could not load whitelist!!" );
		} 
		
	}

	private boolean loadWhitelist() {
		String wl = conf.getProperty("whitelist").toString();
		this.whitelistPlayerNames = new ArrayList<String>();			   
		
		for (String name : wl.split(";")){
			whitelistPlayerNames.add(name);
		}
		
    	return true;
	}
	
	public boolean setWhitelist() {
		String wl = "";
		
		for(String name : this.whitelistPlayerNames){
			wl += name + ";";
		}
		
		conf.setProperty("whitelist", wl);
		conf.save();
		
		loadWhitelist();
    	return true;
	}
	
	public Configuration getConfig(){
		return conf;
	}

	public String getPwd(){
    	// TODO Read password from encrypted file
    	return password;
    }
    
    boolean loadPassword(){   	    	
	    password = conf.getProperty("password").toString();	   		
    	return true;
    }
    
    boolean setPassword(String pw){
    	conf.setProperty("password", pw);
    	conf.save();
    	
    	loadPassword();
    	return true;    	
    }    
}