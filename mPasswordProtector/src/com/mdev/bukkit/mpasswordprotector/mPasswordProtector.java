package com.mdev.bukkit.mpasswordprotector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;


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
	static File configfile = new File(mainDirectory + File.separator + "protector.dat"); 
	static Properties prop = new Properties(); 

    public ArrayList<Player> unauthorisedPlayers = new ArrayList<Player>();
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
   	 
		if(!configfile.exists()){ 
			try { 
				configfile.createNewFile(); 
				FileOutputStream out = new FileOutputStream(configfile); 
				prop.put("password", "asdfasdf");
				prop.put("whitelist", "");
				prop.store(out, "Do NEVER edit this config!"); 
				out.flush();  
				out.close(); 
			} catch (IOException ex) { 
				ex.printStackTrace(); 
			}
		} 
		
		if(!loadPassword()){
			System.out.println( "[mPasswordProtector] could not load password!!" );
		} 
		
		if(!loadWhitelist()){
			System.out.println( "[mPasswordProtector] could not load whitelist!!" );
		} 
		
	}

	private boolean loadWhitelist() {
		FileInputStream in;
		String wl;
		this.whitelistPlayerNames = new ArrayList<String>();
		
		try {
			in = new FileInputStream(configfile);
			prop.load(in);
	    	wl = prop.getProperty("whitelist");
	    	in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 	   
		
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
		
		try {  
			FileOutputStream out = new FileOutputStream(configfile); 
			prop.setProperty("whitelist", wl); 
			prop.store(out, "Do NEVER edit this config!");
			out.flush();  
			out.close(); 
		} catch (IOException ex) { 
			ex.printStackTrace(); 
			return false;
		}
		
		loadWhitelist();
    	return true;
	}

	public String getPwd(){
    	// TODO Read password from encrypted file
    	return password;
    }
    
    boolean loadPassword(){    	
    	FileInputStream in;
		try {
			in = new FileInputStream(configfile);
			prop.load(in);
	    	password = prop.getProperty("password");
	    	in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 	   
		
    	return true;
    }
    
    boolean setPassword(String pw){
    	try {  
			FileOutputStream out = new FileOutputStream(configfile); 
			prop.setProperty("password", pw); // TODO replace with crypt password method
			prop.store(out, "Do NEVER edit this config!");
			out.flush();  
			out.close(); 
		} catch (IOException ex) { 
			ex.printStackTrace(); 
			return false;
		}
    	
    	loadPassword();
    	return true;    	
    }    
}