package com.pedrojm96.pixellogin.bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;


public class AllString {
	
	


	public static String prefix = "&fPixelLogin &8> ";
	public static String already_logged_in = "&cAnother User with the same Account is already logged in.";
	public static String register = "&7Please register with &9/register <clave>";
	public static String login = "&7Please login using &9/login <password>";
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
	public static String error_no_permission = "&7You not have permission to use this command.";
	public static String error_no_console = "&7Not available in the console.";
	public static String error_already_pin = "&7You already have a pin code registered.";
	public static String command_register_use = "&7Use &9/register <password>";
	public static String command_login_use = "&7Use &9/login <password>";
	public static String auto_premium_login_title = "&bPREMIUM";
	public static String auto_premium_login_subtitle = "&aSuccessfully logged in";
	public static String pin_backspace = "&cBackspace";
	public static String pin_ok = "&6OK";
	public static String pin_code_menu = "&8Pin code";
	public static String pin_code_register_success = "&7Successfully registered code.";
	public static String pin_code_login = "&7Please put your pin code.";
	public static String pin_code_login_success = "&7Successful pin code.";
	public static String pin_code_failed = "&7Incorrect pin code.";
	public static String pin_code_null = "&7You must establish a valid pin code.";
	public static String pin_code_login_actionbar = "&6Please put your pin code with the &b/pin &6command.";
	public static String login_bossbar = "&bWelcome. Login with &6/login <password> &bin the chat.";
	public static String register_bossbar = "&bWelcome. Register with &6/register <password> &bin the chat";
	
	public static String token_error = "&cYou have to join through the proxy.";
	
	public static String no_captcha_code = "&7The captcha code is not correct, please try again..";
	
	public static String captcha_checked_title = "&aChecked";
	
	public static String captcha_checked_subtitle = "&eYou can now register.";
	
	
	public static List<String> pins_numbers = Arrays.asList("&a0","&a1","&a2","&a3","&a4","&a5","&a6","&a7","&a8","&a9");
	
	
	public static void load(JsonObject obj) {
		
		
		prefix = obj.get("prefix").getAsString();
		already_logged_in = obj.get("already_logged_in").getAsString();
		register = obj.get("register").getAsString();
		login = obj.get("login").getAsString();
		login_title = obj.get("login_title").getAsString();
		login_subtitle = obj.get("login_subtitle").getAsString();
		register_title = obj.get("register_title").getAsString();
		register_subtitle = obj.get("register_subtitle").getAsString();
		register_success_title = obj.get("register_success_title").getAsString();
	    register_success_subtitle = obj.get("register_success_subtitle").getAsString();
		login_failed_title = obj.get("login_failed_title").getAsString();
		login_failed_subtitle = obj.get("login_failed_subtitle").getAsString();
		login_success_title = obj.get("login_success_title").getAsString();
		login_success_subtitle = obj.get("login_success_subtitle").getAsString();
		error_no_permission = obj.get("error_no_permission").getAsString();
		error_no_console = obj.get("error_no_console").getAsString();
		error_already_pin = obj.get("error_already_pin").getAsString();
		command_register_use = obj.get("command_register_use").getAsString();
		command_login_use = obj.get("command_login_use").getAsString();
		auto_premium_login_title = obj.get("auto_premium_login_title").getAsString();
		auto_premium_login_subtitle = obj.get("auto_premium_login_subtitle").getAsString();
		pin_backspace = obj.get("pin_backspace").getAsString();
		pin_ok = obj.get("pin_ok").getAsString();
		pin_code_menu = obj.get("pin_code_menu").getAsString();
		pin_code_register_success = obj.get("pin_code_register_success").getAsString();
		pin_code_login = obj.get("pin_code_login").getAsString();
		pin_code_login_success = obj.get("pin_code_login_success").getAsString();
		pin_code_failed = obj.get("pin_code_failed").getAsString();
		pin_code_null = obj.get("pin_code_null").getAsString();
		pin_code_login_actionbar = obj.get("pin_code_login_actionbar").getAsString();
		
		login_bossbar = obj.get("login_bossbar").getAsString();
		register_bossbar = obj.get("register_bossbar").getAsString();
		
		token_error = obj.get("token_error").getAsString();
		
		no_captcha_code = obj.get("no_captcha_code").getAsString();
		
		
		captcha_checked_title = obj.get("captcha_checked_title").getAsString();
		captcha_checked_subtitle = obj.get("captcha_checked_subtitle").getAsString();
		
		JsonObject obj2 = obj.getAsJsonObject("pins_numbers");
		
		pins_numbers = new ArrayList<String>();
		for(int i = 0 ; i<10;i++) {
			pins_numbers.add(i, obj2.get(String.valueOf(i)).getAsString());
		}
	}
}
