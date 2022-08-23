package com.pedrojm96.pixellogin.bungee.commands.subcommands;


import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.CoreSubCommand;
import com.pedrojm96.core.bungee.data.CoreSQL;
import com.pedrojm96.pixellogin.bungee.AllString;
import com.pedrojm96.pixellogin.bungee.PixelLoginBungee;


import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;



public class UnRegisterPixelLogin extends CoreSubCommand{

	private PixelLoginBungee plugin;
	
	public UnRegisterPixelLogin(PixelLoginBungee plugin) {
		this.plugin = plugin;
		this.plugin.log.info("Register sub-command /pixellogin unregister");
	}
	
	
	@Override
	public boolean onSubCommand(CommandSender sender, String[] args) {
		
		if(args.length <= 0){
			CoreColor.message(sender, AllString.prefix + AllString.command_pixellogin_unregister_use);
			return true;	
		}
		String playername = args[0];
		if(ProxyServer.getInstance().getPlayer(playername) != null) {
			CoreColor.message(sender, AllString.prefix + AllString.already_logged_in);
			return true;
		}
		
		if(!this.plugin.data.checkData(CoreSQL.WHERE("name:"+playername.toLowerCase()), "name")) {
			CoreColor.message(sender, AllString.prefix + AllString.error_registration_not_found);
			return true;
		}
		plugin.data.delete(CoreSQL.WHERE("name:"+playername.toLowerCase()));
		CoreColor.message(sender, AllString.prefix + AllString.unregister_success);
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
		return "pixellogin.admin.unregister";
	}

}
