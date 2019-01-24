package com.quad.items;

import java.awt.event.KeyEvent;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.components.GameObject;
import com.quad.core.fx.Content;
import com.quad.core.fx.Image;
import com.quad.entity.InventoryItem;
import com.quad.entity.Item;
import com.quad.entity.Player;

public class Wood extends Item{

	//player
			private Player player;
			
			//sprites
			private Image[] sprites;

			public Wood(TileMap tm, Player p) {
				super(tm);

				player = p;
							
				moveSpeed = 0.4;
				fallSpeed = 0.15;
				maxFallSpeed = 4.0;
				jumpStart = -5;
				
				width = 16;
				height = 16;
				cwidth = 16;
				cheight = 16;
				
				sprites = Content.Wood[0];
				
				animation.setFrames(sprites);
				animation.setNumFrames(1);
				animation.setDelay(-1);
			}
			

			public void update(GameContainer gc, float dt){
				updateComponents(gc, dt);
				
				//check to see if player collides with item, and then adds one health to the player
				if(getRectangle().intersects(player.getRectangle()) && gc.getInput().isKeyPressed(KeyEvent.VK_C)){
					remove = true;
					player.getInventory().addItem(InventoryItem.woodItem);
				}
			
				animation.update();
				
			}
			
			public void render(Renderer r, GameContainer gc){
				renderComponents(gc, r);
			}

			@Override
			public void componentEvent(String name, GameObject object) {
				
			}

			@Override
			public void dispose() {
				
			}

}
