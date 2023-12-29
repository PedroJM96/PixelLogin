package com.pedrojm96.pixellogin.bukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pedrojm96.core.CoreColor;
import com.pedrojm96.core.CoreMaterial;





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
			ItemStack it = CoreMaterial.createItem(" ",CoreMaterial.getMaterial("STAINED_GLASS_PANE",dye.get(this.color)),this.color);
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
			menu.setItem(2, CoreMaterial.createItem(1,"&7*",CoreMaterial.getMaterial("BARRIER"),0));
		}else {
			menu.setItem(2, CoreMaterial.createItem(1,"&a*",CoreMaterial.getMaterial("INK_SACK","INK_SAC"),10));
		}
		
		if(ping.size() <=1) {
			menu.setItem(3, CoreMaterial.createItem(1,"&7*",CoreMaterial.getMaterial("BARRIER"),0));
		}else {
			menu.setItem(3, CoreMaterial.createItem(1,"&a*",CoreMaterial.getMaterial("INK_SACK","INK_SAC"),10));
		}
		
		if(ping.size() <=2) {
			menu.setItem(4, CoreMaterial.createItem(1,"&7*",CoreMaterial.getMaterial("BARRIER"),0));
		}else {
			menu.setItem(4, CoreMaterial.createItem(1,"&a*",CoreMaterial.getMaterial("INK_SACK","INK_SAC"),10));
		}
		
		if(ping.size() <=3) {
			menu.setItem(5, CoreMaterial.createItem(1,"&7*",CoreMaterial.getMaterial("BARRIER"),0));
		}else {
			menu.setItem(5, CoreMaterial.createItem(1,"&a*",CoreMaterial.getMaterial("INK_SACK","INK_SAC"),10));
		}
		
		if(ping.size() <=4) {
			menu.setItem(6, CoreMaterial.createItem(1,"&7*",CoreMaterial.getMaterial("BARRIER"),0));
		}else {
			menu.setItem(6, CoreMaterial.createItem(1,"&a*",CoreMaterial.getMaterial("INK_SACK","INK_SAC"),10));
		}
		if(this.enableKey) {
			menu.setItem(20, CoreMaterial.createItem(1,AllString.pins_numbers.get(0),CoreMaterial.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(21, CoreMaterial.createItem(1,AllString.pins_numbers.get(1),CoreMaterial.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(22, CoreMaterial.createItem(1,AllString.pins_numbers.get(2),CoreMaterial.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(23, CoreMaterial.createItem(1,AllString.pins_numbers.get(3),CoreMaterial.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(24, CoreMaterial.createItem(1,AllString.pins_numbers.get(4),CoreMaterial.getMaterial("INK_SACK","INK_SAC"),8));
		    
		    menu.setItem(29, CoreMaterial.createItem(1,AllString.pins_numbers.get(5),CoreMaterial.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(30, CoreMaterial.createItem(1,AllString.pins_numbers.get(6),CoreMaterial.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(31, CoreMaterial.createItem(1,AllString.pins_numbers.get(7),CoreMaterial.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(32, CoreMaterial.createItem(1,AllString.pins_numbers.get(8),CoreMaterial.getMaterial("INK_SACK","INK_SAC"),8));
		    menu.setItem(33, CoreMaterial.createItem(1,AllString.pins_numbers.get(9),CoreMaterial.getMaterial("INK_SACK","INK_SAC"),8));
		}else {
			menu.setItem(20, CoreMaterial.createItem(1," ",CoreMaterial.getMaterial("BARRIER"),0));
		    menu.setItem(21, CoreMaterial.createItem(1," ",CoreMaterial.getMaterial("BARRIER"),0));
		    menu.setItem(22, CoreMaterial.createItem(1," ",CoreMaterial.getMaterial("BARRIER"),0));
		    menu.setItem(23, CoreMaterial.createItem(1," ",CoreMaterial.getMaterial("BARRIER"),0));
		    menu.setItem(24, CoreMaterial.createItem(1," ",CoreMaterial.getMaterial("BARRIER"),0));
		    
		    menu.setItem(29, CoreMaterial.createItem(1," ",CoreMaterial.getMaterial("BARRIER"),0));
		    menu.setItem(30, CoreMaterial.createItem(1," ",CoreMaterial.getMaterial("BARRIER"),0));
		    menu.setItem(31, CoreMaterial.createItem(1," ",CoreMaterial.getMaterial("BARRIER"),0));
		    menu.setItem(32, CoreMaterial.createItem(1," ",CoreMaterial.getMaterial("BARRIER"),0));
		    menu.setItem(33, CoreMaterial.createItem(1," ",CoreMaterial.getMaterial("BARRIER"),0));
		}
	    menu.setItem(48, CoreMaterial.createItem(1,AllString.pin_backspace, Material.CLAY_BALL,0));
	    menu.setItem(50, CoreMaterial.createItem(1,AllString.pin_ok,Material.CLAY_BALL,0));
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
