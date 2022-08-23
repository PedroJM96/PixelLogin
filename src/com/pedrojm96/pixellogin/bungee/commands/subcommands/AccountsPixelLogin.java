package com.pedrojm96.pixellogin.bungee.commands.subcommands;


import java.util.HashMap;
import java.util.List;

import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.CoreSubCommand;
import com.pedrojm96.core.bungee.data.CoreSQL;
import com.pedrojm96.pixellogin.bungee.AllString;
import com.pedrojm96.pixellogin.bungee.PixelLoginBungee;

import net.md_5.bungee.api.CommandSender;



public class AccountsPixelLogin extends CoreSubCommand{

	private PixelLoginBungee plugin;
	
	public AccountsPixelLogin(PixelLoginBungee plugin) {
		this.plugin = plugin;
		this.plugin.log.info("Register sub-command /pixellogin accounts");
	}
	
	
	@Override
	public boolean onSubCommand(CommandSender sender, String[] args) {
		
		if(args.length <= 0){
			CoreColor.message(sender, AllString.prefix + AllString.command_pixellogin_accounts_use);
			return true;	
		}
		String playername = args[0];
		if(!this.plugin.data.checkData(CoreSQL.WHERE("name:"+playername.toLowerCase()), "name")) {
			CoreColor.message(sender, AllString.prefix + AllString.error_registration_not_found);
			return true;
		}
		
		HashMap<String, String> datas = this.plugin.data.get(CoreSQL.WHERE("name:"+playername.toLowerCase()), "ip","uuid");
		String ip = datas.get("ip");
		List<HashMap<String, String>> datasips = this.plugin.data.getAll(CoreSQL.WHERE("ip:"+ip), "order by name DESC","uuid","name");
		

		for(String accounts : AllString.command_accounts_list) {
			
			if(accounts.contains("<list>")) {
				int c=0;
				for(HashMap<String, String> dataip : datasips) {
					String[] list = accounts.split("<list>");
					if(list.length>=2) {
						String listformat = list[1];
						c++;
						CoreColor.message(sender, listformat
								.replace("<#>", String.valueOf(c))
								.replace("<playername>", dataip.get("name"))
								.replace("<uuid>", dataip.get("uuid")));
					
					}else {
						CoreColor.message(sender, accounts);
					}	
				}
				
				
			}else {
				CoreColor.message(sender, accounts
						.replace("<playername>", playername)			
						.replace("<ip>", ip));
			}
			
			
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
		return "pixellogin.admin.accounts";
	}

}
