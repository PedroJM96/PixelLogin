package com.pedrojm96.pixellogin.bukkit;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.pedrojm96.core.CoreMaterial;
import com.pedrojm96.core.CoreVersion;
import com.pedrojm96.core.bungee.CoreColor;

public class Captcha{
	private PixelLoginBukkit plugin;
	private Player player;
	private String answer;
    private BufferedImage image;
    
    private ItemStack[] contents;
    
    private ItemStack[] armour;
    
   
	public Captcha(PixelLoginBukkit plugin,Player player ) {
		 this.plugin = plugin;
		 this.player = player;
		 this.answer = RandomStringUtils.randomAlphabetic(5).toLowerCase();
		 this.image =  new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
		 //this.answer = RandomStringUtils.randomAlphabetic(5).toLowerCase();
		 Graphics2D graphics = (Graphics2D)image.getGraphics();
		 graphics.setColor(Color.WHITE);
		 graphics.fillRect(0, 0, 128, 128);
		 drawImage(graphics);
		 graphics.setFont(new Font("Arial", 1, 35));
		 drawTetx(graphics);
		
		 graphics.dispose();

	 }
	 

	 public BufferedImage getImage() {
		 return this.image;
	 }

	 
	 public void drawImage(Graphics2D graphics) {
		 try {
		      File file = new File(plugin.getDataFolder(), "background.png");
		      BufferedImage background = ImageIO.read(file);
		      AffineTransform affineTransform = new AffineTransform();
		      graphics.drawImage(background, affineTransform, (ImageObserver)null);
		  } catch (Exception exception) {
		      exception.printStackTrace();
		  } 
	 }
	 
	 public void drawTetx(Graphics2D graphics) {
		 graphics.setColor(Color.BLACK);
		 graphics.drawString(this.answer, (int)((image.getWidth() - graphics.getFontMetrics().getStringBounds(this.answer, graphics).getWidth()) / 2.0D), 105);
	 }
	 
	 public void drawLine(Graphics2D graphics) {
		 int lines = ThreadLocalRandom.current().nextInt(10, 25);
	     while (lines != 0) {
	        graphics.setColor(getRandomColor());
	        lines--;
	        graphics.drawLine(getRandomCoordinate(), getRandomCoordinate(), getRandomCoordinate(), getRandomCoordinate());
	     } 
	 }
	 
	 public int getRandomCoordinate() {
		    return ThreadLocalRandom.current().nextInt(0, 128);
	 }
	 
	 public void resetInventory() {
		 	this.player.getInventory().setContents(this.contents);
		    this.player.getInventory().setArmorContents(this.armour);
		    this.player.updateInventory();	
		 
     }

	@SuppressWarnings("deprecation")
	private void sendMapPre1_13(Player player) {
		
		 this.contents = player.getInventory().getContents();
	     this.armour = player.getInventory().getArmorContents();
		 this.player.getInventory().clear();
		 this.player.getInventory().setArmorContents(null);
		 ItemStack map = new ItemStack(Material.MAP);
		 MapView view = Bukkit.createMap(player.getWorld());
		 List<MapRenderer> old = view.getRenderers();
		 view.getRenderers().forEach(view::removeRenderer);
		 view.addRenderer(new CaptchaRender(this.plugin,old,view));
		 ItemMeta meta = map.getItemMeta();
		 meta.setDisplayName(CoreColor.colorCodes("&aCaptcha"));
		 map.setDurability((short) view.getId());
		 map.setItemMeta(meta); 
		 
		 player.updateInventory();
		 
	}
	
	private void sendMap(Player player) {
		
		 if(CoreVersion.getVersion().esMayorIgual(CoreVersion.v1_13)) {
			 sendMapPos1_13(player);
		 }else {
			 sendMapPre1_13(player);
		 }
		 
	}
	
	
	@SuppressWarnings("deprecation")
	private void sendMapPos1_13(Player player) {
		
		 this.contents = player.getInventory().getContents();
	     this.armour = player.getInventory().getArmorContents();
		 this.player.getInventory().clear();
		 this.player.getInventory().setArmorContents(null);
		 ItemStack map = new ItemStack(CoreMaterial.getMaterial("FILLED_MAP"));
		 MapView view = Bukkit.createMap(player.getWorld());
		 MapMeta mapMeta = (MapMeta)map.getItemMeta();
		 mapMeta.setMapId(view.getId());
		 map.setItemMeta(mapMeta);
		 List<MapRenderer> old = view.getRenderers();
		 view.getRenderers().clear();
		 view.addRenderer(new CaptchaRender(this.plugin,old,view));
		 ItemMeta meta = map.getItemMeta();
		 meta.setDisplayName(CoreColor.colorCodes("&aCaptcha"));
		 map.setDurability((short) view.getId());
		 map.setItemMeta(meta); 
		 this.setItemInHand(map);
		 player.updateInventory();
		 
	}
	
	
	public void setItemInHand(ItemStack item) {
		if(CoreVersion.getVersion().esMayorIgual(CoreVersion.v1_9)) {
			this.setItemInHand_1_9(item);
		}else {
			this.setItemInHand_1_8(item);
		}
	}
	
	
	public ItemStack getItemInHand() {
		if(CoreVersion.getVersion().esMayorIgual(CoreVersion.v1_9)) {
			return this.getItemInHand_1_9();
		}else {
			return this.getItemInHand_1_8();
		}
	}
	
	public void setItemInHand_1_8(ItemStack item) {
		
		try {
			 player.getInventory().getClass().getMethod("setItemInHand",item.getClass()).invoke(player.getInventory(),item);
		
		} catch (Exception e)
	    {
		      e.printStackTrace();
		}	
	}

	public void setItemInHand_1_9(ItemStack item) {
		
		try {
			player.getInventory().getClass().getMethod("setItemInMainHand",item.getClass()).invoke(player.getInventory(),item);
		} catch (Exception e)
	    {
		      e.printStackTrace();
		}	
	}
	
	
	public ItemStack getItemInHand_1_8() {
		
		try {
			Object item = player.getInventory().getClass().getMethod("getItemInHand").invoke(player.getInventory());
			return (ItemStack) item;
		} catch (Exception e)
	    {
		      e.printStackTrace();
		}
		return null;	
	}
	
	public ItemStack getItemInHand_1_9() {
		
		try {
			Object item = player.getInventory().getClass().getMethod("getItemInMainHand").invoke(player.getInventory());
			return (ItemStack) item;
		} catch (Exception e)
	    {
		      e.printStackTrace();
		}
		return null;	
	}
	
	
	 
	 public void sendMap() {
		    
		 
		 
		 
		 this.sendMap(this.player);
	 }
	 
	 public Color getRandomColor() {
		    return new Color(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255));
	}
	 
	 public String getAnswer() {
		 return this.answer;
	 }
	
}
