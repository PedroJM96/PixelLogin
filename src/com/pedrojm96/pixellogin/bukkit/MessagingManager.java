package com.pedrojm96.pixellogin.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonObject;
import com.pedrojm96.core.RabbitmqMessagingService;




public class MessagingManager {
	
	

	public final String CHANNEL = "pixellogin:update";
	
	public enum MessagingServiceType {
		pluginmsg, rabbitmq
	}
	
	public MessagingServiceType messaging_service;
	
	public RabbitmqMessagingService messageService;
	
	public PixelLoginBukkit plugin;
	
	public String server_name;
	
	public MessagingManager(PixelLoginBukkit plugin) {
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
	
	
	public void load() {
		if(messaging_service.equals(MessagingServiceType.rabbitmq)) {
			server_name = plugin.config.getString("server-name");
			if(server_name.equalsIgnoreCase("none")){
				plugin.log.fatalError("Plugin disable, Server name is require in the rabbitmq messaging-service, pleace set 'server-name' in config.yml");
				plugin.log.line();
				plugin.getServer().getPluginManager().disablePlugin(plugin);
				return;
			}
			
			if(!plugin.config.getBoolean("rabbitmq.enable")) {
				plugin.log.fatalError("Plugin disable, rabbitmq server is require, pleace set 'rabbitmq' in config.yml");
				plugin.log.line();
				plugin.getServer().getPluginManager().disablePlugin(plugin);
				return;
			}
			messageService = new RabbitmqMessagingService(this.plugin.getLog(),"pixellogin",plugin.config.getString("rabbitmq.host"),plugin.config.getString("rabbitmq.virtual-host"),plugin.config.getInt("rabbitmq.port"),plugin.config.getString("rabbitmq.username"),plugin.config.getString("rabbitmq.password"));
			messageService.join(server_name.toLowerCase(),new BukkitDeliverCallback(plugin));
			
		}else {
			
			if(plugin.bungeeCord) {
				plugin.log.info("Registering channel PxelLogin");
				Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL);
				Bukkit.getMessenger().registerIncomingPluginChannel(plugin, CHANNEL,  new PixelPluginMSGListener(plugin));
			}else {
				plugin.log.error("This Plugin work only on the spigot server with bungeecod set to true, please enabled bungeecord mode.");
				plugin.getServer().getPluginManager().disablePlugin(plugin);   
				return;
			}
		}
	}
	
	
	public void closed() {
		if(messaging_service.equals(MessagingServiceType.rabbitmq)) {
			messageService.close();
		}else {
			this.plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, CHANNEL);
	        this.plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, CHANNEL);
		}
		
		
	}
	
	public void sendToBungeeCord(Player p, String subchannel,String mensaje){
    	
		
		if(messaging_service.equals(MessagingServiceType.rabbitmq)) {
			JsonObject maintObject = new JsonObject();
			maintObject.addProperty("type", subchannel);
			maintObject.addProperty("server", server_name);
			maintObject.addProperty("player", p.getName());
			messageService.send("bungee", maintObject);
		}else {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
	    	out.writeUTF(subchannel);
			out.writeUTF(mensaje);
				
			/** Canal PxelLogin*/
			p.sendPluginMessage(plugin, CHANNEL, out.toByteArray());
			plugin.log.debug("[SEND][C-"+CHANNEL+"][SC-"+subchannel+"]");
		}
		
		
		
		
    }

	
}
