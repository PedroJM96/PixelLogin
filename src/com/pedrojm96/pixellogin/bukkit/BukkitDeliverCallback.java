package com.pedrojm96.pixellogin.bukkit;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pedrojm96.core.effect.CoreActionBar;
import com.pedrojm96.core.effect.CoreBossBar;
import com.pedrojm96.core.effect.CoreTitles;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class BukkitDeliverCallback implements DeliverCallback{

	private PixelLoginBukkit plugin;
	
	public BukkitDeliverCallback(PixelLoginBukkit plugin) {
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
        String type = obj.get("type").getAsString();
        System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + json + "'");
        
        if(type.equalsIgnoreCase("player-profile")) {
        	String player = obj.get("player").getAsString();
        	JsonObject profile = obj.get("profile").getAsJsonObject();
        	plugin.log.debug("["+player+"]Recibiendo datos del profile del jugador del servidor bungee....");
        	onLoadProfile(player,profile);
        }
        if(type.equalsIgnoreCase("captcha-profile")) {
        	String player = obj.get("player").getAsString();
        	String hash = obj.get("hash").getAsString();
        	plugin.log.debug("["+player+"]Recibiendo datos del profile temporal para el chaptcha del servidor bungee....");
        	onCaptchaProfile(player,hash);
        }
        if(type.equalsIgnoreCase("messages")) {
        	JsonObject messages = obj.get("messages").getAsJsonObject();
        	plugin.log.debug("Recibiendo datos de los mensajes del servidor bungee....");
        	onLoadPluginMessage(messages);
        }
        
        if(type.equalsIgnoreCase("login")) {
        	String player = obj.get("player").getAsString();
        	boolean pin_login = obj.get("pinlogin").getAsBoolean();
        	plugin.log.debug("["+player+"]Recibiendo el login del jugador en el servidor bungee....");
        	onLogin(player,pin_login);
        }
        
        if(type.equalsIgnoreCase("register")) {
        	String player = obj.get("player").getAsString();
        	boolean pin_login = obj.get("pinlogin").getAsBoolean();
        	plugin.log.debug("["+player+"]Recibiendo el registro del jugador en el servidor bungee....");
        	onPlayerRegistered(player,pin_login);
        }
        
        if(type.equalsIgnoreCase("send-to-lobby-world")) {
        	String player = obj.get("player").getAsString();
        	
        	plugin.log.debug("["+player+"]Recibiendo ordenes del servidor bungee para el envio del jugador al mundo lobby si esta configurado....");
        	onSendToLobbyWorld(player);
        }
        
        if(type.equalsIgnoreCase("title")) {
        	String player = obj.get("player").getAsString();
        	String title = obj.get("title").getAsString();
        	plugin.log.debug("["+player+"]Recibiendo ordenes del servidor bungee para el envio de titulos al jugador....");
        	onSendTitle(player,title);
        }
        
        if(type.equalsIgnoreCase("run-title")) {
        	String player = obj.get("player").getAsString();
        	String title = obj.get("run-title").getAsString();
        	plugin.log.debug("["+player+"]Recibiendo ordenes del servidor bungee para el envio de titulos repetitivos al jugador....");
        	onRunTitle(player,title);
        }
       
	}
	
	
	public void onRunTitle(String playername,String title) {
		Player player = Bukkit.getPlayerExact(playername);
		
		
		if(player != null && player.isOnline()) {
			if(title.trim().equalsIgnoreCase("login")) {
				
				plugin.login_register_timers.put(player.getName(), Integer.valueOf(Bukkit.getScheduler().runTaskTimer(plugin, new Runnable()
			    {
			      public void run()
			      {
			    	  
			    	  if(player != null && player.isOnline()) {
			    		  CoreBossBar.sendBossBar(player, AllString.login_bossbar, "PURPLE", "NOTCHED_20", 10, plugin);
				    	  CoreTitles.sendTitles(player, AllString.login_title, AllString.login_subtitle);
			  		  }
			      }
			    }, 20L, 100L).getTaskId()));
			}
			if(title.trim().equalsIgnoreCase("register")) {
				
				plugin.login_register_timers.put(player.getName(), Integer.valueOf(Bukkit.getScheduler().runTaskTimer(plugin, new Runnable()
			    {
			      public void run()
			      {
			    	  if(player != null && player.isOnline()) {
			    		  CoreBossBar.sendBossBar(player, AllString.register_bossbar,CoreBossBar.Color.PURPLE ,CoreBossBar.Style.NOTCHED_20, 10, plugin);
				    	  CoreTitles.sendTitles(player, AllString.register_title, AllString.register_subtitle);
			    	  }
			      }
			    }, 20L, 100L).getTaskId()));
			}
		}	
	}
	
	

	public void onSendTitle(String playername,String title) {
		Player player = Bukkit.getPlayerExact(playername);
		if(player != null && player.isOnline()) {
			if(title.trim().equalsIgnoreCase("auto-premium-login")) {
				CoreTitles.sendTitles(player, AllString.auto_premium_login_title, AllString.auto_premium_login_subtitle);
			}
			if(title.trim().equalsIgnoreCase("login")) {
				CoreTitles.sendTitles(player, AllString.login_title, AllString.login_subtitle);
			}
			if(title.trim().equalsIgnoreCase("register")) {
				CoreTitles.sendTitles(player, AllString.register_title, AllString.register_subtitle);
			}
			if(title.trim().equalsIgnoreCase("login-failed")) {
				CoreTitles.sendTitles(player, AllString.login_failed_title, AllString.login_failed_subtitle);
				if (plugin.sound_login_command_failed != null) {
			    	player.playSound(player.getLocation(), plugin.sound_login_command_failed, 1.0F, 1.0F);	
			    }
			}
			
			if(title.trim().equalsIgnoreCase("login-success")) {
				CoreTitles.sendTitles(player, AllString.login_success_title, AllString.login_success_subtitle);
			}
			
			if(title.trim().equalsIgnoreCase("register-success")) {
				CoreTitles.sendTitles(player, AllString.register_success_title, AllString.register_success_subtitle);
			}
		}
	}
	
	public void onSendToLobbyWorld(String playername) {
		
		if(this.plugin.config.getBoolean("lobby-world.enable")) {
	    	new BukkitRunnable()
		    {
	    		@Override
				public void run()
	    		{
	    			Player player = Bukkit.getPlayerExact(playername);
	    			if(player != null && player.isOnline()) {
	    				World w = Bukkit.getWorld(plugin.config.getString("lobby-world.name"));
		    			if(w!=null) {
		    				player.teleport(w.getSpawnLocation());
		    			}
	    			}	
	    		}
		    }.runTaskLater(this.plugin, 40L);
	    }
	}
	
	
	public void onPlayerRegistered(String playername,Boolean pin_login) {
		
		Player player = Bukkit.getPlayerExact(playername);
		if(player != null && player.isOnline()) {	
			PlayerProfile playerProfile;
			if(plugin.player_profile.containsKey(player.getName())) {
				playerProfile = plugin.player_profile.get(player.getName());
				playerProfile.setRegistered(true);		
				playerProfile.setLogin(true);	
				plugin.removeEffects(player);
				Integer timerID = (Integer)plugin.login_register_timers.remove(player.getName());
			    if (timerID != null) {
			    	Bukkit.getScheduler().cancelTask(timerID.intValue());
			    }
			    CoreTitles.sendTitles(player, AllString.register_success_title, AllString.register_success_subtitle);
			    if (plugin.sound_register_command != null) {
			    	player.playSound(player.getLocation(), plugin.sound_register_command, 1.0F, 1.0F);	
			    }
			    
				if(pin_login){
					playerProfile.setPin_login(pin_login);	
				}else {
					plugin.pin_login_actionbar_timers.put(player.getName(), Integer.valueOf(Bukkit.getScheduler().runTaskTimer(plugin, new Runnable()
				    {
				      public void run()
				      {
				    	  if(!playerProfile.isPin_login()) {
				    		  CoreActionBar.sendActionBar(player, AllString.pin_code_login_actionbar);
				    	  }  
				      }
				    }, 0L, 20L).getTaskId()));
				}
				plugin.player_profile.replace(player.getName(),playerProfile);
	        }else {
	        	plugin.log.alert("Ocurrio un error al validar el registro de "+player.getName()+".");
	        }	
		}		
    		    
	}
	
	public void onLogin(String playername,Boolean pin_login) {
		Player player = Bukkit.getPlayerExact(playername);
		if(player != null && player.isOnline()) {
			PlayerProfile playerProfile;
			if(plugin.player_profile.containsKey(player.getName())) {
				playerProfile = plugin.player_profile.get(player.getName());
				playerProfile.setLogin(true);
				plugin.removeEffects(player);
				Integer timerID = (Integer)plugin.login_register_timers.remove(player.getName());
			    if (timerID != null) {
			    	Bukkit.getScheduler().cancelTask(timerID.intValue());
			    }
			    CoreTitles.sendTitles(player, AllString.login_success_title, AllString.login_success_subtitle);
			    if (plugin.sound_login_command_success != null) {
			    	player.playSound(player.getLocation(), plugin.sound_login_command_success, 1.0F, 1.0F);	
			    }
			    
			    if(pin_login) {
			    	playerProfile.setPin_login(pin_login);
			    }else {
			    	plugin.pin_login_actionbar_timers.put(player.getName(), Integer.valueOf(Bukkit.getScheduler().runTaskTimer(plugin, new Runnable()
				    {
				      public void run()
				      {
				    	  if(!playerProfile.isPin_login()) {
				    		  CoreActionBar.sendActionBar(player, AllString.pin_code_login_actionbar);
				    	  }  
				      }
				    }, 40L, 20L).getTaskId()));
			    }
			    
			    plugin.player_profile.replace(player.getName(),playerProfile);
			}	
		}				
	}
	

	
	public void onLoadPluginMessage(JsonObject obj) {
		AllString.load(obj);
		plugin.messages = true;

	}
	
	public void onCaptchaProfile(String playername,String hash) {
		Player player = Bukkit.getPlayerExact(playername);
		if(player != null && player.isOnline()) {
			Integer timerID = (Integer)plugin.get_timers.remove(player.getName());
		    if (timerID != null) {
		    	Bukkit.getScheduler().cancelTask(timerID.intValue());
		    } 
			
			if(plugin.player_profile.containsKey(player.getName())) {
				 plugin.log.debug("["+player.getName()+"]Los datos ya estan cargado, inorando....");
		         return;
			 }
			 
			 if(plugin.captchas.containsKey(player.getName())) {
				 plugin.log.debug("["+player.getName()+"]El chatpcha ya fue cargado inorando....");
		         return;
			 }
			 plugin.log.debug("["+player.getName()+"]Creando chaptcha para el jugador....");
			 Captcha captcha = new Captcha(this.plugin,player);
			 plugin.captchas.put(player.getName(), captcha);
			 captcha.sendMap();
			 PlayerProfile playerProfile = new PlayerProfile(false,false,false,"none");
			 playerProfile.setHash(hash);
			 plugin.player_profile.put(playername,playerProfile);
			
		}
	}
	
	
	public void onLoadProfile(String playername,JsonObject profile) {
		Player player = Bukkit.getPlayerExact(playername);
		if(player != null && player.isOnline()) {
			Integer timerID = (Integer)plugin.get_timers.remove(playername);
		    if (timerID != null) {
		    	Bukkit.getScheduler().cancelTask(timerID.intValue());
		    }
			
			PlayerProfile playerProfile;
	        if(plugin.player_profile.containsKey(playername)) {
	        	plugin.log.debug("["+playername+"]Los datos ya estan cargado, inorando....");
	        	return;
	        }
	        playerProfile = new PlayerProfile(false,false,false,"none");
	        Boolean registered = profile.get("registered").getAsBoolean();;
	        if(registered) {
		        Boolean login =  profile.get("login").getAsBoolean();
		        Boolean pin = profile.get("pinlogin").getAsBoolean();
		        String pin_code = profile.get("pincode").getAsString();
		        String has = profile.get("hash").getAsString();
				
				playerProfile.setLogin(login);
				playerProfile.setPin_login(pin);
				playerProfile.setRegistered(registered);
				playerProfile.setPingCode(pin_code);
				playerProfile.setHash(has);
				plugin.player_profile.put(playername,playerProfile);
				if(playerProfile.isLogin()) {
					plugin.removeEffects(player);
			    }else {
			    	plugin.addEffects(player);
			    }
				if(playerProfile.isLogin() && playerProfile.isPin_login()) {
					if(plugin.config.getBoolean("lobby-world.enable")) {
						if(player != null && player.isOnline()) {
							World w = Bukkit.getWorld(plugin.config.getString("lobby-world.name"));
							if(w!=null) {
								player.teleport(w.getSpawnLocation());
							}
						}	
					    	
					}
				}else {
					if(playerProfile.isLogin()) {
						plugin.pin_login_actionbar_timers.put(player.getName(), Integer.valueOf(Bukkit.getScheduler().runTaskTimer(plugin, new Runnable()
						{
							public void run()
							{
								CoreActionBar.sendActionBar(player, AllString.pin_code_login_actionbar);
						    	
							}
						}, 0L, 20L).getTaskId()));
					}
					if(plugin.config.getBoolean("auth-world.enable")) {
						if(player != null && player.isOnline()) {
							World w = Bukkit.getWorld(plugin.config.getString("auth-world.name"));
						   	if(w!=null) {
						  	player.teleport(w.getSpawnLocation());
							}
					 	}	
					    	
					}
				}
	        }else {
	        	plugin.player_profile.put(playername,playerProfile);
	        	plugin.addEffects(player);
	        }	
		}		
	}	
}
