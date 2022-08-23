package com.pedrojm96.pixellogin.bungee.commands.subcommands;


import java.util.UUID;

import com.pedrojm96.core.CoreEncryption;
import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.CoreSubCommand;
import com.pedrojm96.core.bungee.data.CoreSQL;
import com.pedrojm96.pixellogin.bungee.AllString;
import com.pedrojm96.pixellogin.bungee.MojangProfile;
import com.pedrojm96.pixellogin.bungee.PixelLoginBungee;


import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;



public class RegisterPixelLogin extends CoreSubCommand{

	private PixelLoginBungee plugin;
	
	public RegisterPixelLogin(PixelLoginBungee plugin) {
		this.plugin = plugin;
		this.plugin.log.info("Register sub-command /pixellogin register");
	}
	
	
	@Override
	public boolean onSubCommand(CommandSender sender, String[] args) {
		
		if(args.length <= 1){
			CoreColor.message(sender, AllString.prefix + AllString.command_pixellogin_register_use);
			return true;	
		}
		
		String playername = args[0];
		String password = args[1];
		if(!playername.matches(AllString.join_regex)) {
			CoreColor.message(sender, AllString.prefix + AllString.player_invalid_name);
			return true;
		}
		
		if(ProxyServer.getInstance().getPlayer(playername) != null) {
			CoreColor.message(sender, AllString.prefix + AllString.already_logged_in);
			return true;
		}
		
		if(this.plugin.data.checkData(CoreSQL.WHERE("name:"+playername.toLowerCase()), "name")) {
			CoreColor.message(sender, AllString.prefix + AllString.error_player_already_registered);
			return true;
		}
		
		MojangProfile profile = new MojangProfile("https://api.mojang.com/users/profiles/minecraft/" + playername,plugin.log);
		profile.run();
		if(profile.error()) {
			CoreColor.message(sender, AllString.prefix + AllString.error_mojangapi_shutdown);
			return true;
		}
		String token =  CoreEncryption.generateRandomText(64);
		if(profile.isPremiun()) {
			UUID playerUUID = profile.getUuid();
			if(this.plugin.data.checkData(CoreSQL.WHERE("uuid:"+playerUUID.toString()), "uuid")) {
				CoreColor.message(sender, AllString.prefix + AllString.error_player_already_registered);
				return true;
			}
			String hashPassword = CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).encrypt(password);
	        plugin.data.insert("uuid:"+playerUUID.toString(), "name:"+playername.toLowerCase(),"ip:admin","password:"+hashPassword,"pincode:none","premium:"+1,"hash:"+CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).toString(),"registered:"+1,"login:"+0,"pin_login:"+0,"token:"+CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).encrypt(token),"first_login:"+System.currentTimeMillis(),"last_login:"+System.currentTimeMillis(),"last_disconnected:"+System.currentTimeMillis());
	        CoreColor.message(sender, AllString.prefix + AllString.register_success);
		}else {
			UUID playerUUID = plugin.getOfflineUniqueId(playername);
			String hashPassword = CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).encrypt(password);
	        plugin.data.insert("uuid:"+playerUUID.toString(), "name:"+playername.toLowerCase(),"ip:admin","password:"+hashPassword,"pincode:none","premium:"+0,"hash:"+CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).toString(),"registered:"+1,"login:"+0,"pin_login:"+0,"token:"+CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).encrypt(token),"first_login:"+System.currentTimeMillis(),"last_login:"+System.currentTimeMillis(),"last_disconnected:"+System.currentTimeMillis());
	        CoreColor.message(sender, AllString.prefix + AllString.register_success);
		}
		return true;
	}

	@Override
	public String getErrorNoPermission() {
		// TODO Auto-generated method stub
		return AllString.prefix + AllString.error_no_permission;
	}

	@Override
	public String getPerm() {
		// TODO Auto-generated method stub
		return "pixellogin.admin.register";
	}

}
