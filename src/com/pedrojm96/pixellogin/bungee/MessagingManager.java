package com.pedrojm96.pixellogin.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonObject;
import com.pedrojm96.core.bungee.RabbitmqMessagingService;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MessagingManager {
	
	

	public final String CHANNEL = "pixellogin:update";
	
	public enum MessagingServiceType {
		pluginmsg, rabbitmq
	}
	
	public MessagingServiceType messaging_service;
	
	public RabbitmqMessagingService messageService;
	
	public PixelLoginBungee plugin;
	
	
	
	
	public MessagingManager(PixelLoginBungee plugin) {
		this.plugin = plugin;
		switch(plugin.config.getString("messaging-service").toLowerCase()){
		case "pluginmsg":
			messaging_service = MessagingServiceType.pluginmsg;		
			break;
		case "rabbitmq":
			messaging_service = MessagingServiceType.rabbitmq;
			break;
		default:
			messaging_service = MessagingServiceType.pluginmsg;
			break;
		}
	}
	
	
	public boolean load() {
		if(messaging_service.equals(MessagingServiceType.rabbitmq)) {
			
			
			if(!plugin.config.getBoolean("rabbitmq.enable")) {
				plugin.log.fatalError("Plugin disable, rabbitmq server is require, pleace set 'rabbitmq' in config.yml");
				plugin.log.line();
				this.plugin.getProxy().getPluginManager().unregisterListeners(this.plugin);
				this.plugin.getProxy().getPluginManager().unregisterCommands(this.plugin);
				return false;
			}
			
			messageService = new RabbitmqMessagingService(this.plugin.getLog(), "pixellogin",plugin.config.getString("rabbitmq.host"),plugin.config.getString("rabbitmq.virtual-host"),plugin.config.getInt("rabbitmq.port"),plugin.config.getString("rabbitmq.username"),plugin.config.getString("rabbitmq.password"));
			messageService.join("bungee",new BungeeDeliverCallback(plugin)); 
		}else {
			plugin.getProxy().registerChannel(CHANNEL);
			plugin.getProxy().getPluginManager().registerListener(plugin, new PixelPluginMSGListener(plugin));
		}
		return true;
	}
	
	
	public void closed() {
		if(messaging_service.equals(MessagingServiceType.rabbitmq)) {
			messageService.close();
		}else {
			this.plugin.getProxy().unregisterChannel(CHANNEL);
		}
		
		
	}
	
	
	public void sendToBukkit(ProxiedPlayer player, String subchannel, String message, ServerInfo server) {
		if(messaging_service.equals(MessagingServiceType.rabbitmq)) {
			ProxiedProfile plogin = plugin.proxiedProfile.get(player.getName());
			JsonObject maintObject = new JsonObject();
			maintObject.addProperty("type", subchannel);
			maintObject.addProperty("player", player.getName());
			if(subchannel.equalsIgnoreCase("login") || subchannel.equalsIgnoreCase("register")) {
				maintObject.addProperty("pinlogin", plogin.isPinLogin());	
				
			}else {
				maintObject.addProperty(subchannel, message);
			}
			messageService.send(server.getName().toLowerCase(), maintObject);
		}else {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF(player.getName());
	        out.writeUTF(subchannel);
	        out.writeUTF(message);
	        /** Canal PxelLogin*/
	        plugin.log.debug("[SEND][C-PxelLogin][SC-"+subchannel+"]");
	        server.sendData(CHANNEL, out.toByteArray());
		}
    }
	
}
