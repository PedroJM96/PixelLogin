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

public class CommandsRegister extends CoreCommand{

	public PixelLoginBungee plugin;
	
	public CommandsRegister(PixelLoginBungee plugin,String name, String[] aliases) {
		super(name, "pixellogin.player.register", aliases);
		this.plugin = plugin;
		this.plugin.log.info("Register command /register");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCommand(CommandSender sender, String[] arg) {
		// TODO Auto-generated method stub
		if (!(sender instanceof ProxiedPlayer)) {
			CoreColor.message(sender,AllString.prefix + AllString.error_no_console);
       	 	return;
		}
		ProxiedPlayer player = (ProxiedPlayer)sender;
		ProxiedProfile plogin = this.plugin.proxiedProfile.get(player.getName());
		
		if(plugin.config.getBoolean("captcha-code") && !plogin.isCaptcha_checked()) {
			CoreColor.message(sender,AllString.prefix + AllString.captcha_code_register);
       	 	return;
		}
		
		if(plogin.isRegistered()) {
			CoreColor.message(sender,AllString.prefix + AllString.error_already_registered);
       	 	return;
		}
		if(arg.length == 0) {
			CoreColor.message(sender,AllString.prefix + AllString.command_register_use);
			return;
		}
		String clave = arg[0];
		String adresse = player.getAddress().getAddress().getHostAddress();
        
		
		
        
        String hashPassword = CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).encrypt(clave);

        
        plogin.setLogin(true);
        plugin.log.debug("ProxiedProfile: setLogin(true), setRegister(true)");
        
        plogin.setRegister(true);
       
        Integer timerID = (Integer)this.plugin.login_register_timers.remove(player.getName());
	    if (timerID != null) {
	    	ProxyServer.getInstance().getScheduler().cancel(timerID.intValue());
	    }
	    CoreColor.message(player, AllString.prefix + AllString.register_success);
	    /***********************************************************************************
	    * send titles bukkit---------------------------------------------------------------
	    * CoreTitles.sendTitles(player, AllString.register_success_title, AllString.register_success_subtitle);
	    ***********************************************************************************/
	    String token =  CoreEncryption.generateRandomText(64);
	    if(this.plugin.config.getBoolean("ping-code-all-users") && !(plugin.config.getBoolean("premium-auto-pin") && plogin.isPremiun())  ) {
        	CoreColor.message(player,AllString.prefix + AllString.pin_code_user_register);
        	
        	if(plugin.config.getBoolean("captcha-code")){
        		plugin.data.update(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "name:"+player.getName().toLowerCase(),"ip:"+adresse,"password:"+hashPassword,"pincode:none","premium:"+(plogin.isPremiun()?"1":"0"),"hash:"+CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).toString(),"registered:"+1,"login:"+1,"pin_login:"+0,"token:"+CoreEncryption.valueOf(plogin.getHash()).encrypt(token),"first_login:"+System.currentTimeMillis(),"last_login:"+System.currentTimeMillis(),  "last_disconnected:"+(plogin.isFullLogin()? System.currentTimeMillis() : "-1")   );
        	}else {
            	plugin.data.insert("uuid:"+player.getUniqueId().toString(), "name:"+player.getName().toLowerCase(),"ip:"+adresse,"password:"+hashPassword,"pincode:none","premium:"+(plogin.isPremiun()?"1":"0"),"hash:"+CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).toString(),"registered:"+1,"login:"+1,"pin_login:"+0,"token:"+CoreEncryption.valueOf(plogin.getHash()).encrypt(token),"first_login:"+System.currentTimeMillis(),"last_login:"+System.currentTimeMillis(),   "last_disconnected:"+(plogin.isFullLogin()? System.currentTimeMillis() : "-1") );
        	}
        }else if(this.plugin.config.getBoolean("ping-code-staff")  &&  player.hasPermission("pixellogin.staff") && !(plugin.config.getBoolean("premium-auto-pin") && plogin.isPremiun()) ) {
			CoreColor.message(player,AllString.prefix + AllString.pin_code_staff_register);
			if(plugin.config.getBoolean("captcha-code")){
        		plugin.data.update(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "name:"+player.getName().toLowerCase(),"ip:"+adresse,"password:"+hashPassword,"pincode:none","premium:"+(plogin.isPremiun()?"1":"0"),"hash:"+CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).toString(),"registered:"+1,"login:"+1,"pin_login:"+0,"token:"+CoreEncryption.valueOf(plogin.getHash()).encrypt(token),"first_login:"+System.currentTimeMillis(),"last_login:"+System.currentTimeMillis(),"last_disconnected:"+(plogin.isFullLogin()? System.currentTimeMillis() : "-1")  );
        	}else {
        		plugin.data.insert("uuid:"+player.getUniqueId().toString(), "name:"+player.getName().toLowerCase(),"ip:"+adresse,"password:"+hashPassword,"pincode:none","premium:"+(plogin.isPremiun()?"1":"0"),"hash:"+CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).toString(),"registered:"+1,"login:"+1,"pin_login:"+0,"token:"+CoreEncryption.valueOf(plogin.getHash()).encrypt(token),"first_login:"+System.currentTimeMillis(),"last_login:"+System.currentTimeMillis(),"last_disconnected:"+System.currentTimeMillis());
        	}
		
        	
        }else {
        	plogin.setPinLogin(true);
            plugin.log.debug("ProxiedProfile: setPinLogin(true)");
          
            if(plugin.config.getBoolean("captcha-code")){
        		plugin.data.update(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "name:"+player.getName().toLowerCase(),"ip:"+adresse,"password:"+hashPassword,"pincode:none","premium:"+(plogin.isPremiun()?"1":"0"),"hash:"+CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).toString(),"registered:"+1,"login:"+1,"pin_login:"+1,"token:"+CoreEncryption.valueOf(plogin.getHash()).encrypt(token),"first_login:"+System.currentTimeMillis(),"last_login:"+System.currentTimeMillis(),"last_disconnected:"+(plogin.isFullLogin()? System.currentTimeMillis() : "-1")   );
        	}else {
        		plugin.data.insert("uuid:"+player.getUniqueId().toString(), "name:"+player.getName().toLowerCase(),"ip:"+adresse,"password:"+hashPassword,"pincode:none","premium:"+(plogin.isPremiun()?"1":"0"),"hash:"+CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).toString(),"registered:"+1,"login:"+1,"pin_login:"+1,"token:"+CoreEncryption.valueOf(plogin.getHash()).encrypt(token),"first_login:"+System.currentTimeMillis(),"last_login:"+System.currentTimeMillis(),"last_disconnected:"+(plogin.isFullLogin()? System.currentTimeMillis() : "-1") );
        	}

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
	    
	    this.plugin.messagingManager.sendToBukkit(player,"register", token , player.getServer().getInfo());
	}

	@Override
	public String getErrorNoPermission() {
		// TODO Auto-generated method stub
		return AllString.prefix + AllString.error_no_permission;
	}

	@Override
	public String getPerm() {
		// TODO Auto-generated method stub
		return "pixellogin.player.register";
	}

}
