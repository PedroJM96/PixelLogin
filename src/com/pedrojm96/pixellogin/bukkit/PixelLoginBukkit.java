package com.pedrojm96.pixellogin.bukkit;

import java.util.HashMap;

import org.bstats.bukkit.Metrics;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.pedrojm96.core.CoreConfig;
import com.pedrojm96.core.CoreLog;
import com.pedrojm96.core.CorePlugin;
import com.pedrojm96.core.CoreSpigotUpdater;
import com.pedrojm96.core.command.CoreCommands;
import com.pedrojm96.core.data.CoreMySQL;
import com.pedrojm96.core.data.CoreMySQLConnection;
import com.pedrojm96.core.data.CoreSQL;

import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;








public class PixelLoginBukkit extends JavaPlugin implements CorePlugin{

	public CoreLog log;
	public CoreConfig config;
	
	public CoreConfig configMessages;
	
	public boolean messages;
	
	public boolean get_profile;
	
	
	public HashMap<String, PlayerProfile> player_profile = new HashMap<String, PlayerProfile>();
	
	public HashMap<String, Integer> login_register_timers = new HashMap<String, Integer>();
	public HashMap<String, Integer> pin_login_actionbar_timers = new HashMap<String, Integer>();
	public HashMap<String, SimpleGUI> gui = new HashMap<String, SimpleGUI>();
	
	
	public HashMap<String, Integer> get_timers = new HashMap<String, Integer>();
	
	public HashMap<String, Captcha> captchas = new HashMap<String, Captcha>();
	
	public boolean bungeeCord;
	public JavaPlugin plugin;
	
	public final String CHANNEL = "pixellogin:update";
	public CoreSQL data;
	
	public Sound sound_register_command;
	public Sound sound_login_command_success;
	public Sound sound_login_command_failed;
	public Sound sound_pin_command;
	public Sound sound_pin_menu;
	public Sound sound_pin_menu_success;
	public Sound sound_pin_menu_failed;
	
	public Sound captcha_checked;
	
	public boolean sound;
	
	public MessagingManager messagingManager;
	
	public boolean pluginStart = false;
	
	
	public void banner() {
		this.log.line();
		this.log.println("  _____ _          _ _                 _       ");
		this.log.println(" |  __ (_)        | | |               (_)      ");
		this.log.println(" | |__) |__  _____| | |     ___   __ _ _ _ __  ");
		this.log.println(" |  ___/ \\ \\/ / _ \\ | |    / _ \\ / _` | | '_ \\"); 
		this.log.println(" | |   | |>  <  __/ | |___| (_) | (_| | | | | |"); 
		this.log.println(" |_|   |_/_/\\_\\___|_|______\\___/ \\__, |_|_| |_|"); 
		this.log.println("                                  __/ |        "); 
		this.log.println("                                 |___/         ");
		this.log.line();
	}
	
	
	
	public void loadDependencies(){
		this.log.info("Loading Libraries...");
		 BukkitLibraryManager bukkitLibraryManager = new BukkitLibraryManager(this);
			Library commons_lang_2_6 = Library.builder()
				    .groupId("commons-lang") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
				    .artifactId("commons-lang")
				    .version("2.6")
				     // The following are optional

				     // Sets an id for the library
				    .id("commons-lang-2-6")
				    .isolatedLoad(true)
				    .build();
			Library commons_codec_1_15 = Library.builder()
				    .groupId("commons-codec") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
				    .artifactId("commons-codec")
				    .version("1.15")
				     // The following are optional

				     // Sets an id for the library
				    .id("commons-codec-1-15")
				    .isolatedLoad(true)
				    .build();
			Library com_google_code_gson_2_9_0 = Library.builder()
				    .groupId("com{}google{}code{}gson") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
				    .artifactId("gson")
				    .version("2.9.0")
				     // The following are optional

				     // Sets an id for the library
				    .id("com-google-code-gson-2-9-0")
				    .isolatedLoad(true)
				    .build();
			Library org_slf4j_api_1_7_25 = Library.builder()
				    .groupId("org{}slf4j") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
				    .artifactId("slf4j-api")
				    .version("1.7.25")
				     // The following are optional

				     // Sets an id for the library
				    .id("org-slf4j-api-1-7-25")
				    .isolatedLoad(true)
				    .build();
			Library org_slf4j_simple_1_7_25 = Library.builder()
				    .groupId("org{}slf4j") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
				    .artifactId("slf4j-simple")
				    .version("1.7.25")
				     // The following are optional

				     // Sets an id for the library
				    .id("org-slf4j-simple-1-7-25")
				    .isolatedLoad(true)
				    .build();
			Library com_zaxxer_hikaricp_3_4_1 = Library.builder()
				    .groupId("com{}zaxxer") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
				    .artifactId("HikariCP")
				    .version("3.4.1")
				     // The following are optional

				     // Sets an id for the library
				    .id("com-zaxxer-hikaricp-3-4-1")
				    .isolatedLoad(true)
				    .build();
			
			Library com_rabbitmq_amqp_client_5_10_0 = Library.builder()
				    .groupId("com{}rabbitmq") // "{}" is replaced with ".", useful to avoid unwanted changes made by maven-shade-plugin
				    .artifactId("amqp-client")
				    .version("5.10.0")
				     // The following are optional

				     // Sets an id for the library
				    .id("com-rabbitmq-amqp-client-5-10-0")
				    .isolatedLoad(true)
				    .build();
			bukkitLibraryManager.addMavenCentral();
			bukkitLibraryManager.loadLibrary(commons_lang_2_6);
			bukkitLibraryManager.loadLibrary(commons_codec_1_15);
			bukkitLibraryManager.loadLibrary(com_google_code_gson_2_9_0);
			bukkitLibraryManager.loadLibrary(org_slf4j_simple_1_7_25);
			bukkitLibraryManager.loadLibrary(com_zaxxer_hikaricp_3_4_1);
			bukkitLibraryManager.loadLibrary(org_slf4j_api_1_7_25);
			bukkitLibraryManager.loadLibrary(com_rabbitmq_amqp_client_5_10_0);
	 }
	
	
	
	
	
	@Override
	public void onEnable() {
		this.plugin = this;
		this.log = new CoreLog(this,CoreLog.Color.WHITE);
		banner();
		this.log.info("Plugin Create by PedroJM96.");
		loadDependencies();
		try {
		   //Ponemos a "Dormir" el programa durante los ms que queremos
		   Thread.sleep(1*1000);
		}
		catch (Exception e) {
		   System.out.println(e);
		}
		
		this.log.info("Register commands...");
		this.getServer().getPluginManager().registerEvents(new PixelBukkitListener(this), this);

		CoreCommands.registerCommand(new CommandsPin(this), this);
		try {
			bungeeCord = Class.forName("org.spigotmc.SpigotConfig").getDeclaredField("bungee").getBoolean(null);
		} catch (Exception ex) {
			log.error("This Plugin work only on the spigot server with bungeecod set to true, please enabled bungeecord mode.");
			getServer().getPluginManager().disablePlugin(this);   
			return;
		}
		
		
		this.log.info("Loading configuration...");
		this.config = new CoreConfig(this,"config",log,this.getResource("config_bukkit.yml"),true);
		
		this.log.seDebug(this.config.getBoolean("debug"));

		saveResource("background.png", false);
		
		if(config.getBoolean("sound.enable")) {
			loadSound();
	    }
		
		if(this.config.getBoolean("data-storage.enable")) {
			CoreMySQLConnection  coreConnection =new CoreMySQLConnection(this,this.config.getString("data-storage.host"),this.config.getInt("data-storage.port"),this.config.getString("data-storage.database"),this.config.getString("data-storage.username"),this.config.getString("data-storage.password"),false);	
			this.data = new CoreMySQL(coreConnection,"pixellogin");
		}else {
			log.error("Plugin disable pleace set DataStorage in config.yml");
			this.plugin.getServer().getPluginManager().disablePlugin(this);
			log.line();
			return;
		}
		
		if(!this.data.checkStorage()) {
			this.data.build("uuid varchar(36)" ,"name varchar(16)","ip varchar(40)", "password varchar(255)", "pincode varchar(255)", "premium int(1)", "login int(1)", "pin_login int(1)", "token varchar(255)","first_login bigint(20)", "last_login bigint(20)","last_disconnected bigint(20)","hash varchar(10)");
		}
		
		if (getServer().getOnlineMode()) {
	           log.error("This Plugin does not work on the server in premium mode, please disable it.");
	           getServer().getPluginManager().disablePlugin(this);   
	           return;
	    }
		
		
		messagingManager = new MessagingManager(this);
		messagingManager.load();
		
		
		
		@SuppressWarnings("unused")
		Metrics metrics =new Metrics(this,3981);
		checkForUpdates();
		pluginStart = true;
		this.log.line();
	}
	
	
	@Override
    public void onDisable() {
		
		if(pluginStart) {
			messagingManager.closed(); 
		}
		 
    }
	
	public void loadSound() {
		try {
    		sound_register_command = Sound.valueOf(config.getString("sound.register-command"));
        } catch (IllegalArgumentException ignore2) {
           //Revisar traducion xd
        	log.error("The Sound " + config.getString("sound.register-command") + " has an invalid Sound name in the version, please change to valid name in the version, this sound are disabled.");
        }
    	try {
    		sound_login_command_success = Sound.valueOf(config.getString("sound.login-command-success"));
        } catch (IllegalArgumentException ignore2) {
           //Revisar traducion xd
        	log.error("The Sound " + config.getString("sound.login-command-success") + " has an invalid Sound name in the version, please change to valid name in the version, this sound are disabled.");
        }
    	try {
    		sound_login_command_failed = Sound.valueOf(config.getString("sound.login-command-failed"));
    		
        } catch (IllegalArgumentException ignore2) {
           //Revisar traducion xd
        	log.error("The Sound " + config.getString("sound.login-command-failed") + " has an invalid Sound name in the version, please change to valid name in the version, this sound are disabled.");
        }
    	try {
    		sound_pin_command = Sound.valueOf(config.getString("sound.pin-command"));
    		
        } catch (IllegalArgumentException ignore2) {
           //Revisar traducion xd
        	log.error("The Sound " + config.getString("sound.pin-command") + " has an invalid Sound name in the version, please change to valid name in the version, this sound are disabled.");
        }
    	try {
    		sound_pin_menu = Sound.valueOf(config.getString("sound.pin-menu"));
    		
        } catch (IllegalArgumentException ignore2) {
           //Revisar traducion xd
        	log.error("The Sound " + config.getString("sound.pin-menu") + " has an invalid Sound name in the version, please change to valid name in the version, this sound are disabled.");
        }
    	try {
    		sound_pin_menu_success = Sound.valueOf(config.getString("sound.pin-menu-success"));
    		
        } catch (IllegalArgumentException ignore2) {
           //Revisar traducion xd
        	log.error("The Sound " + config.getString("sound.pin-menu-success") + " has an invalid Sound name in the version, please change to valid name in the version, this sound are disabled.");
        }
    	try {
    		sound_pin_menu_failed = Sound.valueOf(config.getString("sound.pin-menu-failed"));
    		
        } catch (IllegalArgumentException ignore2) {
           //Revisar traducion xd
        	log.error("The Sound " + config.getString("sound.pin-menu-failed") + " has an invalid Sound name in the version, please change to valid name in the version, this sound are disabled.");
        }
    	
    	try {
    		captcha_checked = Sound.valueOf(config.getString("sound.captcha-checked"));
    		
        } catch (IllegalArgumentException ignore2) {
           //Revisar traducion xd
        	log.error("The Sound " + config.getString("sound.captcha-checked") + " has an invalid Sound name in the version, please change to valid name in the version, this sound are disabled.");
        }
	}
	
	public void checkForUpdates() {
		if(config.getBoolean("update-check")){
			new BukkitRunnable() {
				@Override
				public void run() {
					CoreSpigotUpdater updater = new CoreSpigotUpdater(plugin, 76893);
		        	try {
		                if (updater.checkForUpdates()) {
		                	log.alert("An update was found! for PixelLogin. Please update to recieve latest version. download: " + updater.getResourceURL());
		                }	
		            } catch (Exception e) {
		            	
		            	log.error("Failed to check for a update on spigot.");
		            }
				}
        		
        	}.runTask(this);
        	
        	
        } 
    }
	
	
	
	public void addEffects(Player player)
	{
		new BukkitRunnable() {
            @Override
            public void run() {
            	player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 5));
        		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Integer.MAX_VALUE, 5));
            }
        }.runTask(plugin);
		
		
		
		
	}
	  
	public void removeEffects(Player player)
	{
		new BukkitRunnable() {
            @Override
            public void run() {
            	player.removePotionEffect(PotionEffectType.BLINDNESS);
        		player.removePotionEffect(PotionEffectType.CONFUSION);
            }
        }.runTask(plugin);
		
		
	}


	@Override
	public CoreLog getLog() {
		// TODO Auto-generated method stub
		return this.log;
	}


	@Override
	public JavaPlugin getInstance() {
		// TODO Auto-generated method stub
		return this;
	}
	
}
