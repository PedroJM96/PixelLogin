package com.pedrojm96.pixellogin.bungee;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pedrojm96.core.bungee.CoreColor;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeDeliverCallback implements DeliverCallback{

	private PixelLoginBungee plugin;
	
	public BungeeDeliverCallback(PixelLoginBungee plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public void handle(String consumerTag, Delivery delivery) throws IOException {
		// TODO Auto-generated method stub
		String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
		if(json.isEmpty()) {
			plugin.log.debug("json string is null");
			return;
		}
		JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
		if(obj==null) {
			plugin.log.debug("json is null");
			return;
		}
		System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + json + "'");
        String type = obj.get("type").getAsString();
        String server = obj.get("server").getAsString();
        String player = obj.get("player").getAsString();
        if(type.equalsIgnoreCase("get-messages")) {
        	plugin.log.debug("*** ["+player+"] - Solicitud de mensajes revividad, enviando mensajes al servidor bukkit "+server+"....");
        	onGetMessages(server);
        }
        
        if(type.equalsIgnoreCase("get-profile")) {
        	plugin.log.debug("*** ["+player+"] - Solicitud de los datos de Profile recividad, enviando datos al servidor de bukkit "+server+"...");
        	onGetProfile(server,player);
        }
        
        if(type.equalsIgnoreCase("pin-login")) {
        	plugin.log.debug("*** ["+player+"] - El jugador coloco el pin corretamente en el servidor bukkit.");
        	onPinLogin(player);
        }
        
        if(type.equalsIgnoreCase("captcha-checked")) {
        	plugin.log.debug("*** ["+player+"] - El jugador coloco el captcha corretamente en el servidor bukkit.");
        	onCaptchaChecked(player);
        }  
	}
	
	
	
	public void onCaptchaChecked(String playername) {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playername);
		if(player != null && player.isConnected()) {
			ProxiedProfile proxiedProfile = this.plugin.proxiedProfile.get(player.getName());
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
		}    	
	}
	
	
	public void onPinLogin(String playername) {
		
		
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playername);
		if(player != null && player.isConnected()) {
			ProxiedProfile proxiedProfile = this.plugin.proxiedProfile.get(playername);
    		proxiedProfile.setPinLogin(true);
	  		plugin.log.debug("ProxiedProfile: setPinLogin("+true+")");
	  		plugin.proxiedProfile.replace(player.getName(), proxiedProfile);
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
	  		}else {
	  			this.plugin.messagingManager.sendToBukkit(player,"send-to-lobby-world", "true" , player.getServer().getInfo());
	     		
	  		}
		}  	
	}
	
	
	public void onGetMessages(String server) {
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
		JsonObject maintObject = new JsonObject();
		maintObject.addProperty("type", "messages");
		maintObject.add("messages", object);
		plugin.messagingManager.messageService.send(server, maintObject);
		
	}
	
	public void onGetProfile(String server, String player) {
		//String token =  CoreEncryption.generateRandomText(64);
		if(!this.plugin.proxiedProfile.containsKey(player)) {
			return;
		}
		ProxiedProfile plogin = this.plugin.proxiedProfile.get(player);
		if(plogin.isRegistered()) {
			//plugin.data.update(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()),"login:"+ (plogin.isLogin()?"1":"0")   ,"pin_login:"+ (plogin.isPinLogin()?"1":"0") ,"token:"+CoreEncryption.valueOf(plogin.getHash()).encrypt(token));
			JsonObject object = new JsonObject();
			object.addProperty("login", plogin.isLogin());
			object.addProperty("pinlogin", plogin.isPinLogin());
			object.addProperty("registered", plogin.isRegistered());
			object.addProperty("pincode", plogin.getPincode());
			object.addProperty("hash", plogin.getHash());
			
			JsonObject maintObject = new JsonObject();
			maintObject.addProperty("type", "player-profile");
			maintObject.addProperty("player", player);
			maintObject.add("profile", object);
			plugin.messagingManager.messageService.send(server, maintObject);
		}else if(plugin.config.getBoolean("captcha-code")) {		
			JsonObject maintObject = new JsonObject();
			maintObject.addProperty("type", "captcha-profile");
			maintObject.addProperty("player", player);
			maintObject.addProperty("hash", plogin.getHash());
			plugin.messagingManager.messageService.send(server, maintObject);
			
		}else {
			JsonObject object = new JsonObject();
			object.addProperty("login", plogin.isLogin());
			object.addProperty("pinlogin", plogin.isPinLogin());
			object.addProperty("registered", plogin.isRegistered());
			object.addProperty("pincode", plogin.getPincode());
			object.addProperty("hash", plogin.getHash());
			JsonObject maintObject = new JsonObject();
			maintObject.addProperty("type", "player-profile");
			maintObject.addProperty("player", player);
			maintObject.add("profile", object);
			plugin.messagingManager.messageService.send(server, maintObject);
		}
		plugin.sendProfile.put(player, true);
	}
	

}
