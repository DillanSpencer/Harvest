package com.quad.Tile;

import java.awt.Rectangle;

import com.quad.core.GameContainer;
import com.quad.entity.Animation;

public class GameTile {
	
	protected TileMap tm;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	// position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	protected int rowTile;
	protected int colTile;
	
	//collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	
	// dimensions
	protected int width;
	protected int height;
	
	//attributes
	protected int health;
	protected int maxHealth;
	protected boolean remove;
	
	public GameTile(TileMap tm) {
		this.tm = tm;
		tileSize = tm.getTileSize();
		remove = false;
	}
	
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getXmap() {return (int)xmap;}
	public int getYmap() {return (int)xmap;}
	public int getdx() {return (int)dx; }
	public int getdy() {return (int)dy; } 
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		this.xdest = x;
		this.ydest = y;
		setMapPosition();
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(
				(int)((x) - width / 2),
				(int)((y) - height / 2),
				width,
				height
		);
	}
	
	public void setMapPosition() {
		xmap = tm.getx();
		ymap = tm.gety();
	}
	
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
			x + xmap - width > GameContainer.width ||
			y + ymap + height < 0 ||
			y + ymap - height > GameContainer.height;
	}
	
	public void hit(int damage) {
		health -= damage;
		if(health <= 0) remove = true;
	}

}
