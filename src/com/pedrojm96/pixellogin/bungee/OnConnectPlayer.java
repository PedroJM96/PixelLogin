package com.pedrojm96.pixellogin.bungee;

import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.data.CoreSQL;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;

public class OnConnectPlayer {
	private PixelLoginBungee plugin;
	private ProxiedPlayer proxiedPlayer;
	private ProxiedProfile proxiedProfile;
	
	public OnConnectPlayer(ProxiedPlayer proxiedPlayer,PixelLoginBungee plugin) {
		this.plugin = plugin;
		this.proxiedPlayer = proxiedPlayer;
		this.proxiedProfile = plugin.proxiedProfile.get(proxiedPlayer.getName());
	}
	
	
	public boolean hashPinCode() {
		return proxiedProfile.hashPinCode();	
	}
	
	public boolean isCaptcha_checked() {
		return proxiedProfile.isCaptcha_checked();	
	}
	
	public boolean isLogin() {
		return proxiedProfile.isLogin();	
	}
	
	
	public boolean isRegistered() {
		return proxiedProfile.isRegistered();
	}
	
	
	public boolean isFullLogin() {
		return proxiedProfile.isLogin() && proxiedProfile.isPinLogin();
	}
	
	public boolean isPremiumAutoLogin() {
		return proxiedProfile.isPremiun() && plugin.config.getBoolean("premium-auto-login");
	}
	
	@SuppressWarnings("deprecation")
	public void fullLogin(ServerConnectEvent e) {
		
		proxiedProfile.setLogin(true);
		proxiedProfile.setPinLogin(true);
		plugin.data.update(CoreSQL.WHERE("uuid:"+proxiedPlayer.getUniqueId().toString()), "last_login:"+System.currentTimeMillis(),"ip:"+proxiedPlayer.getAddress().getAddress().getHostAddress());
		if(this.plugin.config.getBoolean("lobby-server.enable")) {
			ServerInfo server = ProxyServer.getInstance().getServerInfo(this.plugin.config.getString("lobby-server.name"));
			if(server==null) {
				proxiedPlayer.disconnect(CoreColor.getColorTextComponent( AllString.prefix + "Lobby server not found"));
			}else {
				e.setTarget(server);
			}
		}
	}
	
	
	public void auth(ServerConnectEvent e) {
		if(this.plugin.config.getBoolean("auth-server.enable")) {
			ServerInfo server = ProxyServer.getInstance().getServerInfo(this.plugin.config.getString("auth-server.name"));
			if(server==null) {
				proxiedPlayer.disconnect(CoreColor.getColorTextComponent( AllString.prefix + "Auth server not found"));
			}else {
				e.setTarget(server);
			}
		}
	}
	
	
	public void login(ServerConnectEvent e) {
		proxiedProfile.setLogin(true);
		plugin.log.debug("ProxiedProfile: setLogin(true)");
		//plugin.data.update(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "last_login:"+System.currentTimeMillis(),"ip:"+player.getAddress().getAddress().getHostAddress());
		if(this.plugin.config.getBoolean("auth-server.enable")) {
			ServerInfo server = ProxyServer.getInstance().getServerInfo(this.plugin.config.getString("auth-server.name"));
			if(server==null) {
				proxiedPlayer.disconnect(CoreColor.getColorTextComponent( AllString.prefix + "Auth server not found"));
				
			}else {
				e.setTarget(server);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean isSessionActive() {
		if(plugin.config.getBoolean("session.enable")) {
			if(proxiedProfile.getSession().isActive(proxiedPlayer.getAddress().getAddress().getHostAddress())) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public boolean isSessionPinActive() {
		if(plugin.config.getBoolean("session.enable") && plugin.config.getBoolean("session.session-pin")) {
			if(proxiedProfile.getSession().isActive(proxiedPlayer.getAddress().getAddress().getHostAddress())) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	public boolean requiresPinCode() {
		if(plugin.config.getBoolean("premium-auto-pin") && proxiedProfile.isPremiun()) {
			return false;
		}else { 
			if(plugin.config.getBoolean("ping-code-all-users")) {
				return true;
			}else if(plugin.config.getBoolean("ping-code-staff")) {
				if(proxiedPlayer.hasPermission("pixellogin.staff")) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}
	}
}
