package com.pedrojm96.pixellogin.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;


import com.pedrojm96.core.CoreColor;
import com.pedrojm96.core.CoreEncryption;
import com.pedrojm96.core.data.CoreSQL;
import com.pedrojm96.core.effect.CoreTitles;
import com.pedrojm96.pixellogin.bukkit.MessagingManager.MessagingServiceType;




public class PixelBukkitListener implements Listener{

	public PixelLoginBukkit plugin;
	
	public int intento;
	
	
	public PixelBukkitListener(PixelLoginBukkit plugin) {
		this.plugin = plugin;
	}
	

	@EventHandler
	public void onLogin(PlayerLoginEvent e)
	{

		for (Player all : Bukkit.getOnlinePlayers()) {
			if (all.getName().toLowerCase().equalsIgnoreCase(e.getPlayer().getName().toLowerCase()))
			{
				e.disallow(null, AllString.prefix + AllString.already_logged_in);
				break;
			}
		}
	}
	
	@EventHandler
	public void onLogout(PlayerQuitEvent e)
	{
		Player player = e.getPlayer();
	    this.plugin.player_profile.remove(player.getName());
	    this.plugin.gui.remove(player.getName());
	    Integer timerID = (Integer)this.plugin.login_register_timers.remove(player.getName());
	    if (timerID != null) {
	    	Bukkit.getScheduler().cancelTask(timerID.intValue());
	    }
	    
	    Integer pin_loginID = (Integer)this.plugin.pin_login_actionbar_timers.remove(player.getName());
	    if (pin_loginID != null) {
	    	Bukkit.getScheduler().cancelTask(pin_loginID.intValue());
	    }
	    
	    Integer gte_timerID = (Integer)plugin.get_timers.remove(player.getName());
	    if (gte_timerID != null) {
	    	Bukkit.getScheduler().cancelTask(gte_timerID.intValue());
	    }
	    
	    this.plugin.captchas.remove(player.getName());
	    
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e)
	{
		if(!(e.getEntity() instanceof Player)) {
			return;
		}
		
		
		if(!this.plugin.player_profile.containsKey(e.getEntity().getName())) {
			e.setCancelled(true);
			return;
		}
		
		PlayerProfile p = this.plugin.player_profile.get(e.getEntity().getName());
		
	    if (!p.isLogin() || !p.isPin_login()) {
	    	e.setCancelled(true);
	    }
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if(!this.plugin.player_profile.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
			return;
		}
		
		PlayerProfile p = this.plugin.player_profile.get(e.getPlayer().getName());
		
	    if (!p.isLogin()) {
	    	e.setCancelled(true);
	    	return;
	    }
	    if(!p.isPin_login()) {
	    	e.setCancelled(true);
	    	CoreColor.message(e.getPlayer(), AllString.prefix +AllString.pin_code_login);
	    }
	    
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		if(!this.plugin.player_profile.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
			return;
		}
		
		PlayerProfile p = this.plugin.player_profile.get(e.getPlayer().getName());
		
	    if (!p.isLogin()) {
	    	e.setCancelled(true);
	    	return;
	    }
	    if(!p.isPin_login()) {
	    	e.setCancelled(true);
	    	CoreColor.message(e.getPlayer(), AllString.prefix +AllString.pin_code_login);
	    }
	}
	

	/*@EventHandler(priority = EventPriority.HIGH)
	public void onMapInitilize(MapInitializeEvent event) {
		plugin.log.debug("MapInitializeEvent");
		MapView map = event.getMap();
	    List<MapRenderer> old = map.getRenderers();
	    map.setScale(MapView.Scale.NORMAL);
	    map.getRenderers().forEach(map::removeRenderer);
	    map.addRenderer(new CaptchaRender(this.plugin,old,map));
	}*/
	
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e)
	{
		
		if(!this.plugin.player_profile.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
			return;
		}
		
		PlayerProfile p = this.plugin.player_profile.get(e.getPlayer().getName());
		
	    if (!p.isLogin() || !p.isPin_login()) {
	    	e.setCancelled(true);
	    }
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		
		if(!this.plugin.player_profile.containsKey(e.getPlayer().getName())) {
			Location to = e.getFrom();
	    	to.setPitch(e.getTo().getPitch());
	    	e.setTo(to);
			return;
		}
		
		if(this.plugin.player_profile.get(e.getPlayer().getName()) !=null ) {
			PlayerProfile p = this.plugin.player_profile.get(e.getPlayer().getName());
			
		    if (!p.isLogin()) {
		    	Location to = e.getFrom();
		    	to.setPitch(e.getTo().getPitch());
		    	e.setTo(to);
		    }
		}
		
		
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		
		if(this.plugin.captchas.containsKey(e.getPlayer().getName())) {
			
			Captcha captcha = this.plugin.captchas.get(e.getPlayer().getName());
			
			plugin.log.info("Mesaje: "+e.getMessage());
			plugin.log.info("Captcha: "+captcha.getAnswer());
			
			if(!e.getMessage().equalsIgnoreCase(captcha.getAnswer())) {
				CoreColor.message(e.getPlayer(), AllString.prefix +AllString.no_captcha_code);
				e.setCancelled(true);
				return;
			}
			Player player = e.getPlayer();
			String adresse = player.getAddress().getAddress().getHostAddress();
			String token =  CoreEncryption.generateRandomText(64);
			if(plugin.data.checkData(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "uuid")) {
				plugin.data.update( CoreSQL.WHERE("uuid:"+player.getUniqueId().toString())    ,"name:"+player.getName().toLowerCase(),"ip:"+adresse,"password:none","pincode:none","premium:-1","hash:none","registered:"+0,"login:"+0,"pin_login:"+0,"token:"+CoreEncryption.MD5.encrypt(token),"first_login:"+-1,"last_login:"+-1,"last_disconnected:"+-1);
			}else {
				plugin.data.insert("uuid:"+player.getUniqueId().toString(), "name:"+player.getName().toLowerCase(),"ip:"+adresse,"password:none","pincode:none","premium:-1","hash:none","registered:"+0,"login:"+0,"pin_login:"+0,"token:"+CoreEncryption.MD5.encrypt(token),"first_login:"+-1,"last_login:"+-1,"last_disconnected:"+-1);
			}

			captcha.resetInventory();
			
			plugin.captchas.remove(e.getPlayer().getName());
			
			this.plugin.messagingManager.sendToBungeeCord(player, "captcha-checked", token);
			if (plugin.sound_pin_menu_success != null) {
		    	player.playSound(player.getLocation(), plugin.captcha_checked, 1.0F, 1.0F);	
		    }
			CoreTitles.sendTitles(e.getPlayer(), AllString.captcha_checked_title, AllString.captcha_checked_subtitle);
			e.setCancelled(true);
			return;
		}
		
		
		
		if(!this.plugin.player_profile.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
			return;
		}
		
		PlayerProfile p = this.plugin.player_profile.get(e.getPlayer().getName());
		
	    if (!p.isLogin()) {
	    	e.setCancelled(true);
	    	if (p.isRegistered())
	    	{
	    		CoreColor.message(e.getPlayer(), AllString.prefix +AllString.login);
	    	}
	    	else
	    	{
	    		CoreColor.message(e.getPlayer(), AllString.prefix +AllString.register);
	    	}
	    	return;
	    }
	    if(!p.isPin_login()) {
	    	e.setCancelled(true);
	    	CoreColor.message(e.getPlayer(), AllString.prefix +AllString.pin_code_login);
	    }
	}
	
	@EventHandler
	public void onInventoryClik(InventoryClickEvent e)
	{
		Player player = (Player) e.getWhoClicked();
		if(!this.plugin.player_profile.containsKey(player.getName())) {
			e.setCancelled(true);
			return;
		}
		
		PlayerProfile p = this.plugin.player_profile.get(player.getName());
		if (!p.isLogin()) {
			e.setCancelled(true);
			e.getView().close();
			return;
	    }
		if(e.getView().getTitle().equals(CoreColor.colorCodes(AllString.pin_code_menu))) {
			e.setCancelled(true);
			SimpleGUI gui = plugin.gui.get(player.getName());
			
			/**
			 * Numero 0
			 */
			if(e.getSlot() == 20 && gui.isEnableKey() ) {
				gui.addPin(0);
				gui.update();
				if (plugin.sound_pin_menu != null) {
			    	player.playSound(player.getLocation(), plugin.sound_pin_menu, 1.0F, 1.0F);	
			    }
				return;
			}
			/**
			 * Numero 1
			 */
			if(e.getSlot() == 21 && gui.isEnableKey() ) {
				gui.addPin(1);
				gui.update();
				if (plugin.sound_pin_menu != null) {
			    	player.playSound(player.getLocation(), plugin.sound_pin_menu, 1.0F, 1.0F);	
			    }
				return;
			}
			/**
			 * Numero 2
			 */
			if(e.getSlot() == 22 && gui.isEnableKey() ) {
				gui.addPin(2);
				gui.update();
				if (plugin.sound_pin_menu != null) {
			    	player.playSound(player.getLocation(), plugin.sound_pin_menu, 1.0F, 1.0F);	
			    }
				return;
			}
			/**
			 * Numero 3
			 */
			if(e.getSlot() == 23 && gui.isEnableKey() ) {
				gui.addPin(3);
				gui.update();
				if (plugin.sound_pin_menu != null) {
			    	player.playSound(player.getLocation(), plugin.sound_pin_menu, 1.0F, 1.0F);	
			    }
				return;
			}
			/**
			 * Numero 4
			 */
			if(e.getSlot() == 24 && gui.isEnableKey() ) {
				gui.addPin(4);
				gui.update();
				if (plugin.sound_pin_menu != null) {
			    	player.playSound(player.getLocation(), plugin.sound_pin_menu, 1.0F, 1.0F);	
			    }
				return;
			}
			/**
			 * Numero 5
			 */
			if(e.getSlot() == 29 && gui.isEnableKey() ) {
				gui.addPin(5);
				gui.update();
				if (plugin.sound_pin_menu != null) {
			    	player.playSound(player.getLocation(), plugin.sound_pin_menu, 1.0F, 1.0F);	
			    }
				return;
			}
			/**
			 * Numero 6
			 */
			if(e.getSlot() == 30 && gui.isEnableKey() ) {
				gui.addPin(6);
				gui.update();
				if (plugin.sound_pin_menu != null) {
			    	player.playSound(player.getLocation(), plugin.sound_pin_menu, 1.0F, 1.0F);	
			    }
				return;
			}
			/**
			 * Numero 7
			 */
			if(e.getSlot() == 31 && gui.isEnableKey() ) {
				gui.addPin(7);
				gui.update();
				if (plugin.sound_pin_menu != null) {
			    	player.playSound(player.getLocation(), plugin.sound_pin_menu, 1.0F, 1.0F);	
			    }
				return;
			}
			/**
			 * Numero 8
			 */
			if(e.getSlot() == 32 && gui.isEnableKey() ) {
				gui.addPin(8);
				gui.update();
				if (plugin.sound_pin_menu != null) {
			    	player.playSound(player.getLocation(), plugin.sound_pin_menu, 1.0F, 1.0F);	
			    }
				return;
			}
			/**
			 * Numero 9
			 */
			if(e.getSlot() == 33 && gui.isEnableKey() ) {
				gui.addPin(9);
				gui.update();
				if (plugin.sound_pin_menu != null) {
			    	player.playSound(player.getLocation(), plugin.sound_pin_menu, 1.0F, 1.0F);	
			    }
				return;
			}
			/**
			 * Backspace
			 */
			if(e.getSlot() == 48 ) {
				gui.remPin();
				gui.update();
				if (plugin.sound_pin_menu != null) {
			    	player.playSound(player.getLocation(), plugin.sound_pin_menu, 1.0F, 1.0F);	
			    }
				return;
			}
			/**
			 * OK
			 */
			if(e.getSlot() == 50 ) {
				
				
				e.getView().close();
				if(p.hashPinCode()) {
					String hashPin = CoreEncryption.valueOf(p.getHash()).encrypt(gui.getPinCode());
					if(p.getPinCode().equals(hashPin)) {
						CoreColor.message((Player)e.getWhoClicked(),AllString.prefix + AllString.pin_code_login_success);
						p.setPin_login(true);
						this.plugin.messagingManager.sendToBungeeCord((Player)e.getWhoClicked(), "pin-login", String.valueOf(true));
						plugin.data.update(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "pin_login:1","last_login:"+System.currentTimeMillis(),"ip:"+player.getAddress().getAddress().getHostAddress());
						if (plugin.sound_pin_menu_success != null) {
					    	player.playSound(player.getLocation(), plugin.sound_pin_menu_success, 1.0F, 1.0F);	
					    }
						
						Integer timerID = (Integer)this.plugin.pin_login_actionbar_timers.remove(player.getName());
					    if (timerID != null) {
					    	Bukkit.getScheduler().cancelTask(timerID.intValue());
					    }
						
						if(this.plugin.config.getBoolean("lobby-world.enable")) {
					    	new BukkitRunnable()
						    {
					    		@Override
								public void run()
					    		{
					    			if(player != null && player.isOnline()) {
					    				World w = Bukkit.getWorld(plugin.config.getString("lobby-world.name"));
						    			if(w!=null) {
						    				e.getWhoClicked().teleport(w.getSpawnLocation());
						    			}
					    			}
					    		}
						    }.runTaskLater(this.plugin, 40L);
					    }
					}else {
						CoreColor.message((Player)e.getWhoClicked(),AllString.prefix + AllString.pin_code_failed);
						gui.reset();
						gui.open((Player)e.getWhoClicked());
						if (plugin.sound_pin_menu_failed != null) {
					    	player.playSound(player.getLocation(), plugin.sound_pin_menu_failed, 1.0F, 1.0F);	
					    }
					}
				}else {
					if(gui.getPinSize() < 1) {
						CoreColor.message((Player)e.getWhoClicked(),AllString.prefix + AllString.pin_code_null);
						if (plugin.sound_pin_menu_failed != null) {
					    	player.playSound(player.getLocation(), plugin.sound_pin_menu_failed, 1.0F, 1.0F);	
					    }
					}else {
						String hashPin = CoreEncryption.valueOf(p.getHash()).encrypt(gui.getPinCode());
						
						plugin.data.update(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "pincode:"+hashPin,"pin_login:1","last_login:"+System.currentTimeMillis(),"ip:"+player.getAddress().getAddress().getHostAddress());
						
						CoreColor.message((Player)e.getWhoClicked(),AllString.prefix + AllString.pin_code_register_success);
						p.setPin_login(true);
						p.setPingCode(hashPin);
						this.plugin.messagingManager.sendToBungeeCord((Player)e.getWhoClicked(), "pin-login", String.valueOf(true));
						if (plugin.sound_pin_menu_success != null) {
					    	player.playSound(player.getLocation(), plugin.sound_pin_menu_success, 1.0F, 1.0F);	
					    }
						
						Integer timerID = (Integer)this.plugin.pin_login_actionbar_timers.remove(player.getName());
					    if (timerID != null) {
					    	Bukkit.getScheduler().cancelTask(timerID.intValue());
					    }
						
						
						if(this.plugin.config.getBoolean("lobby-world.enable")) {
					    	new BukkitRunnable()
						    {
					    		@Override
								public void run()
					    		{
					    			if(player != null && player.isOnline()) {
					    				World w = Bukkit.getWorld(plugin.config.getString("lobby-world.name"));
						    			if(w!=null) {
						    				e.getWhoClicked().teleport(w.getSpawnLocation());
						    			}
					    			}	
					    		}
						    }.runTaskLater(this.plugin, 40L);
					    }
					}
				}
				return;
			}
		}	
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e)
	{
		if(!this.plugin.player_profile.containsKey(e.getWhoClicked().getName())) {
			e.setCancelled(true);
			e.getView().close();
			return;
		}
		
		
		PlayerProfile p = this.plugin.player_profile.get(e.getWhoClicked().getName());
		if (!p.isLogin() || !p.isPin_login()) {
			e.setCancelled(true);
			e.getView().close();
	    }
	}
	
	@EventHandler
	public void onInventoryDrop(PlayerDropItemEvent e)
	{
		
		if(!this.plugin.player_profile.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
			return;
		}
		
		PlayerProfile p = this.plugin.player_profile.get(e.getPlayer().getName());
	    if (!p.isLogin() || !p.isPin_login()) {
	    	e.setCancelled(true);
	    }
	}
	
	@EventHandler
	public void onBlockCmds(PlayerCommandPreprocessEvent e)
	{
		
		if(!this.plugin.player_profile.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
			return;
		}
		
		PlayerProfile p = this.plugin.player_profile.get(e.getPlayer().getName());
	    if (!p.isLogin()) {
	    	if ((e.getMessage().toLowerCase().startsWith("/login")) || (e.getMessage().toLowerCase().startsWith("/register"))) {
	    		e.setCancelled(false);
	    	} else {
	    		if(p.isRegistered()) {
	    			CoreColor.message(e.getPlayer(),AllString.prefix + AllString.command_register_use);
	    		}else{
	    			CoreColor.message(e.getPlayer(),AllString.prefix + AllString.command_login_use);
	    		}
	    		e.setCancelled(true);
	    		return;
	    	}
	    }
	    
	    if(!p.isPin_login() && !e.getMessage().toLowerCase().startsWith("/login") && !e.getMessage().toLowerCase().startsWith("/register") && !e.getMessage().toLowerCase().startsWith("/pin")) {
	    	e.setCancelled(true);
	    	CoreColor.message(e.getPlayer(), AllString.prefix +AllString.pin_code_login);
	    	
	    }
	}
	
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e) {

	    if(this.plugin.captchas.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		
		
		
		
		if(!this.plugin.player_profile.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
			return;
		}
		
		
		PlayerProfile p = this.plugin.player_profile.get(e.getPlayer().getName());
	    
		if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) && (!p.isLogin())) {
	    	e.setCancelled(true);
	    	return;
	    }
		
		
		if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_AIR)) && (!p.isPin_login())) {
	    	e.setCancelled(true);
	    }
		
		if (((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) && (!p.isPin_login())) {
	    	e.setCancelled(true);
	    	CoreColor.message(e.getPlayer(), AllString.prefix +AllString.pin_code_login);
	    }
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onJoinClear(PlayerJoinEvent e)
	{
		if(this.plugin.config.getBoolean("join-clear-chat")) {
			Player p = e.getPlayer();
			for (int i = 0; i < 120; i++) {		  
				p.sendMessage("");
			}
		}
	}
	
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onGetMessages(PlayerJoinEvent e)
	{
		Player player = e.getPlayer();
		
		long start = 1;
		if(plugin.messagingManager.messaging_service.equals(MessagingServiceType.pluginmsg)) {
			start = 20L;
		}
		
		
		
	    this.plugin.get_timers.put(e.getPlayer().getName(), Bukkit.getScheduler().runTaskTimer(plugin, new Runnable()
	    {
	        public void run()
	        {
	        	if(!plugin.player_profile.containsKey(e.getPlayer().getName())) {
	        		plugin.log.debug("["+player.getName()+"]Solicitando datos del perfil del jugador....");
		    		plugin.messagingManager.sendToBungeeCord(player, "get-profile", String.valueOf(true));
	        	}else {

	        	}

	    		if(!plugin.messages) {
	    			plugin.log.debug("["+player.getName()+"]Solicitando para los menssages....");
    				plugin.messagingManager.sendToBungeeCord(player, "get-messages", String.valueOf(true));
	    		}
	        }
	    }, start,20L).getTaskId());
		;
	}
	  
	
}
