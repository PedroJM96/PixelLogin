package com.pedrojm96.pixellogin.bukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pedrojm96.core.CoreColor;
import com.pedrojm96.core.CoreUtils;





public class SimpleGUI {

	private String title;
	private int slot;
	private Inventory menu;
	private int color;
	
	private boolean enableKey = true;
	
	private List<String> ping = new ArrayList<String>();
	private List<String> dye = new ArrayList<String>();
	
	public boolean isEnableKey() {
		return this.enableKey;
	}
	
	public void setEnableKey(boolean bo) {
		this.enableKey = bo;
	}
	
	public int getPinSize() {
		return ping.size();
	}
	
	public String getPinCode() {
		String code = " ";
		for(int i = 0; i<ping.size() ; i++) {
			code = code + ping.get(i).trim();
		}
		return code.trim();
	}
	
	public void addPin(int pi) {
		ping.add(String.valueOf(pi));
		
		if(ping.size() >= 5) {
			this.enableKey = false;
		}
	}
	
	public void remPin() {
		if(ping.size() > 0) {
			ping.remove(ping.size()-1);
		}
		
		
		if(ping.size() < 5) {
			this.enableKey = true;
		}
	}
	
	public SimpleGUI(String title, int row) {
		this.title = title;
		this.slot = getSlot(row);
		this.menu = Bukkit.createInventory(null,this.slot, CoreColor.colorCodes(this.title));
	}
	public SimpleGUI(String title, int row,int glasscolor) {
		dye();
		this.title = title;
		this.slot = getSlot(row);
		this.menu = Bukkit.createInventory(null,this.slot, CoreColor.colorCodes(this.title));
		this.color = glasscolor;
		for (int i = 0; i < slot; i++)
		{
			ItemStack it = createItem(" ",CoreUtils.getMaterial("STAINED_GLASS_PANE",dye.get(this.color)),this.color);
		    menu.setItem(i, it);
		}
		
		
	}
	public void open(Player p) {
		update();
		p.openInventory(menu);
	}
	public void reset() {
		ping = new ArrayList<String>();
		this.enableKey = true;
	}
	public void update() {
		if(ping.size() <=0) {
			menu.setItem(2, createItem(1,"&7*",Material.BARREL,0));
		}else {
			menu.setItem(2, createItem(1,"&a*",CoreUtils.getMaterial("INK_SACK","INK_SAC"),10));
		}
		
		if(ping.size() <=1) {
			menu.setItem(3, createItem(1,"&7*",Material.BARREL,0));
		}else {
			menu.setItem(3, createItem(1,"&a*",CoreUtils.getMaterial("INK_SACK","INK_SAC"),10));
		}
		
		if(ping.size() <=2) {
			menu.setItem(4, createItem(1,"&7*",Material.BARREL,0));
		}else {
			menu.setItem(4, createItem(1,"&a*",CoreUtils.getMaterial("INK_SACK","INK_SAC"),10));
		}
		
		if(ping.size() <=3) {
			menu.setItem(5, createItem(1,"&7*",Material.BARREL,0));
		}else {
			menu.setItem(5, createItem(1,"&a*",CoreUtils.getMaterial("INK_SACK","INK_SAC"),10));
		}
		
		if(ping.size() <=4) {
			menu.setItem(6, createItem(1,"&7*",Material.BARREL,0));
		}else {
			menu.setItem(6, createItem(1,"&a*",CoreUtils.getMaterial("INK_SACK","INK_SAC"),10));
		}
		if(this.enableKey) {
			menu.setItem(20, createItem(1,AllString.pins_numbers.get(0),CoreUtils.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(21, createItem(1,AllString.pins_numbers.get(1),CoreUtils.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(22, createItem(1,AllString.pins_numbers.get(2),CoreUtils.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(23, createItem(1,AllString.pins_numbers.get(3),CoreUtils.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(24, createItem(1,AllString.pins_numbers.get(4),CoreUtils.getMaterial("INK_SACK","INK_SAC"),8));
		    
		    menu.setItem(29, createItem(1,AllString.pins_numbers.get(5),CoreUtils.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(30, createItem(1,AllString.pins_numbers.get(6),CoreUtils.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(31, createItem(1,AllString.pins_numbers.get(7),CoreUtils.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(32, createItem(1,AllString.pins_numbers.get(8),CoreUtils.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(33, createItem(1,AllString.pins_numbers.get(9),CoreUtils.getMaterial("INK_SACK","INK_SAC"),8));
		}else {
			menu.setItem(20, createItem(1," ",Material.BARREL,0));
		    menu.setItem(21, createItem(1," ",Material.BARREL,0));
		    menu.setItem(22, createItem(1," ",Material.BARREL,0));
		    menu.setItem(23, createItem(1," ",Material.BARREL,0));
		    menu.setItem(24, createItem(1," ",Material.BARREL,0));
		    
		    menu.setItem(29, createItem(1," ",Material.BARREL,0));
		    menu.setItem(30, createItem(1," ",Material.BARREL,0));
		    menu.setItem(31, createItem(1," ",Material.BARREL,0));
		    menu.setItem(32, createItem(1," ",Material.BARREL,0));
		    menu.setItem(33, createItem(1," ",Material.BARREL,0));
		}
	    menu.setItem(48, createItem(1,AllString.pin_backspace, Material.CLAY_BALL,0));
	    menu.setItem(50, createItem(1,AllString.pin_ok,Material.CLAY_BALL,0));
	}

	@SuppressWarnings("deprecation")
	public static ItemStack createItem(String name,List<String> lore,String mate,int shrt) {
		ItemStack i = new ItemStack(Material.getMaterial(mate),1,(short)shrt);
		ItemMeta im = i.getItemMeta();
		name = CoreColor.colorCodes(name);
		im.setDisplayName(name);
		lore = CoreColor.rColorList(lore);
		im.setLore(lore);
		i.setItemMeta(im);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem(String name,Material mate,int shrt) {
		ItemStack i = new ItemStack(mate,1,(short)shrt);
		ItemMeta im = i.getItemMeta();
		String n = CoreColor.colorCodes(name);
		im.setDisplayName(n);
		i.setItemMeta(im);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem(int amount ,String name,Material mate,int shrt) {
		ItemStack i = new ItemStack(mate,amount,(short)shrt);
		ItemMeta im = i.getItemMeta();
		String n = CoreColor.colorCodes(name);
		im.setDisplayName(n);
		i.setItemMeta(im);
		return i;
	}
	
	public int getSlot(int rows){
		if (rows <= 0) {
	        int s = 9;
	        return s;
	     }else if(rows > 6){
	    	 int s = 54;
	    	return s;
	     }else{
	    	 int s = rows * 9;
	    	 return s;
	     }
	}
	
	
	private void dye() {
		//0
		dye.add("WHITE_STAINED_GLASS_PANE");
		//1
		dye.add("ORANGE_STAINED_GLASS_PANE");
		//2
		dye.add("MAGENTA_STAINED_GLASS_PANE");
		//3
		dye.add("LIGHT_BLUE_STAINED_GLASS_PANE");
		//4
		dye.add("YELLOW_STAINED_GLASS_PANE");
		//5
		dye.add("LIME_STAINED_GLASS_PANE");
		//6
		dye.add("PINK_STAINED_GLASS_PANE");
		//7
		dye.add("GRAY_STAINED_GLASS_PANE");
		//8
		dye.add("LIGHT_GRAY_STAINED_GLASS_PANE");
		//9
		dye.add("CYAN_STAINED_GLASS_PANE");
		//10
		dye.add("PURPLE_STAINED_GLASS_PANE");
		//11
		dye.add("BLUE_STAINED_GLASS_PANE");
		//12
		dye.add("BROWN_STAINED_GLASS_PANE");
		//13
		dye.add("GREEN_STAINED_GLASS_PANE");
		//14
		dye.add("RED_STAINED_GLASS_PANE");
		//15
		dye.add("BLACK_STAINED_GLASS_PANE");
	}
	
}
