package com.pedrojm96.pixellogin.bungee.commands.subcommands;


import java.util.UUID;

import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.CoreSubCommand;
import com.pedrojm96.core.bungee.data.CoreSQL;
import com.pedrojm96.pixellogin.bungee.AllString;
import com.pedrojm96.pixellogin.bungee.MojangProfile;
import com.pedrojm96.pixellogin.bungee.PixelLoginBungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;



public class PremiumPixelLogin extends CoreSubCommand{

	private PixelLoginBungee plugin;
	
	public PremiumPixelLogin(PixelLoginBungee plugin) {
		this.plugin = plugin;
		this.plugin.log.info("Register sub-command /pixellogin premium");
	}
	
	
	@Override
	public boolean onSubCommand(CommandSender sender, String[] args) {
		
		if(args.length <= 0){
			CoreColor.message(sender, AllString.prefix + AllString.command_pixellogin_premium_use);
			return true;	
		}
		
		String playername = args[0];
		if(!this.plugin.data.checkData(CoreSQL.WHERE("name:"+playername.toLowerCase()), "name")) {
			CoreColor.message(sender, AllString.prefix + AllString.error_registration_not_found);
			return true;
		}
		
		MojangProfile profile = new MojangProfile("https://api.mojang.com/users/profiles/minecraft/" + playername,plugin.log);
		profile.run();
		if(profile.error()) {
			CoreColor.message(sender, AllString.prefix + AllString.error_mojangapi_shutdown);
			return true;
		}
		if(!profile.isPremiun()) {
			CoreColor.message(sender, AllString.prefix + AllString.player_no_premium);
			return true;
		}
		
		UUID playerUUID = profile.getUuid();
		plugin.data.update(CoreSQL.WHERE("name:"+playername.toLowerCase()),"premium:"+1,"uuid:"+playerUUID.toString());
		
		CoreColor.message(sender, AllString.prefix + AllString.premium_success);
		if(ProxyServer.getInstance().getPlayer(playername) != null) {
			ProxyServer.getInstance().getPlayer(playername).disconnect(CoreColor.getColorTextComponent(AllString.prefix + AllString.premium_changed));  
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
		return "pixellogin.admin.premium";
	}

}
