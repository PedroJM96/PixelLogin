package com.pedrojm96.pixellogin.bungee.commands.subcommands;


import java.util.UUID;

import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.CoreSubCommand;
import com.pedrojm96.core.bungee.data.CoreSQL;
import com.pedrojm96.pixellogin.bungee.AllString;
import com.pedrojm96.pixellogin.bungee.PixelLoginBungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;



public class CrackedPixelLogin extends CoreSubCommand{

	private PixelLoginBungee plugin;
	
	public CrackedPixelLogin(PixelLoginBungee plugin) {
		this.plugin = plugin;
		this.plugin.log.info("Register sub-command /pixellogin cracked");
	}
	
	
	@Override
	public boolean onSubCommand(CommandSender sender, String[] args) {
		
		if(args.length <= 0){
			CoreColor.message(sender, AllString.prefix + AllString.command_pixellogin_cracked_use);
			return true;	
		}
		
		String playername = args[0];
		if(!this.plugin.data.checkData(CoreSQL.WHERE("name:"+playername.toLowerCase()), "name")) {
			CoreColor.message(sender, AllString.prefix + AllString.error_registration_not_found);
			return true;
		}
		UUID playerUUID = plugin.getOfflineUniqueId(playername);
		
		plugin.data.update(CoreSQL.WHERE("name:"+playername.toLowerCase()),"premium:"+0,"uuid:"+playerUUID.toString());
		CoreColor.message(sender, AllString.prefix + AllString.cracked_success);
		if(ProxyServer.getInstance().getPlayer(playername) != null) {
			ProxyServer.getInstance().getPlayer(playername).disconnect(CoreColor.getColorTextComponent(AllString.prefix + AllString.cracked_changed));  
			return true;
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
		return "pixellogin.admin.cracked";
	}

}
