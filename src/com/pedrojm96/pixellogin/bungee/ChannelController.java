package com.pedrojm96.pixellogin.bungee;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;
import com.pedrojm96.core.CoreEncryption;
import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.data.CoreSQL;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChannelController {

	private PixelLoginBungee plugin;
	
	public ChannelController(PixelLoginBungee plugin) {
		this.plugin = plugin;
	}
	
	
	public void onPinLogin(ProxiedPlayer player, String pin_login) {
		
		ProxiedProfile proxiedProfile = this.plugin.proxiedProfile.get(player.getName());
		
		ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable()
	    {
	      public void run()
	      {
	    	HashMap<String, String> data = plugin.data.get(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()),"hash","pincode","pin_login");
	    	boolean pin_login = Integer.valueOf(data.get("pin_login"))==1?true:false;
	    	if(pin_login) {
	    		proxiedProfile.setPinLogin(pin_login);
		    	proxiedProfile.setPincode(data.get("pincode"));
		    	proxiedProfile.setHash(data.get("hash"));
		  		plugin.log.debug("ProxiedProfile: setPinLogin("+true+")");
		  		plugin.proxiedProfile.replace(player.getName(), proxiedProfile);
	    	}else {
	    		plugin.log.debug("No se pudo registrar corretamente el pin");
	    		player.disconnect(CoreColor.getColorTextComponent(AllString.prefix+AllString.token_error));
	    	}
	    	
	    
	    	if(plugin.config.getBoolean("lobby-server.enable")) {
				ServerInfo server = ProxyServer.getInstance().getServerInfo(plugin.config.getString("lobby-server.name"));
				if(server==null) {
					CoreColor.message(player, AllString.prefix + "Lobby server not found");
				}else {
					ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable()
				    {
				      public void run()
				      {
				    	  player.connect(server);
				    	  
				      }
				    }, 1L, TimeUnit.SECONDS);
				}
			}  
	      }
	    }, 1L, TimeUnit.SECONDS);	
	}
	
	
	public void onCaptchaChecked(ProxiedPlayer player, String captcha_checked) {
		
		ProxiedProfile proxiedProfile = this.plugin.proxiedProfile.get(player.getName());
		
		ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable()
	    {
	      public void run()
	      {
	    	HashMap<String, String> data = plugin.data.get(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()),"hash","token");
	    	String hashtoken = data.get("token");
	    	if(hashtoken.equals(CoreEncryption.MD5.encrypt(captcha_checked))) {
	    		proxiedProfile.setCaptcha_checked(true);

	    		CoreColor.message(player, AllString.prefix + AllString.captcha_checked);
	    		
	    		Integer timerID = (Integer)plugin.login_register_timers.remove(player.getName());
	    	    if (timerID != null) {
	    	    	ProxyServer.getInstance().getScheduler().cancel(timerID.intValue());
	    	    }
	    	    
	    	    
	    	    plugin.messagingManager.sendToBukkit(player,"run-title", "register" , player.getServer().getInfo());
				plugin.login_register_timers.put(player.getName(), Integer.valueOf(ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable()
			    {
			      public void run()
			      {
			    	  CoreColor.message(player, AllString.prefix +AllString.register);
			    	  
			      }
			    }, 1L, 5L, TimeUnit.SECONDS).getId()));
	    	    
	    	}else {
	    		plugin.log.alert("Ocurrio un error al validar el token de "+player.getName()+" en la base de datos.");
	    		player.disconnect(CoreColor.getColorTextComponent(AllString.prefix+AllString.token_error));
	    	}
	      }
	    }, 2L, TimeUnit.SECONDS);	
	}
	
	
	
	public void onGetMessages(ProxiedPlayer player, String get_messages) {
		
		JsonObject object = new JsonObject();
		object.addProperty("prefix", AllString.prefix);
		object.addProperty("already_logged_in", AllString.already_logged_in);
		object.addProperty("register", AllString.register);
		object.addProperty("login", AllString.login);
		object.addProperty("login_title", AllString.login_title);
		object.addProperty("login_subtitle", AllString.login_subtitle);
		object.addProperty("register_title", AllString.register_title);
		object.addProperty("register_subtitle", AllString.register_subtitle);
		object.addProperty("register_success_title", AllString.register_success_title);
		object.addProperty("register_success_subtitle", AllString.register_success_subtitle);
		
		
		object.addProperty("login_failed_title", AllString.login_failed_title);
		object.addProperty("login_failed_subtitle", AllString.login_failed_subtitle);
		object.addProperty("login_success_title", AllString.login_success_title);
		object.addProperty("login_success_subtitle", AllString.login_success_subtitle);
		object.addProperty("error_no_permission", AllString.error_no_permission);
		object.addProperty("error_no_console", AllString.error_no_console);
		object.addProperty("error_already_pin", AllString.error_already_pin);
		object.addProperty("command_register_use", AllString.command_register_use);
		object.addProperty("command_login_use", AllString.command_login_use);
		object.addProperty("auto_premium_login_title", AllString.auto_premium_login_title);
		
		object.addProperty("auto_premium_login_subtitle", AllString.auto_premium_login_subtitle);
		object.addProperty("pin_backspace", AllString.pin_backspace);
		object.addProperty("pin_ok", AllString.pin_ok);
		object.addProperty("pin_code_menu", AllString.pin_code_menu);
		object.addProperty("pin_code_register_success", AllString.pin_code_register_success);
		object.addProperty("pin_code_login", AllString.pin_code_login);
		object.addProperty("pin_code_login_success", AllString.pin_code_login_success);
		object.addProperty("pin_code_failed", AllString.pin_code_failed);
		object.addProperty("pin_code_null", AllString.pin_code_null);
		object.addProperty("pin_code_login_actionbar", AllString.pin_code_login_actionbar);
		
		object.addProperty("login_bossbar", AllString.login_bossbar);
		object.addProperty("register_bossbar", AllString.register_bossbar);
		
		object.addProperty("token_error", AllString.token_error);
		
		object.addProperty("no_captcha_code", AllString.no_captcha_code);
		
		object.addProperty("captcha_checked_title", AllString.captcha_checked_title);
		object.addProperty("captcha_checked_subtitle", AllString.captcha_checked_subtitle);
		

		JsonObject object2 = new JsonObject();
		
		for(int i = 0 ; i<10;i++) {
			object2.addProperty(String.valueOf(i), AllString.pins_numbers.get(i));
		}
		
		object.add("pins_numbers", object2);

		String json = object.toString();
		
		plugin.messagingManager.sendToBukkit(player,"messages", json , player.getServer().getInfo());
		
	}
	
	
	
	public void onGetProfile(ProxiedPlayer player, String get_profile) {
		
		String token =  CoreEncryption.generateRandomText(64);
		ProxiedProfile plogin = this.plugin.proxiedProfile.get(player.getName());
		if(plogin.isRegistered()) {
				plugin.data.update(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()),"login:"+ (plogin.isLogin()?"1":"0")   ,"pin_login:"+ (plogin.isPinLogin()?"1":"0") ,"token:"+CoreEncryption.valueOf(plogin.getHash()).encrypt(token));
				plugin.messagingManager.sendToBukkit(player,"player-profile", token , player.getServer().getInfo());
		}else if(plugin.config.getBoolean("captcha-code")) {
			plugin.messagingManager.sendToBukkit(player,"captcha-profile", token , player.getServer().getInfo());
		}else {
			plugin.messagingManager.sendToBukkit(player,"player-profile", token , player.getServer().getInfo());
		}
		
		
		plugin.sendProfile.put(player.getName(), true);
	}
	
}
