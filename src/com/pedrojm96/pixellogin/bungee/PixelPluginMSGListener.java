package com.pedrojm96.pixellogin.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;


import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PixelPluginMSGListener implements Listener{
	private PixelLoginBungee plugin;
	
	private ChannelController controller;
	
	public PixelPluginMSGListener(PixelLoginBungee plugin) {
		this.plugin = plugin;
		this.controller = new ChannelController(plugin);
	}
	
	
	@EventHandler
    public void onPluginMessage(PluginMessageEvent e){
		
		/** Canal PxelLogin*/
		if (e.getTag().equalsIgnoreCase(plugin.messagingManager.CHANNEL)) {
			ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
			String subchannel = in.readUTF();
			String value = in.readUTF();
			
			
			//plugin.log.debug("[GET][C-"+e.getTag()+"][SC-"+subchannel+"]["+e.getReceiver().toString()+"]");

			if (!(e.getReceiver() instanceof ProxiedPlayer) ) {
				return;
			}
			
			ProxiedPlayer player = plugin.getProxy().getPlayer(e.getReceiver().toString());
			plugin.log.debug("*** ["+player.getName()+"] - PluginMessageEvent.");
			
			if(subchannel.trim().equals("get-profile")){
				plugin.log.debug("*** ["+player.getName()+"] - Solicitud de los datos de Profile recividad, enviando datos al servidor de bukkit...");
				String get_profile = value;
				this.controller.onGetProfile(player, get_profile.trim());
            }
			
			
			if(subchannel.trim().equals("pin-login")){
				plugin.log.debug("*** ["+player.getName()+"] - El jugador coloco el pin corretamente en el servidor bukkit.");
				String pin_login = value;
				this.controller.onPinLogin(player, pin_login.trim());;
            }
			
			if(subchannel.trim().equals("captcha-checked")){
				plugin.log.debug("*** ["+player.getName()+"] - El jugador coloco el captcha corretamente en el servidor bukkit.");
				String token = value;
				this.controller.onCaptchaChecked(player, token.trim());;
            }
			

			if(subchannel.trim().equals("get-messages")){
				plugin.log.debug("*** ["+player.getName()+"] - Solicitud de mensajes revividad, enviando mensajes al servidor bukkit....");
				String get_messages = value;
				this.controller.onGetMessages(player, get_messages.trim());
            }
		
		}
	
    }
}
