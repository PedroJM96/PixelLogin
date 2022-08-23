package com.pedrojm96.pixellogin.bungee.commands.subcommands;


import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.CoreSubCommand;
import com.pedrojm96.pixellogin.bungee.AllString;
import com.pedrojm96.pixellogin.bungee.PixelLoginBungee;


import net.md_5.bungee.api.CommandSender;



public class HelpPixelLogin extends CoreSubCommand{

	private PixelLoginBungee plugin;
	
	public HelpPixelLogin(PixelLoginBungee plugin) {
		this.plugin = plugin;
		this.plugin.log.info("Register sub-command /pixellogin help");
	}
	
	
	@Override
	public boolean onSubCommand(CommandSender sender, String[] args) {
		
		plugin.log.info(AllString.help_command_pixellogin_register);
		CoreColor.message(sender,"&8&m---------------------------------------------------");
		CoreColor.message(sender,AllString.prefix + "");
		CoreColor.message(sender,AllString.prefix + AllString.help_command_pixellogin_register);
		CoreColor.message(sender,AllString.prefix + AllString.help_description_pixellogin_register);
		
		CoreColor.message(sender,AllString.prefix + AllString.help_command_pixellogin_cracked);
		CoreColor.message(sender,AllString.prefix + AllString.help_description_pixellogin_cracked);
		
		CoreColor.message(sender,AllString.prefix + AllString.help_command_pixellogin_premium);
		CoreColor.message(sender,AllString.prefix + AllString.help_description_pixellogin_premium);
		
		
		CoreColor.message(sender,AllString.prefix + AllString.help_command_pixellogin_info);
		CoreColor.message(sender,AllString.prefix + AllString.help_description_pixellogin_info);
		
		CoreColor.message(sender,AllString.prefix + AllString.help_command_pixellogin_accounts);
		CoreColor.message(sender,AllString.prefix + AllString.help_description_pixellogin_accounts);
		
		CoreColor.message(sender,AllString.prefix + AllString.help_command_pixellogin_unregister);
		CoreColor.message(sender,AllString.prefix + AllString.help_description_pixellogin_unregister);
		CoreColor.message(sender,AllString.prefix + "");
		CoreColor.message(sender,"&8&m---------------------------------------------------");
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
		return "pixellogin.admin";
	}

}
