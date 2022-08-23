package com.pedrojm96.pixellogin.bungee;

import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;


public class ProxiedProfile {

	private boolean is_registered = false;
	private boolean is_login = false;
	private boolean is_valid_pin = false;
	
	private boolean premiun = false;
	
	private String password;
	private String pincode = "none";
	
	private String hash;
	
	private boolean captcha_checked = false;
	
	
	private String ip;
	
	
	private long last_login;
	
	private long last_disconnected;
	
	private UUID uuid;
	
	private ProxiedPlayer player;
	
	private PixelLoginBungee plugin;
	

	public ProxiedProfile(UUID uuid,boolean register,PixelLoginBungee plugin) {
		this.is_login = false;
		this.is_registered = register;
		this.uuid = uuid;
		this.plugin = plugin;
	}
	
	public boolean hashPinCode() {
		return (!this.pincode.equals("none"));
	}
	
	public void setLogin(boolean login) {
		this.is_login = login;
	}
	
	public void setPinLogin(boolean pin) {
		this.is_valid_pin = pin;
	}
	
	public void setRegister(boolean re) {
		this.is_registered = re;
	}
	
	public boolean checkHashPassword(String hashPassword) {
		return this.password.equals(hashPassword);
	}
	
	public boolean isLogin() {
		return this.is_login;
	}
	
	public boolean isPinLogin() {
		return this.is_valid_pin;
	}
	
	public boolean isRegistered() {
		return this.is_registered;
	}

	
	public boolean isFullLogin() {
		return isLogin() && isPinLogin();
	}
	
	/**
	 * @return the player
	 */
	public ProxiedPlayer getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(ProxiedPlayer player) {
		this.player = player;
	}

	

	/**
	 * @return the pincode
	 */
	public String getPincode() {
		return pincode;
	}

	/**
	 * @param pincode the pincode to set
	 */
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	/**
	 * @return the premiun
	 */
	public boolean isPremiun() {
		return premiun;
	}

	/**
	 * @param premiun the premiun to set
	 */
	public void setPremiun(boolean premiun) {
		this.premiun = premiun;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * @return the last_join
	 */
	public long getLast_login() {
		return last_login;
	}

	/**
	 * @param last_join the last_join to set
	 */
	public void setLast_login(long last_login) {
		this.last_login = last_login;
	}	
	
	public Session getSession() {
		return new Session(this.last_disconnected,plugin.config.getInt("session.time"),plugin.config.getString("session.type"),this.ip);
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the last_disconnected
	 */
	public long getLast_disconnected() {
		return last_disconnected;
	}

	/**
	 * @param last_disconnected the last_disconnected to set
	 */
	public void setLast_disconnected(long last_disconnected) {
		this.last_disconnected = last_disconnected;
	}

	/**
	 * @return the captcha_checked
	 */
	public boolean isCaptcha_checked() {
		return captcha_checked;
	}

	/**
	 * @param captcha_checked the captcha_checked to set
	 */
	public void setCaptcha_checked(boolean captcha_checked) {
		this.captcha_checked = captcha_checked;
	}
}
