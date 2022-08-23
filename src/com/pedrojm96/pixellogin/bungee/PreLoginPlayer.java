package com.pedrojm96.pixellogin.bungee;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.pedrojm96.core.bungee.data.CoreSQL;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;

public class PreLoginPlayer {
	
	private PendingConnection pendingConnection;
	private PixelLoginBungee plugin;
	
	public PreLoginPlayer(PendingConnection pendingConnection,PixelLoginBungee plugin) {
		this.pendingConnection = pendingConnection;
		this.plugin = plugin;
	}
	
	private boolean premiun;
	
	public boolean checkValitName() {
		return pendingConnection.getName().matches(AllString.join_regex);
	}
	
	public boolean alreadyJoin() {
		return (ProxyServer.getInstance().getPlayer(pendingConnection.getName()) != null);
	}
	
	
	
	public boolean isMaxIP() {
		List<HashMap<String, String>> playersips = plugin.data.getAll(CoreSQL.WHERE("ip:"+pendingConnection.getAddress().getAddress().getHostAddress()), "order by ip DESC limit 20", "name","uuid");
		int ips = 0;
		boolean nameregisterip = false;
		if(playersips != null) {
			for(HashMap<String, String> playerip : playersips) {
				if(pendingConnection.getName().toLowerCase().equalsIgnoreCase(playerip.get("name"))) {
					nameregisterip = true;
					continue;
				}else {
					ips++;
				}
			}
		}
		
		return ( (ips+1)>plugin.config.getInt("max-ip-accounts") && !nameregisterip );
	}
	
	
	public boolean isRegisterDatabase() {
		return this.plugin.data.checkData(CoreSQL.WHERE("name:"+pendingConnection.getName().toLowerCase(),"registered:"+1  ), "name");
	}
	
	
	public boolean checkPremiunNameChnage(MojangProfile profile) {
		return this.plugin.data.checkData(CoreSQL.WHERE("uuid:"+profile.getUuid().toString() ,"registered:"+1  ), "uuid");
	}
	
	
	public boolean isPremiunProfile() {
		return this.premiun;
	}
	
	public void loadNewCrackeProfile() {
		this.premiun = false;
		plugin.log.debug("* ["+pendingConnection.getName()+"] - El jugador no es premiun, se registran datos temporales del nuevo jugador no premiun...");
		UUID playerUUID = plugin.getOfflineUniqueId(pendingConnection.getName());
		premiun = false;
		ProxiedProfile proxiedProfile = new ProxiedProfile(playerUUID,false,plugin);
		proxiedProfile.setPremiun(premiun);
		proxiedProfile.setHash(plugin.config.getString("encryption-method").toUpperCase());
		proxiedProfile.setIp(pendingConnection.getAddress().getAddress().getHostAddress());
		plugin.proxiedProfile.put(pendingConnection.getName(), proxiedProfile);
		plugin.log.debug("* ["+pendingConnection.getName()+"] - ProxiedProfile creado a travez de los datos del jugador no premiun nuevo.");
		plugin.log.info("["+pendingConnection.getName()+"]"+(premiun?"[PREMIUM]":"[NO-PREMIUM]")+" "+playerUUID.toString());
	}
	
	public void loadNewPremiunProfile(MojangProfile profile) {
		this.premiun = profile.isPremiun();
		plugin.log.debug("* ["+pendingConnection.getName()+"] - No esta registrado con otro nombre, es un jugador premiun nuevo, se registran los dato temporales del nuevo jugador...");
		ProxiedProfile proxiedProfile = new ProxiedProfile(profile.getUuid(),false,plugin);
		proxiedProfile.setPremiun(profile.isPremiun());
		proxiedProfile.setHash(plugin.config.getString("encryption-method").toUpperCase());
		proxiedProfile.setIp(pendingConnection.getAddress().getAddress().getHostAddress());
		plugin.proxiedProfile.put(pendingConnection.getName(), proxiedProfile);
		plugin.log.debug("* ["+pendingConnection.getName()+"] - ProxiedProfile creado a travez de los datos del jugador premiun nuevo.");
		plugin.log.info("["+pendingConnection.getName()+"]"+(profile.isPremiun()?"[PREMIUM]":"[NO-PREMIUM]")+" "+profile.getUuid().toString());
	}
	
	public void loadNameChangeDatabaseProfile(MojangProfile profile) {
		this.premiun = profile.isPremiun();
		plugin.log.debug("* ["+pendingConnection.getName()+"] - Si esta registrado con otro nombre, se actualiza sus datos al nombre actual y se cargan los datos...");
		HashMap<String, String> data = this.plugin.data.get(CoreSQL.WHERE("uuid:"+profile.getUuid().toString()), "premium","password","pincode","name","hash","last_login","ip","last_disconnected");
		String oldName = data.get("name");
		this.plugin.data.update(CoreSQL.WHERE("uuid:"+profile.getUuid().toString()), "name:"+pendingConnection.getName().toLowerCase());
		ProxiedProfile proxiedProfile = new ProxiedProfile(profile.getUuid(),true,plugin);
		proxiedProfile.setPremiun(profile.isPremiun());
		proxiedProfile.setCaptcha_checked(true);
		proxiedProfile.setPassword(data.get("password"));
		proxiedProfile.setPincode(data.get("pincode"));
		proxiedProfile.setHash(data.get("hash"));
		proxiedProfile.setIp(data.get("ip"));
		proxiedProfile.setLast_disconnected(Long.valueOf(data.get("last_disconnected")));
		proxiedProfile.setLast_login(Long.valueOf(data.get("last_login")));
		plugin.log.debug("* ["+pendingConnection.getName()+"] - ProxiedProfile creado a travez de la base de datos cambiando el nombre de usuario por el actual.");
		plugin.proxiedProfile.put(pendingConnection.getName(), proxiedProfile);	
		plugin.log.info("["+pendingConnection.getName()+"]"+(profile.isPremiun()?"[PREMIUM]":"[NO-PREMIUM]")+" "+profile.getUuid().toString()+"[NAME-CHANGE] ["+oldName+"]");
	}
	
	
	
	public void  loadDatabaseProfile() {
		plugin.log.debug("* ["+pendingConnection.getName()+"] - Si esta registrado se procede a cargar todos sus datos...");
		HashMap<String, String> data = this.plugin.data.get(CoreSQL.WHERE("name:"+pendingConnection.getName().toLowerCase()), "uuid","premium","password","pincode","hash","last_login","ip","last_disconnected");
		UUID playerUUID = UUID.fromString(data.get("uuid"));
		this.premiun = (Integer.valueOf(data.get("premium"))==1?true:false) ;
		ProxiedProfile proxiedProfile = new ProxiedProfile(playerUUID,true,plugin);
		proxiedProfile.setPremiun(this.premiun);
		proxiedProfile.setCaptcha_checked(true);
		proxiedProfile.setPassword(data.get("password"));
		proxiedProfile.setPincode(data.get("pincode"));
		proxiedProfile.setHash(data.get("hash"));
		proxiedProfile.setIp(data.get("ip"));
		proxiedProfile.setLast_disconnected(Long.valueOf(data.get("last_disconnected")));
		proxiedProfile.setLast_login(Long.valueOf(data.get("last_login")));
		plugin.log.debug("* ["+pendingConnection.getName()+"] - ProxiedProfile creado a travez de los datos de la base de datos.");
		plugin.proxiedProfile.put(pendingConnection.getName(), proxiedProfile);
		plugin.log.info("["+pendingConnection.getName()+"]"+(premiun?"[PREMIUM]":"[NO-PREMIUM]")+" "+playerUUID.toString());
	}

}
