package com.pedrojm96.pixellogin.bukkit;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pedrojm96.core.CoreEncryption;
import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.data.CoreSQL;
import com.pedrojm96.core.effect.CoreActionBar;
import com.pedrojm96.core.effect.CoreBossBar;
import com.pedrojm96.core.effect.CoreTitles;

public class ChannelController{
	
	public PixelLoginBukkit plugin;
	
	
	public ChannelController(PixelLoginBukkit plugin) {
		this.plugin = plugin;
	}
	
	
	public void onCaptchaProfile(Player player, String token) {
	
		
		
		
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
			
		}
	}
	
	
	public void onLoadProfile(Player player, String token) {
		
		new BukkitRunnable()
	    {
    		@Override
			public void run()
    		{
    			if(player != null && player.isOnline()) {
    				
    				Integer timerID = (Integer)plugin.get_timers.remove(player.getName());
    			    if (timerID != null) {
    			    	Bukkit.getScheduler().cancelTask(timerID.intValue());
    			    }
    				
    				PlayerProfile playerProfile;
			        if(plugin.player_profile.containsKey(player.getName())) {
			        	plugin.log.debug("["+player.getName()+"]Los datos ya estan cargado, inorando....");
			        	return;
			        }else {
			        	playerProfile = new PlayerProfile(false,false,false,"none");
			        	plugin.player_profile.put(player.getName(),playerProfile);
			        }
    				
    				
    				
    				if(plugin.data.checkData(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "uuid")) {
    					HashMap<String, String> data = plugin.data.get(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()),"login","hash","pincode","pin_login","token");
 
    					Boolean registered = true;
    			        Boolean login = (Integer.valueOf(data.get("login"))==1?true:false);
    			        Boolean pin = (Integer.valueOf(data.get("pin_login"))==1?true:false);
    			        String pin_code = data.get("pincode");
    			        String has = data.get("hash");
    					
    					String hashtoken =  data.get("token");
    					
    					

        				if(hashtoken.equals(CoreEncryption.valueOf(has).encrypt(token))) {
        					playerProfile = plugin.player_profile.get(player.getName());
        					playerProfile.setLogin(login);
        					playerProfile.setPin_login(pin);
        					playerProfile.setRegistered(registered);
        					playerProfile.setPingCode(pin_code);
        					playerProfile.setHash(has);
        					plugin.player_profile.replace(player.getName(),playerProfile);
        					
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
        					plugin.log.alert("Ocurrio un error al validar el token de "+player.getName()+" en la base de datos.");
    						player.kickPlayer(CoreColor.colorCodes(AllString.prefix+AllString.token_error));
        				}
    				}else {
    					
    					plugin.addEffects(player);
    				}
    			}	
    		}
	    }.runTaskLater(this.plugin, 5L);
	}
	
	
	public void onLogin(Player player, String token) {
		new BukkitRunnable()
	    {
    		@Override
			public void run()
    		{
    			if(player != null && player.isOnline()) {
    				
    				if(plugin.data.checkData(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "uuid")) {
    					HashMap<String, String> data = plugin.data.get(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()),"login","hash","pincode","pin_login","token");
    					
    					
    					PlayerProfile playerProfile;
    					if(plugin.player_profile.containsKey(player.getName())) {
    						playerProfile = plugin.player_profile.get(player.getName());
     			        	
     			        }else {
     			        	playerProfile = new PlayerProfile(true,false,false,data.get("pincode"));
     			        	plugin.player_profile.put(player.getName(),playerProfile);
     			        }
    					
    					
    					playerProfile.setHash(data.get("hash"));
    					playerProfile.setPingCode(data.get("pincode"));
    					playerProfile.setRegistered(true);
    					
    					String hashtoken =  data.get("token");
        				if(hashtoken.equals(CoreEncryption.valueOf(playerProfile.getHash()).encrypt(token))) {
        					boolean login = (Integer.valueOf(data.get("login"))==1?true:false);
        					boolean pin_login = (Integer.valueOf(data.get("pin_login"))==1?true:false);
        					if(login) {
        						playerProfile.setLogin(login);
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

        					}else {
        						plugin.log.alert("Ocurrio un error al validar el login de "+player.getName()+" en la base de datos.");
        						player.kickPlayer(CoreColor.colorCodes(AllString.prefix+AllString.token_error));
        					}
        				}else {
        					plugin.log.alert("Ocurrio un error al validar el token de "+player.getName()+" en la base de datos.");
        					player.kickPlayer(CoreColor.colorCodes(AllString.prefix+AllString.token_error));
        				}
    				}else {
    					plugin.log.alert("Ocurrio un error al validar al jugador "+player.getName()+" en la base de datos.");
    					player.kickPlayer(CoreColor.colorCodes(AllString.prefix+AllString.token_error));
    				}
    			}	
    		}
	    }.runTaskLater(this.plugin, 10L);
				
	}
	
	public void onLoadPluginMessage(String json) {
		JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
		AllString.load(obj);
		plugin.messages = true;

	}
	
	public void onPlayerRegistered(Player player,String token) {
		
		new BukkitRunnable()
	    {
    		@Override
			public void run()
    		{
    			if(player != null && player.isOnline()) {
    				if(plugin.data.checkData(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "uuid")) {
    					
    					HashMap<String, String> data = plugin.data.get(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()),"login","hash","pincode","pin_login","token");
    					
    					PlayerProfile playerProfile;
    					if(plugin.player_profile.containsKey(player.getName())) {
    						playerProfile = plugin.player_profile.get(player.getName());
     			        	
     			        }else {
     			        	playerProfile = new PlayerProfile(true,false,false,data.get("pincode"));
     			        	 plugin.player_profile.put(player.getName(),playerProfile);
     			        }
    					
    					playerProfile.setHash(data.get("hash"));
    					playerProfile.setPingCode(data.get("pincode"));
    					playerProfile.setRegistered(true);
    					String hashtoken =  data.get("token");
	    				if(hashtoken.equals(CoreEncryption.valueOf(playerProfile.getHash()).encrypt(token))) {
	    					boolean login = (Integer.valueOf(data.get("login"))==1?true:false);
	    					boolean pin_login = (Integer.valueOf(data.get("pin_login"))==1?true:false);
	    					if(login) {
	    						playerProfile.setLogin(login);	
	    						
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
	    						plugin.log.alert("Ocurrio un error al validar el registro de "+player.getName()+" en la base de datos.");
	    						player.kickPlayer(CoreColor.colorCodes(AllString.prefix+AllString.token_error));
	    					}
	    				}else {
	    					plugin.log.alert("Ocurrio un error al validar el token de "+player.getName()+" en la base de datos.");
	    					player.kickPlayer(CoreColor.colorCodes(AllString.prefix+AllString.token_error));
	    				}
        			}else {
        				plugin.log.alert("Ocurrio un error al validar al jugador "+player.getName()+" en la base de datos.");
        				player.kickPlayer(CoreColor.colorCodes(AllString.prefix+AllString.token_error));
        			}
    			}		
    		}
	    }.runTaskLater(this.plugin, 10L);    
	}
	
	
	public void onSendToLobbyWorld(Player player,String send_to_lobby_world) {
		
		if(this.plugin.config.getBoolean("lobby-world.enable")) {
	    	new BukkitRunnable()
		    {
	    		@Override
				public void run()
	    		{
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
	
	
	public void onSendTitle(Player player,String title) {
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
	
	public void onRunTitle(Player player,String title) {
		
		if(title.trim().equalsIgnoreCase("login")) {
			
			plugin.login_register_timers.put(player.getName(), Integer.valueOf(Bukkit.getScheduler().runTaskTimer(plugin, new Runnable()
		    {
		      public void run()
		      {
		    	  CoreBossBar.sendBossBar(player, AllString.login_bossbar, "PURPLE", "NOTCHED_20", 10, plugin);
		    	  
		    	  CoreTitles.sendTitles(player, AllString.login_title, AllString.login_subtitle);
		    	
		      }
		    }, 20L, 100L).getTaskId()));
		}
		if(title.trim().equalsIgnoreCase("register")) {
			
			plugin.login_register_timers.put(player.getName(), Integer.valueOf(Bukkit.getScheduler().runTaskTimer(plugin, new Runnable()
		    {
		      public void run()
		      {
		    	  CoreBossBar.sendBossBar(player, AllString.register_bossbar,CoreBossBar.Color.PURPLE ,CoreBossBar.Style.NOTCHED_20, 10, plugin);
		    	  CoreTitles.sendTitles(player, AllString.register_title, AllString.register_subtitle);
		    	
		      }
		    }, 20L, 100L).getTaskId()));
		}
	}
	
}
