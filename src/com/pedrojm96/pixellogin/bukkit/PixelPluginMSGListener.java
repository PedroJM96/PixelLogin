package com.pedrojm96.pixellogin.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;


public class PixelPluginMSGListener implements PluginMessageListener{
	
	
	private PixelLoginBukkit plugin;
	private ChannelController controller;
	
	
	public PixelPluginMSGListener(PixelLoginBukkit plugin) {
		this.plugin = plugin;
		this.controller = new ChannelController(plugin);
	}
	
	
	@Override
    public  void onPluginMessageReceived(String channel, Player oldplayer, byte[] message) {
		
			
			if ( !channel.equalsIgnoreCase(plugin.CHANNEL))
			{
				return;
			}
		
			ByteArrayDataInput in = ByteStreams.newDataInput(message);
			String channel_player = in.readUTF();
			
			Player player = null;
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (all.getName().equalsIgnoreCase(channel_player))
				{
					player = all;
					break;
				}
			}
			
			if(player==null) {
				return;
			}
	
			String subchannel = in.readUTF();
			String value = in.readUTF();;
			
			plugin.log.debug("[GET][C-"+channel+"][SC-"+subchannel.trim()+"]["+player.getName()+"]");
			
			if(subchannel.trim().equals("player-profile")){
				plugin.log.debug("["+player.getName()+"]Recibiendo datos del profile del jugador del servidor bungee....");
				String json = value;
				this.controller.onLoadProfile(player, json.trim());
	        }
			
			if(subchannel.trim().equals("captcha-profile")){
				plugin.log.debug("["+player.getName()+"]Recibiendo datos del profile temporal para el chaptcha....");
				String json = value;
				this.controller.onCaptchaProfile(player, json.trim());
	        }
			
			if(subchannel.trim().equals("messages")){
				plugin.log.debug("["+player.getName()+"]Recibiendo datos de los mensajes del servidor bungee....");
				String json = value;
				this.controller.onLoadPluginMessage(json);
	        }

			if(subchannel.trim().equals("login")) {
				plugin.log.debug("["+player.getName()+"]Recibiendo el login del jugador en el servidor bungee....");
				String login = value;
				this.controller.onLogin(player, login.trim());
			}
	
			if(subchannel.trim().equals("register")) {
				plugin.log.debug("["+player.getName()+"]Recibiendo el registro del jugador en el servidor bungee....");
				String registered = value;
				this.controller.onPlayerRegistered(player, registered.trim());
			}
			
			if(subchannel.trim().equals("send-to-lobby-world")) {
				plugin.log.debug("["+player.getName()+"]Recibiendo ordenes del servidor bungee para el envio del jugador al mundo lobby si esta configurado....");
				String send_to_lobby_world = value;
				this.controller.onSendToLobbyWorld(player, send_to_lobby_world.trim());
				
			}
			
			if(subchannel.trim().equals("title")) {
				plugin.log.debug("["+player.getName()+"]Recibiendo ordenes del servidor bungee para el envio de titulos al jugador....");
				String title = value;
				this.controller.onSendTitle(player, title.trim());
			}
			
			
			if(subchannel.trim().equals("run-title")) {
				plugin.log.debug("["+player.getName()+"]Recibiendo ordenes del servidor bungee para el envio de titulos repetitivos al jugador....");
				String title = value;
				this.controller.onRunTitle(player, title.trim());
			}
    }
	
}
