package com.pedrojm96.pixellogin.bungee.commands;

import java.util.concurrent.TimeUnit;

import com.pedrojm96.core.CoreEncryption;
import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.CoreCommand;
import com.pedrojm96.core.bungee.data.CoreSQL;
import com.pedrojm96.pixellogin.bungee.AllString;
import com.pedrojm96.pixellogin.bungee.PixelLoginBungee;
import com.pedrojm96.pixellogin.bungee.ProxiedProfile;


import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class CommandsLogin extends CoreCommand{

	public PixelLoginBungee plugin;
	
	public CommandsLogin(PixelLoginBungee plugin,String name, String[] aliases) {
		super(name, "pixellogin.player.login", aliases);
		this.plugin = plugin;
		this.plugin.log.info("Register command /login");
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		if (!(sender instanceof ProxiedPlayer)) {
			CoreColor.message(sender,AllString.prefix + AllString.error_no_console);
       	 	return;
		}
		ProxiedPlayer player = (ProxiedPlayer)sender;
		ProxiedProfile plogin = this.plugin.proxiedProfile.get(player.getName());
		if(!plogin.isRegistered()) {
			CoreColor.message(sender,AllString.prefix + AllString.command_register_use);
       	 	return;
		}
		if(plogin.isLogin()) {
			CoreColor.message(sender,AllString.prefix + AllString.you_already_logged_in);
       	 	return;
		}
		if(args.length == 0) {
			CoreColor.message(sender,AllString.prefix + AllString.command_login_use);
			return;
		}
		String clave = args[0];
		String hashPassword = CoreEncryption.valueOf(plogin.getHash()).encrypt(clave);
		if(!plogin.checkHashPassword(hashPassword)) {
			CoreColor.message(sender,AllString.prefix + AllString.login_failed);
			//enviar al bukkit
			this.plugin.messagingManager.sendToBukkit(player,"title", "login-failed" , player.getServer().getInfo());
			return;
		}
		

        Integer timerID = (Integer)this.plugin.login_register_timers.remove(player.getName());
	    if (timerID != null) {
	    	ProxyServer.getInstance().getScheduler().cancel(timerID.intValue());
	    }
	    CoreColor.message(player, AllString.prefix + AllString.login_success);
	    
	    
	    
	    plogin.setLogin(true);
	    plugin.log.debug("ProxiedProfile: setLogin(true)");
	    String token =  CoreEncryption.generateRandomText(64);
	    if(this.plugin.config.getBoolean("ping-code-all-users")) {
	    	if(plogin.hashPinCode()) {
	    		CoreColor.message(player,AllString.prefix + AllString.pin_code_login);
	    		//SimpleGUI gui =  new SimpleGUI(AllString.pin_code_menu,6,15);
	    		//plugin.gui.put(player.getUniqueId(),gui);
	    		//gui.open(player);
			}else {
				CoreColor.message(player,AllString.prefix + AllString.pin_code_user_register);
			}
			plugin.data.update(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "last_login:"+System.currentTimeMillis(),"login:"+1,"token:"+CoreEncryption.valueOf(plogin.getHash()).encrypt(token),"ip:"+player.getAddress().getAddress().getHostAddress());
        }else if(this.plugin.config.getBoolean("ping-code-staff")  &&  player.hasPermission("pixellogin.staff") ) {
	    	if(plogin.hashPinCode()) {
	    		CoreColor.message(player,AllString.prefix + AllString.pin_code_login);
	    		//SimpleGUI gui =  new SimpleGUI(AllString.pin_code_menu,6,15);
	    		//plugin.gui.put(player.getUniqueId(),gui);
	    		//gui.open(player);
			}else {
				CoreColor.message(player,AllString.prefix + AllString.pin_code_staff_register);
			}	
			plugin.data.update(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "last_login:"+System.currentTimeMillis(),"login:"+1,"token:"+CoreEncryption.valueOf(plogin.getHash()).encrypt(token),"ip:"+player.getAddress().getAddress().getHostAddress());
        }else {
        	plogin.setPinLogin(true);
     	    plugin.log.debug("ProxiedProfile: setPinLogin(true)");
			plugin.data.update(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "last_login:"+System.currentTimeMillis(),"login:"+1,"pin_login:"+1,"token:"+CoreEncryption.valueOf(plogin.getHash()).encrypt(token),"ip:"+player.getAddress().getAddress().getHostAddress());

     	    if(this.plugin.config.getBoolean("lobby-server.enable")) {
     			ServerInfo server = ProxyServer.getInstance().getServerInfo(this.plugin.config.getString("lobby-server.name"));
     			if(server==null) {
     				CoreColor.message(player, AllString.prefix + "Lobby server not found");
     			}else {
     				ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable()
     			    {
     			      public void run()
     			      {
     			    	  
     			    	  player.connect(server);
     			    	  
     			      }
     			    }, 5L, TimeUnit.SECONDS);
     			}
     		}else {
     			this.plugin.messagingManager.sendToBukkit(player,"send-to-lobby-world", "true" , player.getServer().getInfo());
     		} 	
        }
	    this.plugin.messagingManager.sendToBukkit(player,"login", token , player.getServer().getInfo());
	   
	}

	@Override
	public String getErrorNoPermission() {
		// TODO Auto-generated method stub
		return AllString.prefix + AllString.error_no_permission;
	}

	@Override
	public String getPerm() {
		// TODO Auto-generated method stub
		return "pixellogin.player.login";
	}

}
