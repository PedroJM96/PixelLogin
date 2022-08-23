package com.pedrojm96.pixellogin.bukkit;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CaptchaRender  extends MapRenderer{

	private PixelLoginBukkit plugin;
	
	private  List<MapRenderer> old;
	
	private MapView map;

    private boolean render;
    
    
    public CaptchaRender(PixelLoginBukkit p, List<MapRenderer> o, MapView m) {
    	this.plugin = p;
    	this.old = o;
    	this.map = m;
    }
	
	@Override
	public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
		// TODO Auto-generated method stub
	   if(render) {
		 return;
	   }
		
       if (player == null) {;
       	return;
       }
       
       if(!plugin.captchas.containsKey(player.getName())) {
       		old.forEach(map::addRenderer);
       }else {
    	   render = true;
    	   mapCanvas.drawImage(0, 0, plugin.captchas.get(player.getName()).getImage());
       } 
	}

}
