package com.pedrojm96.pixellogin.bungee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pedrojm96.core.bungee.CoreLog;


public class MojangProfile implements Runnable{

	private String url;
	private String name;
	
	private UUID uuid;
	private boolean premiun = false;
	private CoreLog log;
	private boolean error = false;
	
	public class ProfileGso
	  {
	    private String id;
	    private String name;
	    
	    public ProfileGso() {}
	    
	    public String getId()
	    {
	      return this.id;
	    }
	    
	    public String getName()
	    {
	      return this.name;
	    }
	  }
	
	
	
	public MojangProfile(String url, CoreLog log) {
		this.url = url;
		this.log = log;
	}


	@Override
	public void run() {
		try
	    {
	    	HttpURLConnection conn = getConnection(this.url);
	    	if (conn.getResponseCode() == 200)
	    	{
	    		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    		StringBuilder localStringBuilder = new StringBuilder();
	    	    String str1 = null;
	    	    while ((str1 = reader.readLine()) != null) {
	    	    	localStringBuilder.append(str1);
	    	    }
	    	    String str2 = localStringBuilder.toString();
	    	    if (!str2.isEmpty())
	    	    {
	    	    	Gson localGson = new GsonBuilder().create();
	    	    	ProfileGso localMojangProfile = (ProfileGso)localGson.fromJson(str2, ProfileGso.class);
	    	        this.name = localMojangProfile.getName();
	    	        this.uuid = parseUniqueId(localMojangProfile.getId());
	    	        this.premiun = true;
	    	    }
	    	}
	    }
	    catch (Exception ex)
	    {
	    	error = true;
	    	log.error( "Failed to check if player has a paid account: "+this.url);
	    	log.debug( "Failed to check if player has a paid account", ex);
	    }
	}
	
	
	public boolean error() {
		return this.error;
	}
	
	private HttpURLConnection getConnection(String url) throws IOException
	{
		 HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
		 connection.setConnectTimeout(2500);
		 connection.setReadTimeout(2500);  
		 connection.setRequestProperty("Content-Type", "application/json");
		 connection.setRequestProperty("User-Agent", "Premium-Checker");    
		 return connection;
	}
	
	private UUID parseUniqueId(String paramString)
	  {
	    return UUID.fromString(paramString.substring(0, 8) + "-" + paramString.substring(8, 12) + "-" + paramString.substring(12, 16) + "-" + paramString
	      .substring(16, 20) + "-" + paramString.substring(20, 32));
	  }


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	
	
}
