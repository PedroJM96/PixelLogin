package com.pedrojm96.pixellogin.bungee;



import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bstats.bungeecord.Metrics;

import com.google.common.base.Charsets;
import com.pedrojm96.core.bungee.CoreConfig;
import com.pedrojm96.core.bungee.CoreLog;
import com.pedrojm96.core.bungee.CorePlugin;
import com.pedrojm96.core.bungee.CoreSpigotUpdater;
import com.pedrojm96.core.bungee.data.CoreMySQL;
import com.pedrojm96.core.bungee.data.CoreSQL;
import com.pedrojm96.pixellogin.bungee.commands.CommandsChangePassword;
import com.pedrojm96.pixellogin.bungee.commands.CommandsLogin;
import com.pedrojm96.pixellogin.bungee.commands.CommandsPixelLogin;
import com.pedrojm96.pixellogin.bungee.commands.CommandsRegister;
import com.pedrojm96.pixellogin.bungee.commands.CommandsUnRegister;
import com.pedrojm96.pixellogin.bungee.commands.subcommands.AccountsPixelLogin;
import com.pedrojm96.pixellogin.bungee.commands.subcommands.CrackedPixelLogin;
import com.pedrojm96.pixellogin.bungee.commands.subcommands.HelpPixelLogin;
import com.pedrojm96.pixellogin.bungee.commands.subcommands.InfoPixelLogin;
import com.pedrojm96.pixellogin.bungee.commands.subcommands.PremiumPixelLogin;
import com.pedrojm96.pixellogin.bungee.commands.subcommands.RegisterPixelLogin;
import com.pedrojm96.pixellogin.bungee.commands.subcommands.UnRegisterPixelLogin;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class PixelLoginBungee extends Plugin implements CorePlugin{
	
	public CoreLog log;
	public CoreSQL data;
	
	public CoreConfig config;
	public CoreConfig messages;
	
	public Plugin plugin;
	
	public HashMap<String, ProxiedProfile> proxiedProfile = new HashMap<String, ProxiedProfile>();
	
	public HashMap<String, OnConnectPlayer> connectPlayer = new HashMap<String, OnConnectPlayer>();
	
	public HashMap<String, Integer> login_register_timers = new HashMap<String, Integer>();
	
	public HashMap<String, Boolean> serverConnected = new HashMap<String, Boolean>();
	
	public boolean pluginStart = false;
	public HashMap<String, Boolean> sendProfile = new HashMap<String, Boolean>();
	
	
	public MessagingManager messagingManager;
	
	
	
	
	public void banner() {
		this.log.line();
		this.log.println("  _____ _          _ _                 _       ");
		this.log.println(" |  __ (_)        | | |               (_)      ");
		this.log.println(" | |__) |__  _____| | |     ___   __ _ _ _ __  ");
		this.log.println(" |  ___/ \\ \\/ / _ \\ | |    / _ \\ / _` | | '_ \\"); 
		this.log.println(" | |   | |>  <  __/ | |___| (_) | (_| | | | | |"); 
		this.log.println(" |_|   |_/_/\\_\\___|_|______\\___/ \\__, |_|_| |_|"); 
		this.log.println("                                  __/ |        "); 
		this.log.println("                                 |___/         ");
		this.log.line();
	}
	
	

	
	
	public void onEnable() {
		this.log = new CoreLog(this,"PixelLogin",CoreLog.Color.WHITE);
		banner();
		this.log.info("Plugin Create by PedroJM96.");
		this.log.info("Register commands...");
		PluginManager pluginManager = getProxy().getPluginManager();
		pluginManager.registerCommand(this, new CommandsRegister(this,"register",new String[] {"reg"}));
		pluginManager.registerCommand(this, new CommandsLogin(this,"login",new String[] {"lo"}));
		pluginManager.registerCommand(this, new CommandsUnRegister(this,"unregister",new String[] {"unreg"}));
		pluginManager.registerCommand(this, new CommandsChangePassword(this,"changepassword",new String[] {"chpass"}));
		CommandsPixelLogin pixellogincmd = new CommandsPixelLogin(this,"pixellogin",new String[] {"pixelo"});
		pixellogincmd.addSubCommand(Arrays.asList("register","reg"), new RegisterPixelLogin(this));
		pixellogincmd.addSubCommand(Arrays.asList("cracked"), new CrackedPixelLogin(this));
		pixellogincmd.addSubCommand(Arrays.asList("premium"), new PremiumPixelLogin(this));
		pixellogincmd.addSubCommand(Arrays.asList("info"), new InfoPixelLogin(this));
		pixellogincmd.addSubCommand(Arrays.asList("accounts"), new AccountsPixelLogin(this));
		pixellogincmd.addSubCommand(Arrays.asList("unregister","unreg"), new UnRegisterPixelLogin(this));
		pixellogincmd.addSubCommand(Arrays.asList("help","?"), new HelpPixelLogin(this));
		pluginManager.registerCommand(this, pixellogincmd);
		pluginManager.registerListener(this, new PixelBungeeListener(this));
		this.log.info("Loading configuration...");
		this.config = new CoreConfig(this,"config",log,this.getResourceAsStream("config_bungee.yml"),this.getResourceAsStream("config_bungee.yml"),true);
		this.log.seDebug(this.config.getBoolean("debug"));
		if(this.config.getString("messages").equalsIgnoreCase("ES")) {
			this.messages = new CoreConfig(this,"messages_ES",log,this.getResourceAsStream("messages_ES.yml"),this.getResourceAsStream("messages_ES.yml"),true);
		}else {
			this.messages = new CoreConfig(this,"messages_EN",log,this.getResourceAsStream("messages_EN.yml"),this.getResourceAsStream("messages_EN.yml"),true);
		}
		AllString.load(config, messages);
		if(this.config.getBoolean("data-storage.enable")) {
			this.data = new CoreMySQL(this,this.config.getString("data-storage.host"),this.config.getInt("data-storage.port"),this.config.getString("data-storage.database"),this.config.getString("data-storage.username"),this.config.getString("data-storage.password"),"pixellogin");
		}else {
			log.error("Plugin disable please set DataStorage in config.yml");
			getProxy().getPluginManager().unregisterListeners(this);
			getProxy().getPluginManager().unregisterCommands(this);
			log.line();
			return;
		}
		if(!this.data.checkStorage()) {
			this.data.build( CoreSQL.FIELD("uuid", "varchar(36)", String.class) ,CoreSQL.FIELD("name", "varchar(16)", String.class),CoreSQL.FIELD("ip", "varchar(40)", String.class) ,CoreSQL.FIELD("password", "varchar(255)", String.class),CoreSQL.FIELD("pincode", "varchar(255)", String.class),CoreSQL.FIELD("premium", "int(1)", int.class),CoreSQL.FIELD("registered", "int(1)", int.class),CoreSQL.FIELD("login", "int(1)", int.class),CoreSQL.FIELD("pin_login", "int(1)", int.class), CoreSQL.FIELD("token", "varchar(255)", String.class)  ,CoreSQL.FIELD("first_login", "bigint(20)", long.class),CoreSQL.FIELD("last_login", "bigint(20)", long.class),CoreSQL.FIELD("last_disconnected", "bigint(20)", long.class),CoreSQL.FIELD("hash", "varchar(10)", String.class));
		}else if(!this.data.columnExists("registered")) {
			this.log.alert("The \"registered\" column does not exist.");
			this.data.addColumn("registered", "INT NOT NULL");
			this.data.update("registered:"+1);;
		}

		messagingManager = new MessagingManager(this);
		if(!messagingManager.load()) {
			return;
		}
		
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this,6893);
		checkForUpdates();
		pluginStart = true;
		this.log.line();
	}
	
	@Override
    public void onDisable() {
		if(pluginStart) {
			messagingManager.closed();
		}
		
    }
	
	public void checkForUpdates() {
        if(config.getBoolean("update-check")){

        	getProxy().getScheduler().runAsync(plugin, new Runnable() {
				@Override
				public void run() {
					CoreSpigotUpdater updater = new CoreSpigotUpdater(plugin, 76893);
		        	try {
		                if (updater.checkForUpdates()) {
		                	log.info("An update was found! for PixelLogin. Please update to recieve latest version. download: " + updater.getResourceURL());
		                }	
		            } catch (Exception e) {
		            	
		            	log.error("Failed to check for a update on spigot.");
		            }
				}
        		
        	});
        } 
    }
	
	
	
	public UUID getOfflineUniqueId(String paramString)
	{
	    return UUID.nameUUIDFromBytes(("OfflinePlayer:" + paramString).getBytes(Charsets.UTF_8));
	}
	
	


	@Override
	public CoreLog getLog() {
		// TODO Auto-generated method stub
		return this.log;
	}


	@Override
	public Plugin getInstance() {
		// TODO Auto-generated method stub
		return this;
	}
	
}
