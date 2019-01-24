package com.quad.hud;

import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.fx.Content;
import com.quad.core.fx.Image;
import com.quad.core.fx.ShadowType;
import com.quad.entity.Player;
import com.quad.entity.SaveInfo;

public class HUD {
	
	//health
	private Image heart = Content.Heart[0][0];
	private Player p;
	
	public HUD(Player p){
		this.p = p;
		heart.shadowType = ShadowType.NONE;
	}
	
	
	public void render(GameContainer gc, Renderer r){
		//render lives
	}

}
