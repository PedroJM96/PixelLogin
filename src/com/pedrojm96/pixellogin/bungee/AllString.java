package com.pedrojm96.pixellogin.bungee;

import java.util.ArrayList;
import java.util.List;

import com.pedrojm96.core.bungee.CoreConfig;





public class AllString {
	
	
	
	
	
	public static String prefix;
	public static String already_logged_in;
	
	public static String you_already_logged_in;
	public static String register;
	public static String login;
	

	public static String error_no_permission;
	public static String error_no_console;
	public static String command_register_use;
	public static String error_already_registered;
	public static String register_success;
	public static String login_failed;
	public static String command_login_use;
	public static String login_success;
	public static String pin_code_staff_register;
	public static String error_already_pin;
	public static String pin_backspace;
	public static String pin_ok;
	public static String pin_code_menu;
	public static String pin_code_register_success;
	public static String pin_code_login;
	public static String pin_code_login_success;
	public static String pin_code_failed;
	public static String pin_code_null;
	public static String cracked_player_already_registered;
	public static String premium_invalid_session;
	public static String auto_premium_login;
	
	public static String join_regex = "[a-zA-Z0-9_]{1,16}";
	public static String player_invalid_name;
	
	
	
	public static String login_title = "&cPlease log in";
	public static String login_subtitle = "&6Login with /login <password> in the chat.";
	public static String register_title = "&cPlease register";
	public static String register_subtitle = "&6Register with /register <password> in the chat";
	public static String register_success_title = "&aSuccessfully logged in";
	public static String register_success_subtitle = "&7You can play now";
	public static String login_failed_title = "&cIncorrect Password";
	public static String login_failed_subtitle = "&7You have &c%Attempts% &7Attempts left.";
	public static String login_success_title = "&aSuccessfully logged in";
	public static String login_success_subtitle = "&7You can play now";
	
	public static String auto_premium_login_title = "&bPREMIUM";
	public static String auto_premium_login_subtitle = "&aSuccessfully logged in";
	
	public static String pin_code_user_register;
	
	public static String command_pixellogin_use;
	public static String command_pixellogin_register_use;
	public static String command_pixellogin_cracked_use;
	public static String error_mojangapi_shutdown;
	public static String error_player_already_registered;
	
	public static String help_command_pixellogin_register;
	public static String help_description_pixellogin_register;
	
	public static String unregister_success;
	public static String command_pixellogin_unregister_use;
	public static String error_registration_not_found;
	
	public static String help_command_pixellogin_unregister;
	public static String help_description_pixellogin_unregister;
	
	public static String command_changepassword_use;
	
	public static String changepassword_success;
	
	public static String error_max_multi_accounts;
	
	public static String session_restored;
	
	public static String pin_code_login_actionbar;
	
	public static String login_bossbar;
	public static String register_bossbar;
	
	public static String kick_timeout;
	
	public static String token_error;
	
	public static String cracked_success;
	
	public static String cracked_changed;
	
	public static String command_pixellogin_premium_use;
	
	public static String player_no_premium;
	
	public static String premium_success;
	
	public static String premium_changed;
	
	public static String help_command_pixellogin_cracked;
	public static String help_description_pixellogin_cracked;
	
	public static String help_command_pixellogin_premium;
	public static String help_description_pixellogin_premium;
	
	public static String command_pixellogin_info_use;
	
	public static String command_pixellogin_accounts_use;
	
	public static List<String> command_info_list = new ArrayList<String>();
	
	public static List<String> command_accounts_list = new ArrayList<String>();
	
	
	public static String help_command_pixellogin_info;
	public static String help_description_pixellogin_info;
	
	public static String help_command_pixellogin_accounts;
	public static String help_description_pixellogin_accounts;
	
	
	public static String no_captcha_code;
	
	public static String captcha_checked_title;
	
	public static String captcha_checked_subtitle;
	
	public static String captcha_checked;
	
	public static String captcha_code_register;
	
	public static String captcha;
	
	
	public static List<String> pins_numbers = new ArrayList<String>();
	
	
	public static void load(CoreConfig config,CoreConfig messages) {
		prefix = config.getString("prefix");
		already_logged_in = messages.getString("already-logged-in");
		you_already_logged_in = messages.getString("you-already-logged-in");
		register = messages.getString("register");
		login = messages.getString("login");
		error_no_permission = messages.getString("error-no-permission");
		command_register_use = messages.getString("command-register-use");
		error_no_console = messages.getString("error-no-console");
		error_already_registered = messages.getString("error-already-registered");
		register_success = messages.getString("register-success");
		login_failed = messages.getString("login-failed");
		command_login_use = messages.getString("command-login-use");
		login_success = messages.getString("login-success");
		pin_code_staff_register = messages.getString("pin-code-staff-register");
		error_already_pin = messages.getString("error-already-pin");
		pin_backspace = messages.getString("pin-backspace");
		pin_ok = messages.getString("pin-ok");
		pin_code_menu = messages.getString("pin-code-menu");
		pin_code_register_success = messages.getString("pin-code-register-success");
		pin_code_login = messages.getString("pin-code-login");
		pin_code_login_success = messages.getString("pin-code-login-success");
		pin_code_failed = messages.getString("pin-code-failed");
		pin_code_null = messages.getString("pin-code-null");
		cracked_player_already_registered = messages.getString("cracked-player-already-registered");
		premium_invalid_session = messages.getString("premium-invalid-session");
		auto_premium_login = messages.getString("auto-premium-login");
		pin_code_user_register = messages.getString("pin-code-user-register");
		player_invalid_name = messages.getString("player-invalid-name");
		pin_code_login_actionbar = messages.getString("pin-code-login-actionbar");
		
		login_title = messages.getString("login-title");
		login_subtitle = messages.getString("login-subtitle");
		register_title = messages.getString("register-title");
		register_subtitle = messages.getString("register-subtitle");
		register_success_title = messages.getString("register-success-title");
		register_success_subtitle = messages.getString("register-success-subtitle");
		login_failed_title = messages.getString("login-failed-title");
		login_failed_subtitle = messages.getString("login-failed-subtitle");
		login_success_title = messages.getString("login-success-title");
		login_success_subtitle = messages.getString("login-success-subtitle");
		
		auto_premium_login_title = messages.getString("auto-premium-login-title");
		auto_premium_login_subtitle = messages.getString("auto-premium-login-subtitle");
		
		command_pixellogin_use = messages.getString("command-pixellogin-use");
		command_pixellogin_register_use = messages.getString("command-pixellogin-register-use");
		command_pixellogin_cracked_use = messages.getString("command-pixellogin-cracked-use");
		error_mojangapi_shutdown = messages.getString("error-mojangapi-shutdown");
		error_player_already_registered = messages.getString("error-player-already-registered");
		
		help_command_pixellogin_register = messages.getString("help-command-pixellogin-register");
		help_description_pixellogin_register = messages.getString("help-description-pixellogin-register");
		
		unregister_success = messages.getString("unregister-success");
		command_pixellogin_unregister_use = messages.getString("command-pixellogin-unregister-use");
		error_registration_not_found = messages.getString("error-registration-not-found");
		
		help_command_pixellogin_unregister = messages.getString("help-command-pixellogin-unregister");
		help_description_pixellogin_unregister = messages.getString("help-description-pixellogin-unregister");
		
		command_changepassword_use = messages.getString("command-changepassword-use");
		
		changepassword_success = messages.getString("changepassword-success");
		
		error_max_multi_accounts = messages.getString("error-max-multi-accounts");
		
		session_restored = messages.getString("session-restored");
		
		login_bossbar = messages.getString("login-bossbar");
		register_bossbar = messages.getString("register-bossbar");
		
		kick_timeout = messages.getString("kick-timeout");
		
		token_error = messages.getString("token-error");
		
		cracked_success = messages.getString("cracked-success");
		cracked_changed = messages.getString("cracked-changed");
		
		player_no_premium = messages.getString("player-no-premium");
		
		premium_success = messages.getString("premium-success");
		
		premium_changed = messages.getString("premium-changed");
		
		command_pixellogin_premium_use = messages.getString("command-pixellogin-premium-use");
		
		
		help_command_pixellogin_cracked = messages.getString("help-command-pixellogin-cracked");
		help_description_pixellogin_cracked = messages.getString("help-description-pixellogin-cracked");
		
		help_command_pixellogin_premium = messages.getString("help-command-pixellogin-premium");
		help_description_pixellogin_premium = messages.getString("help-description-pixellogin-premium");
		
		command_pixellogin_info_use = messages.getString("command-pixellogin-info-use");
		
		command_pixellogin_accounts_use = messages.getString("command-pixellogin-accounts-use");
		
		command_info_list = messages.getStringList("command-info-list");
		
		command_accounts_list = messages.getStringList("command-accounts-list");

		
		no_captcha_code = messages.getString("no-captcha-code");
		
		help_command_pixellogin_info = messages.getString("help-command-pixellogin-info");
		help_description_pixellogin_info = messages.getString("help-description-pixellogin-info");
		
		help_command_pixellogin_accounts = messages.getString("help-command-pixellogin-accounts");
		help_description_pixellogin_accounts = messages.getString("help-description-pixellogin-accounts");
		
		captcha_checked_title = messages.getString("captcha-checked-title");
		
		captcha_checked_subtitle = messages.getString("captcha-checked-subtitle");
		
		captcha_checked = messages.getString("captcha-checked");
		
		captcha_code_register = messages.getString("captcha-code-register");
		captcha = messages.getString("captcha");
		
		
		for(int i = 0 ; i<10;i++) {
			pins_numbers.add(i, messages.getString("pins-numbers."+i));
		}
	}
}
