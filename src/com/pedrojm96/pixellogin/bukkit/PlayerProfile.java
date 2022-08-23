package com.pedrojm96.pixellogin.bukkit;

public class PlayerProfile {


	private boolean registered;
	private boolean login;
	private boolean pin_login = false;
	private String pin_code = "none";
	
	private String hash;
	
	private String captcha_answer;

	

	public PlayerProfile(boolean registered,boolean login,boolean pin,String pincode) {
		this.login = login;
		this.registered = registered;
		this.pin_login = pin;
		this.pin_code = pincode;

		
	}
	
	
	
	public String getCaptchaAnswer() {
		return this.captcha_answer;
	}
	
	public String getPinCode() {
		return this.pin_code;
	}
	
	public boolean hashPinCode() {
		if(pin_login) {
			return true;
		}else {
			return (!this.pin_code.equals("none"));
		}
	}



	/**
	 * @return the registered
	 */
	public boolean isRegistered() {
		return registered;
	}



	/**
	 * @param registered the registered to set
	 */
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}



	/**
	 * @return the login
	 */
	public boolean isLogin() {
		return login;
	}



	/**
	 * @param login the login to set
	 */
	public void setLogin(boolean login) {
		this.login = login;
	}



	/**
	 * @return the pin_login
	 */
	public boolean isPin_login() {
		return pin_login;
	}



	/**
	 * @param pin_login the pin_login to set
	 */
	public void setPin_login(boolean pin_login) {
		this.pin_login = pin_login;
	}






	/**
	 * @param ping the ping to set
	 */
	public void setPingCode(String ping) {
		this.pin_code = ping;
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
			
}
