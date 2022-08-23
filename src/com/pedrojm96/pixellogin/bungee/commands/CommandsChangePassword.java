package com.pedrojm96.pixellogin.bungee.commands;


import com.pedrojm96.core.CoreEncryption;
import com.pedrojm96.core.bungee.CoreColor;
import com.pedrojm96.core.bungee.CoreCommand;
import com.pedrojm96.core.bungee.data.CoreSQL;
import com.pedrojm96.pixellogin.bungee.AllString;
import com.pedrojm96.pixellogin.bungee.PixelLoginBungee;
import com.pedrojm96.pixellogin.bungee.ProxiedProfile;



import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class CommandsChangePassword extends CoreCommand{

	public PixelLoginBungee plugin;
	
	public CommandsChangePassword(PixelLoginBungee plugin,String name, String[] aliases) {
		super(name, "pixellogin.player.changepassword", aliases);
		this.plugin = plugin;
		this.plugin.log.info("Register command /changepassword");
	}

	@Override
	public void onCommand(CommandSender sender, String[] arg) {
		// TODO Auto-generated method stub
		if (!(sender instanceof ProxiedPlayer)) {
			CoreColor.message(sender,AllString.prefix + AllString.error_no_console);
       	 	return;
		}
		
		if(arg.length == 0) {
			CoreColor.message(sender,AllString.prefix + AllString.command_changepassword_use);
			return;
		}
		
		ProxiedPlayer player = (ProxiedPlayer)sender;
		ProxiedProfile plogin = this.plugin.proxiedProfile.get(player.getName());
		if(!plogin.isRegistered()) {
			CoreColor.message(sender,AllString.prefix + AllString.command_register_use);
       	 	return;
		}
		
		if(!plogin.isLogin()) {
			CoreColor.message(sender,AllString.prefix + AllString.command_login_use);
       	 	return;
		}
		
		if(plugin.config.getBoolean("ping-code-all-users") && !plogin.isPinLogin()) {
			CoreColor.message(sender,AllString.prefix + AllString.pin_code_login);
       	 	return;
		}
		if(plugin.config.getBoolean("ping-code-staff") && sender.hasPermission("pixellogin.staff") && !plogin.isPinLogin()) {
			CoreColor.message(sender,AllString.prefix + AllString.pin_code_login);
       	 	return;
		}
		
		String clave = arg[0];
		String hashPassword = CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).encrypt(clave);
		plugin.data.update(CoreSQL.WHERE("uuid:"+player.getUniqueId().toString()), "password:"+hashPassword,"hash:"+CoreEncryption.valueOf(plugin.config.getString("encryption-method").toUpperCase()).toString());
		plogin.setPassword(hashPassword);
		CoreColor.message(player, AllString.prefix + AllString.changepassword_success);
	}

	@Override
	public String getErrorNoPermission() {
		// TODO Auto-generated method stub
		return AllString.prefix + AllString.error_no_permission;
	}

	@Override
	public String getPerm() {
		// TODO Auto-generated method stub
		return "pixellogin.player.changepassword";
	}

}
