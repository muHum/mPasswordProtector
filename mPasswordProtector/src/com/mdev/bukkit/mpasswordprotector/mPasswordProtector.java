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
	static File pwd = new File(mainDirectory + File.separator + "protector.dat"); 
	static Properties prop = new Properties(); 

    public ArrayList<Player> unauthorisedPlayers = new ArrayList<Player>();
    static String password;
    
    public void onDisable() {
    	PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( "[INFO] " + pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!" );
    }

    public void onEnable() {     	    	
    	new File(mainDirectory).mkdir(); //makes the Zones directory/folder in the plugins directory
    	 
		if(!pwd.exists()){ 
			try { 
				pwd.createNewFile(); 
				FileOutputStream out = new FileOutputStream(pwd); 
				prop.put("password", "asdfasdf"); // 
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
        
        PluginDescriptionFile pdfFile = this.getDescription();  
        System.out.println( "[mPasswordProtector] " + " v" + pdfFile.getVersion() + " enabled!" );
    }

    public String getPwd(){
    	// TODO Read password from encrypted file
    	System.out.println( "[mPasswordProtector] debug -- password= " + password );
    	return password;
    }
    
    boolean loadPassword(){    	
    	FileInputStream in;
		try {
			in = new FileInputStream(pwd);
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
			FileOutputStream out = new FileOutputStream(pwd); 
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