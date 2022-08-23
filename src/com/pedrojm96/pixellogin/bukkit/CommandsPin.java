package com.pedrojm96.pixellogin.bukkit;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import com.pedrojm96.core.CoreColor;
import com.pedrojm96.core.command.CorePluginCommand;




public class CommandsPin extends CorePluginCommand{

	
	public PixelLoginBukkit plugin;
	
	public CommandsPin(PixelLoginBukkit plugin){
		this.plugin = plugin;
		this.plugin.log.info("Register command /pin");
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, String command, String[] args) {
		// TODO Auto-generated method stub
		if (!(sender instanceof Player)) {
			CoreColor.message(sender,AllString.prefix + AllString.error_no_console);
       	 	return true;
		} 
		Player player = (Player)sender;
		PlayerProfile plogin = this.plugin.player_profile.get(player.getName());
		if(!plogin.isRegistered()) {
			CoreColor.message(sender,AllString.prefix + AllString.command_register_use);
       	 	return true;
		}
		if(!plogin.isLogin()) {
			CoreColor.message(sender,AllString.prefix + AllString.command_login_use);
       	 	return true;
		}
		if(plogin.isPin_login()) {
			CoreColor.message(sender,AllString.prefix + AllString.error_already_pin);
       	 	return true;
		}
		
		if (plugin.sound_pin_command != null) {
	    	player.playSound(player.getLocation(), plugin.sound_pin_command, 1.0F, 1.0F);	
	    }
		
		if(plugin.gui.containsKey(player.getName())) {
			SimpleGUI gui = plugin.gui.get(player.getName());
    		gui.reset();
    		gui.open(player);
    		return true;
		}
		SimpleGUI gui =  new SimpleGUI(AllString.pin_code_menu,6,15);
		plugin.gui.put(player.getName(),gui);
		gui.open(player);
		return true;
	}

	@Override
	public String getErrorNoPermission() {
		// TODO Auto-generated method stub
		return  AllString.prefix + AllString.error_no_permission;
	}

	@Override
	public String getPerm() {
		// TODO Auto-generated method stub
		return "pixellogin.player.pin";
	}


	@Override
	public List<String> onCustomTabComplete(CommandSender sender, List<String> list, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "pin";
	}


	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList("pi");
	}


	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "/pin";
	}


	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Login Plugin pin commands for bukkit server.";
	}

}
