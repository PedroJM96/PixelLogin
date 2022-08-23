package com.pedrojm96.pixellogin.bungee.commands;

import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.CoreCommand;
import com.pedrojm96.pixellogin.bungee.AllString;
import com.pedrojm96.pixellogin.bungee.PixelLoginBungee;


import net.md_5.bungee.api.CommandSender;

public class CommandsPixelLogin extends CoreCommand{

	public PixelLoginBungee plugin;
	
	public CommandsPixelLogin(PixelLoginBungee plugin,String name, String[] aliases) {
		super(name, "pixellogin.admin", aliases);
		this.plugin = plugin;
		this.plugin.log.info("Register command /pixellogin");
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		CoreColor.message(sender,AllString.prefix + AllString.command_pixellogin_use);
		return;
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
