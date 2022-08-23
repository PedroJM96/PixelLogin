package com.pedrojm96.pixellogin.bungee;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.data.CoreSQL;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PixelBungeeListener implements Listener{
	
	private PixelLoginBungee plugin;
	
	
	
	public PixelBungeeListener(PixelLoginBungee plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority=64)
	public void onServerConnectedEvent(ServerConnectedEvent e) {
		ProxiedPlayer player = e.getPlayer();
		plugin.log.debug("["+player.getName()+"] [ServerConnectedEvent]");
	
		
		OnConnectPlayer onConnectPlayer = plugin.connectPlayer.get(player.getName());

		if(plugin.serverConnected.containsKey(player.getName()) &&  (plugin.serverConnected.get(player.getName()) == true)) {
			return;
		}
		plugin.serverConnected.put(player.getName(), true);
		if(onConnectPlayer.isRegistered()) {
			if(onConnectPlayer.isPremiumAutoLogin()) {
				ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable()
			    {
			      public void run()
			      {
			    	  
			    	  if(!onConnectPlayer.requiresPinCode()) {
			    		  CoreColor.message(player, AllString.prefix + AllString.auto_premium_login);
				    	  plugin.messagingManager.sendToBukkit(player,"title","auto-premium-login" , e.getServer().getInfo());
			    	  }else if(onConnectPlayer.isSessionPinActive()) {
		    			  CoreColor.message(player, AllString.prefix + AllString.session_restored);
		    		  }else {
		    			  
		    			  if(plugin.config.getBoolean("ping-code-all-users")) {
				    		  if(onConnectPlayer.hashPinCode()) {
				    			  CoreColor.message(player, AllString.prefix + AllString.auto_premium_login);
						    	  plugin.messagingManager.sendToBukkit(player,"title","auto-premium-login" , e.getServer().getInfo());  
				    			  CoreColor.message(player, AllString.prefix +  AllString.pin_code_login);
				    		  }else {
				    			  CoreColor.message(player, AllString.prefix +  AllString.pin_code_user_register);
				    		  }
				    	  }else if(plugin.config.getBoolean("ping-code-staff") && player.hasPermission("pixellogin.staff")) {
				    		  if(onConnectPlayer.hashPinCode()) {
				    			  CoreColor.message(player, AllString.prefix + AllString.auto_premium_login);
						    	  plugin.messagingManager.sendToBukkit(player,"title","auto-premium-login" , e.getServer().getInfo());
				    			  CoreColor.message(player, AllString.prefix +  AllString.pin_code_login);
				    		  }else {
				    			  CoreColor.message(player, AllString.prefix + AllString.pin_code_staff_register);
				    		  }  
				    	  }
		    		  } 
			      }
			    }, 3L, TimeUnit.SECONDS);
			}else if(onConnectPlayer.isFullLogin()){
				ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable()
			    {
			      public void run()
			      {
			    	  CoreColor.message(player, AllString.prefix + AllString.session_restored);
			      }
			    }, 3L, TimeUnit.SECONDS);
				
				
			}else if(onConnectPlayer.isLogin()){
				//reinicion de seccion
				if(!onConnectPlayer.requiresPinCode()) {
					ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable()
				    {
				      public void run()
				      {
				    	  CoreColor.message(player, AllString.prefix + AllString.session_restored);
				      }
				    }, 3L, TimeUnit.SECONDS);
				}else {
					if(plugin.config.getBoolean("ping-code-all-users")) {
			    		  if(onConnectPlayer.hashPinCode()) {
			    			  CoreColor.message(player, AllString.prefix + AllString.auto_premium_login);
					    	  plugin.messagingManager.sendToBukkit(player,"title","auto-premium-login" , e.getServer().getInfo());  
			    			  CoreColor.message(player, AllString.prefix +  AllString.pin_code_login);
			    		  }else {
			    			  CoreColor.message(player, AllString.prefix +  AllString.pin_code_user_register);
			    		  }
			    	  }else if(plugin.config.getBoolean("ping-code-staff") && player.hasPermission("pixellogin.staff")) {
			    		  if(onConnectPlayer.hashPinCode()) {
			    			  CoreColor.message(player, AllString.prefix + AllString.auto_premium_login);
					    	  plugin.messagingManager.sendToBukkit(player,"title","auto-premium-login" , e.getServer().getInfo());
			    			  CoreColor.message(player, AllString.prefix +  AllString.pin_code_login);
			    		  }else {
			    			  CoreColor.message(player, AllString.prefix + AllString.pin_code_staff_register);
			    		  }  
			    	  }
				}	
			}else {
				plugin.messagingManager.sendToBukkit(player,"run-title", "login" , e.getServer().getInfo());
				this.plugin.login_register_timers.put(e.getPlayer().getName(), Integer.valueOf(ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable()
			    {
			      public void run()
			      {
			    	  CoreColor.message(e.getPlayer(), AllString.prefix +AllString.login);
			    	  
			      }
			    }, 1L, 5L, TimeUnit.SECONDS).getId()));
			}
		}else if(plugin.config.getBoolean("captcha-code")){
			
			this.plugin.login_register_timers.put(e.getPlayer().getName(), Integer.valueOf(ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable()
		    {
		      public void run()
		      {
		    	  CoreColor.message(e.getPlayer(), AllString.prefix +AllString.captcha);
		    	  
		      }
		    }, 1L, 5L, TimeUnit.SECONDS).getId()));
		}else{
			
			plugin.messagingManager.sendToBukkit(player,"run-title", "register" , e.getServer().getInfo());
			this.plugin.login_register_timers.put(e.getPlayer().getName(), Integer.valueOf(ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable()
		    {
		      public void run()
		      {
		    	  CoreColor.message(e.getPlayer(), AllString.prefix +AllString.register);
		    	  
		      }
		    }, 1L, 5L, TimeUnit.SECONDS).getId()));
		}
		
		
		
		
		if(!onConnectPlayer.isLogin()) {
			
			if(plugin.config.getBoolean("timeout.enable")) {
				
				TimeUnit type = TimeUnit.SECONDS;
				long time = plugin.config.getInt("timeout.time");
				switch (plugin.config.getString("timeout.type")) {
					case "SECONDS":
						type = TimeUnit.SECONDS;
					case "MINUTES":
						type = TimeUnit.MINUTES;
					default:
						type = TimeUnit.SECONDS;
				}
				
				ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable()
				{
				  public void run()
				  {
					  if(player!=null) {
						  
						  if(ProxyServer.getInstance().getPlayer(player.getName()) != null) {
							  if(plugin.serverConnected.containsKey(player.getName()) &&  (plugin.serverConnected.get(player.getName()) == true)) {
								  if(!onConnectPlayer.isLogin()) {
									  //player kit meSsage
									  player.disconnect(CoreColor.getColorTextComponent(AllString.prefix+AllString.kick_timeout));
								  } 
							  }
						  }  
					  }  
				  }
				}, time, type);
			}
		}
	}
	
	
	@EventHandler(priority=64)
	public void onServerConnect(ServerConnectEvent e) {

	    if(e.isCancelled()) {
	    	return;
	    }
	    ProxiedPlayer player = e.getPlayer();
	    OnConnectPlayer onConnectPlayer = new OnConnectPlayer(player,plugin);
	    plugin.connectPlayer.put(player.getName(), onConnectPlayer);
	    plugin.log.debug("["+player.getName()+"] [ServerConnectEvent]");
		if(onConnectPlayer.isLogin()) {
			return;
		}

		if(onConnectPlayer.isRegistered()) {
			plugin.log.debug("** ["+player.getName()+"][Registrado] - Verificando si el jugador es premiun y el inicio premiun automatico esta activo... ");
			if(onConnectPlayer.isPremiumAutoLogin()) {
				plugin.log.debug("** ["+player.getName()+"][Registrado][AutoLogin] - Inicio atomatico esta activopero se comprueba si el usuario requiere utilizar un codigo ping y si la seccion esta activa...");
				if(!onConnectPlayer.requiresPinCode()) {
					//Si el jugador es premiun el auto login esta ativado, y auto pin esta ativado o no reguiere ping para todos los usuarios.
					plugin.log.debug("** ["+player.getName()+"][Registrado][AutoLogin][AutonPin][FullLogin] - No es necesario el codigo ping, el jugador inicia session y se envia al lobby si esta configurado...");
					onConnectPlayer.fullLogin(e);
				}else if(onConnectPlayer.isSessionPinActive()) {
					
					plugin.log.debug("** ["+player.getName()+"][Registrado][AutoLogin][SessionPin][FullLogin] - Es necesario el codigo ping, pero la seccion anterior aun esta activa, el jugador inicia session y se envia al lobby si esta configurado...");
					onConnectPlayer.fullLogin(e);
					
				}else {
					//Si el jugador es premiun y el auto login esta ativado, pero el ping esta ativado para todos los uauarios o para los miembros del staff y el jugador es un miembro del staff.
					plugin.log.debug("** ["+player.getName()+"][Registrado][AutoLogin][Login] - Es necesario el codigo ping, el jugador tiene que colocar el codigo, se envia al auth si esta configurado...");
					onConnectPlayer.login(e);
				}
			}else {
				//configuracion de la seccion.
				plugin.log.debug("** ["+player.getName()+"][Registrado] - Se comprueba si la session esta activa...");
				
				if(onConnectPlayer.isSessionPinActive()) {
					plugin.log.debug("** ["+player.getName()+"][Registrado][SessionPin][FullLogin] - La session anterior esta activa, se inicia session completa y se envia al lobby si esta configurado...");
					onConnectPlayer.fullLogin(e);
				}else if(onConnectPlayer.isSessionActive() ) {
					if(onConnectPlayer.requiresPinCode()) {
						plugin.log.debug("** ["+player.getName()+"][Registrado][Session][Login] - La session anterior esta activa, pero requiere colocar el codigo ping se envia al auth si esta configurado...");
						onConnectPlayer.login(e);
					}else {
						plugin.log.debug("** ["+player.getName()+"][Registrado][Session][FullLogin] - La session anterior esta activa, se inicia session completa y se envia al lobby si esta configurado...");
						onConnectPlayer.fullLogin(e);
					}
				}else {
					plugin.log.debug("** ["+player.getName()+"][Registrado] - La session anterior no esta activa, el jugador debe iniciar session se envia al auth si esta configurado...");
					onConnectPlayer.auth(e);
				}
			}
		}else {
			plugin.log.debug("** ["+player.getName()+"][NoRegistrado] - El jugador no esta registrado, el jugador debe registrarse, se envia al auth si esta configurado...");
			onConnectPlayer.auth(e);
		}
	}
	
	
	
	@EventHandler(priority=64)
	public void onPreLogin(PreLoginEvent e) {
		if (e.isCancelled()) {
			return;
	    }
		
		PreLoginPlayer preloginPlayer = new PreLoginPlayer(e.getConnection(),plugin);
		
		PendingConnection pendingConnection = e.getConnection();
		plugin.log.debug("["+pendingConnection.getName()+"] [PreLoginEvent]");
		plugin.log.debug("* ["+pendingConnection.getName()+"] - Verificando que el jugador tenga un nombre de usuario valido...");
		
		if(!preloginPlayer.checkValitName()) {
			e.setCancelled(true);
			e.setCancelReason(CoreColor.getColorTextComponent(AllString.prefix+AllString.player_invalid_name));
			return;
		}
		
		plugin.log.debug("* ["+pendingConnection.getName()+"] - Verificando que el jugador no se encuentre conectado al servidor...");
		if(preloginPlayer.alreadyJoin()) {
			e.setCancelled(true);
			e.setCancelReason(CoreColor.getColorTextComponent(AllString.prefix+AllString.already_logged_in));
			return;
		}
		
		
		plugin.log.debug("* ["+pendingConnection.getName()+"] - Comprobando que el jugador no exceda el limite de usuarios para una direcion ip...");
		if(preloginPlayer.isMaxIP()) {
			e.setCancelled(true);
			e.setCancelReason(CoreColor.getColorTextComponent(AllString.prefix+AllString.error_max_multi_accounts));
			return;
		}
		
		plugin.log.debug("* ["+pendingConnection.getName()+"] - Verificando si el jugador se encuentra registrado...");
		if(preloginPlayer.isRegisterDatabase()) {
			preloginPlayer.loadDatabaseProfile();	
		}else {
			plugin.log.debug("* ["+pendingConnection.getName()+"] - No esta registrado, omprobando si el jugador es premiun...");
			MojangProfile profile = new MojangProfile("https://api.mojang.com/users/profiles/minecraft/" + pendingConnection.getName(),plugin.log);
			profile.run();
			if(profile.error()) {
				e.setCancelled(true);
				e.setCancelReason(CoreColor.getColorTextComponent(AllString.prefix+AllString.error_mojangapi_shutdown));
				return;
			}
			if(profile.isPremiun()) {
				plugin.log.debug("* ["+pendingConnection.getName()+"] - El jugador es premiun, comprobando si ya se encuentra registrado con otro nombre de usuario...");
				if(preloginPlayer.checkPremiunNameChnage(profile)) {
					//El jugador cambio de nombre....
					preloginPlayer.loadNameChangeDatabaseProfile(profile);
				}else {
					//es un jugador premiun nuevo...
					preloginPlayer.loadNewPremiunProfile(profile);
				}
			}else {
				preloginPlayer.loadNewCrackeProfile();
			}
		}
		pendingConnection.setOnlineMode(preloginPlayer.isPremiunProfile());
	}
	
	
	
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		
		if(this.plugin.serverConnected.containsKey(e.getPlayer().getName())) {
			this.plugin.serverConnected.remove(e.getPlayer().getName());
		}
		
		if(this.plugin.sendProfile.containsKey(e.getPlayer().getName())) {
			this.plugin.sendProfile.remove(e.getPlayer().getName());
		}
		
		if(this.plugin.login_register_timers.containsKey(e.getPlayer().getName())) {
			Integer timerID = (Integer)this.plugin.login_register_timers.remove(e.getPlayer().getName());
		    if (timerID != null) {
		    	ProxyServer.getInstance().getScheduler().cancel(timerID.intValue());
		    }
		}
		
		if(this.plugin.connectPlayer.get(e.getPlayer().getName()).isFullLogin()) {
			if(plugin.data.checkData(CoreSQL.WHERE("uuid:"+uuid.toString() ), "uuid")) {
				plugin.data.update(CoreSQL.WHERE("uuid:"+uuid.toString()) ,"login:0","pin_login:0",   "last_disconnected:"+System.currentTimeMillis());
			}
		}
		
		if(this.plugin.connectPlayer.containsKey(e.getPlayer().getName())) {
			this.plugin.connectPlayer.remove(e.getPlayer().getName());
		}
		
		if(this.plugin.proxiedProfile.containsKey(e.getPlayer().getName())) {
			
			if(!this.plugin.proxiedProfile.get(e.getPlayer().getName()).isRegistered()) {
				plugin.data.delete(CoreSQL.WHERE("uuid:"+uuid.toString()));
			}
			
			this.plugin.proxiedProfile.remove(e.getPlayer().getName());
		}
		
		
		
	}
	
	@EventHandler(priority=-64)
	public void onChat(ChatEvent e) {

		if(e.isCancelled()) {
			
			return;
		}
		
	    if (!(e.getSender() instanceof ProxiedPlayer)) {
	      return;
	    }
	    
	   
	    
	    
	    
	    ProxiedPlayer player = (ProxiedPlayer)e.getSender();
	    OnConnectPlayer onConnectPlayer = this.plugin.connectPlayer.get(player.getName());
	    
	    if(plugin.config.getBoolean("captcha-code") && !onConnectPlayer.isCaptcha_checked() ) {

	    	if(e.getMessage().startsWith("/")) {
	    		e.setCancelled(true);
	    		CoreColor.message(player, AllString.prefix +  AllString.no_captcha_code);
	    		return;
	    	}
	    	return;
	    }
	    
	    
	    
	    if(e.getMessage().startsWith("/register") || e.getMessage().startsWith("/login") || e.getMessage().startsWith("/pin")) {
	    	return;
	    }
	    
	    if (!onConnectPlayer.isFullLogin()) {
	    
	    	e.setCancelled(true);
	    	if (onConnectPlayer.isRegistered())
	    	{
	    		if(onConnectPlayer.isLogin()) {
	    			
	    			if(plugin.config.getBoolean("ping-code-all-users")) {
			    		  if(onConnectPlayer.hashPinCode()) {
			    			  CoreColor.message(player, AllString.prefix +  AllString.pin_code_login);
			    		  }else {
			    			  CoreColor.message(player, AllString.prefix +  AllString.pin_code_user_register);
			    		  }
			    	  }else {
			    		  if(onConnectPlayer.hashPinCode()) {
			    			  CoreColor.message(player, AllString.prefix +  AllString.pin_code_login);
			    		  }else {
			    			  CoreColor.message(player, AllString.prefix + AllString.pin_code_staff_register);
			    		  }  
			    	  }
	    		}else {
	    			CoreColor.message(player, AllString.prefix +AllString.login);
	    		}
	
	    	}else{
	    		CoreColor.message(player, AllString.prefix +AllString.register);
	    	}
	    	return;
	    }
	}
}
