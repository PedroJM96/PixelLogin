package com.pedrojm96.pixellogin.bungee.commands.subcommands;


import java.util.HashMap;

import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.CoreSubCommand;
import com.pedrojm96.core.bungee.data.CoreSQL;
import com.pedrojm96.pixellogin.bungee.AllString;
import com.pedrojm96.pixellogin.bungee.PixelLoginBungee;

import net.md_5.bungee.api.CommandSender;



public class InfoPixelLogin extends CoreSubCommand{

	private PixelLoginBungee plugin;
	
	public InfoPixelLogin(PixelLoginBungee plugin) {
		this.plugin = plugin;
		this.plugin.log.info("Register sub-command /pixellogin info");
	}
	
	
	@Override
	public boolean onSubCommand(CommandSender sender, String[] args) {
		
		if(args.length <= 0){
			CoreColor.message(sender, AllString.prefix + AllString.command_pixellogin_info_use);
			return true;	
		}
		String playername = args[0];
		if(!this.plugin.data.checkData(CoreSQL.WHERE("name:"+playername.toLowerCase()), "name")) {
			CoreColor.message(sender, AllString.prefix + AllString.error_registration_not_found);
			return true;
		}
		
		HashMap<String, String> datas = this.plugin.data.get(CoreSQL.WHERE("name:"+playername.toLowerCase()), "uuid","premium","last_login","ip","last_disconnected");
		
		String uuid = datas.get("uuid");
		String ip = datas.get("ip");
		String premium = (datas.get("premium").equals("1") ? "&atrue": "&cfalse" );
		String last_login = datas.get("last_login");
		String last_disconnected = datas.get("last_disconnected");
		
		
		for(String info : AllString.command_info_list) {
			CoreColor.message(sender, info
					.replace("<playername>", playername)
					.replace("<uuid>", uuid)
					.replace("<premium>", premium)
					.replace("<ip>", ip)
					.replace("<lastlogin>", last_login)
					.replace("<lastdisconnected>", last_disconnected));
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
		return "pixellogin.admin.info";
	}

}
