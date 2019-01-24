package com.quad.core.components;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.quad.Tile.Tile;
import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.entity.Animation;


public abstract class GameObject
{
	// tile stuff
			protected TileMap tileMap;
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
			
			// dimensions
			protected int width;
			protected int height;
			
			// collision box
			protected int cwidth;
			protected int cheight;
			protected int coffx;
			protected int coffy;
			
			// collision
			protected int currRow;
			protected int currCol;
			protected double xdest;
			protected double ydest;
			protected double xtemp;
			protected double ytemp;
			protected boolean topLeft;
			protected boolean topRight;
			protected boolean bottomLeft;
			protected boolean bottomRight;
			
			// animation
			protected Animation animation;
			protected int animCount;
			protected int currentAction;
			protected int previousAction;
			protected boolean facingRight;
			
			// movement
			protected boolean left;
			protected boolean right;
			protected boolean up;
			protected boolean down;
			protected boolean jumping;
			protected boolean falling;
			protected boolean fallDamage;
			
			// movement attributes
			protected double moveSpeed;
			protected double maxSpeed;
			protected double stopSpeed;
			protected double fallSpeed;
			protected double maxFallSpeed;
			protected double jumpStart;
			protected double stopJumpSpeed;
			protected boolean moving;
			
			//attributes
			protected int health;
			protected int maxHealth;

		protected String tag = "null";
		protected boolean dead = false;
		protected boolean movement = true;
		protected ArrayList<Component> components = new ArrayList<Component>();
		
		public GameObject(TileMap tm) {
			tileMap = tm;
			tileSize = tm.getTileSize();
			animation = new Animation();
			facingRight = true;
		}
		
		public boolean willIntersect(int x, int y, GameObject o) {
			Rectangle r1 = new Rectangle((int)(getx() + x),(int)(gety() + y) , 1, 1);
			Rectangle r2 = new Rectangle(o.getx(),o.gety(),1,1);
			//System.out.println((o.getx() + x) + " " + (o.gety() + y) + "------" + );
			return r1.intersects(r2);
		}
		
		public boolean intersects(GameObject o) {
			Rectangle r1 = getRectangle();
			Rectangle r2 = o.getRectangle();
			return r1.intersects(r2);
		}
		
		public boolean intersects(Rectangle r) {
			return getRectangle().intersects(r);
		}
		
		public boolean contains(GameObject o) {
			Rectangle r1 = getRectangle();
			Rectangle r2 = o.getRectangle();
			return r1.contains(r2);
		}
		
		public boolean contains(Rectangle r) {
			return getRectangle().contains(r);
		}
		
		public Rectangle getRectangle() {
			return new Rectangle(
					(int)((x) - cwidth / 2),
					(int)((y) - cheight / 2)+coffy,
					cwidth,
					cheight
			);
		}
		
		public int getRow() { return rowTile; }
		public int getCol() { return colTile; }
		
		public boolean validateNextPosition() {
			
			if(moving) return true;
			
			rowTile = (int) (y / tileSize);
			colTile = (int) (x / tileSize);
			
			if(left) {
				if(colTile == 0 || tileMap.getType(rowTile, colTile - 1) == Tile.BLOCKED) {
					return false;
				}

				else {
					xdest = x - tileSize;
				}
			}
			if(right) {
				if(colTile == tileMap.getNumCols() || tileMap.getType(rowTile, colTile + 1) == Tile.BLOCKED) {
					return false;
				}
				else {
					xdest = x + tileSize;
				}
			}
			if(up) {
				if(rowTile == 0 || tileMap.getType(rowTile - 1, colTile) == Tile.BLOCKED) {
					return false;
				}
				else {
					ydest = y - tileSize;
				}
			}
			if(down) {
				if(rowTile == tileMap.getNumRows() - 1 || tileMap.getType(rowTile + 1, colTile) == Tile.BLOCKED) {
					return false;
				}
				else {
					ydest = y + tileSize;
				}
			}
			
			return true;
			
		}
		
		public void getNextPosition() {
			
			if(left && x > xdest) x -= moveSpeed;
			else left = false;
			if(left && x < xdest) x = xdest;
			
			if(right && x < xdest) x += moveSpeed;
			else right = false;
			if(right && x > xdest) x = xdest;
			
			if(up && y > ydest) y -= moveSpeed;
			else up = false;
			if(up && y < ydest) y = ydest;
			
			if(down && y < ydest) y += moveSpeed;
			else down = false;
			if(down && y > ydest) y = ydest;
			
		}
		
		public int getx() { return (int)x; }
		public int gety() { return (int)y; }
		public int getdx() {return (int)dx; }
		public int getdy() {return (int)dy; } 
		public int getWidth() { return width; }
		public int getHeight() { return height; }
		public int getCWidth() { return cwidth; }
		public int getCHeight() { return cheight; }
		public boolean isFacingRight() { return facingRight; }
		
		public void setPosition(double x, double y) {
			this.x = x;
			this.y = y;
			this.xdest = x;
			this.ydest = y;
		}
		
//		public void setAngle(Player p, double x, double y, boolean right){
//			if(right){
//				double distX = Math.abs(p.getx() - x);
//				double distY = Math.abs(p.gety() - y);
//				double time = 40;
//				
//				if(distX < 20)time=20;
//				else if(distX > 50) time = 60;
//				
//				dx = distX / time;
//				dy = -distY / time;
//			}else{
//				double distX = Math.abs(p.getx() - x);
//				double distY = Math.abs(p.gety() - y);
//				double time = 40;
//				
//				if(distX < 20)time=20;
//				else if(distX > 50) time = 60;
//				
//				dx = (distX / time)*-1;
//				dy = (distY / time)*-1;
//			}
//		}
		
		public void setVector(double dx, double dy) {
			this.dx = dx;
			this.dy = dy;
		}
		
		public void setMapPosition() {
			xmap = tileMap.getx();
			ymap = tileMap.gety();
		}
		
		public void setLeft() {
			if(moving) return;
			left = true;
			moving = validateNextPosition();
		}
		public void setRight() {
			if(moving) return;
			right = true;
			moving = validateNextPosition();
		}
		public void setUp() {
			if(moving) return;
			up = true;
			moving = validateNextPosition();
		}
		public void setDown() {
			if(moving) return;
			down = true;
			moving = validateNextPosition();
		}
		public void debug() {
			System.out.println(x);
			System.out.println(xdest);
		}
		
		public boolean notOnScreen() {
			return x + xmap + width < 0 ||
				x + xmap - width > GameContainer.width ||
				y + ymap + height < 0 ||
				y + ymap - height > GameContainer.height;
		}
		
		public void updateComponents(GameContainer gc, float dt)
		{
			// get next position
			if(moving) getNextPosition();
			
			//debug collision error
			if(moving == false) {
				xdest = x;
				ydest = y;
			}
						
			// check stop moving
			if(x == xdest && y == ydest) {
				left = right = up = down = moving = false;
				rowTile = (int) (y / tileSize);
				colTile = (int) (x / tileSize);
			}
			
			for(Component c : components)
			{
				c.update(gc, this, dt);
			}
			
		}
		
		public void renderComponents(GameContainer gc, Renderer r)
		{
			for(Component c : components)
			{
				c.render(gc, r);
			}
			
			setMapPosition();			
			
			if(facingRight) {
				r.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2),
					(int)(y + ymap - height / 2),
					width,
					height
				);
			}
			else {
				r.drawImageReverted(
					animation.getImage(),
					(int)(x + xmap - width / 2 + width),
					(int)(y + ymap - height / 2),
					width,
					height
				);
			}
			
			Rectangle re = getRectangle();
			re.x += xmap;
			re.y += ymap;
			
			//r.drawRect(re.x, re.y,re.width , re.height, 0xffffff, ShadowType.NONE);

			
		}
		
		public abstract void componentEvent(String name, GameObject object);
		public abstract void dispose();
		
		public void addComponent(Component c)
		{
			components.add(c);
		}
		
		public void removeComponent(String tag)
		{
			for(int i = 0; i < components.size(); i++)
			{
				if(components.get(i).getTag().equalsIgnoreCase(tag))
				{
					components.remove(i);
				}
			}
		}
		
		public String getTag()
		{
			return tag;
		}
		public void setTag(String tag)
		{
			this.tag = tag;
		}
		public boolean isDead()
		{
			return dead;
		}
		public void setDead(boolean dead)
		{
			this.dead = dead;
		}
		public boolean getMoving(){
			return moving;
		}
		public void setMovement(boolean b){
			movement = b;
			if(!b) left = right = jumping = falling = false;
		}
		public double getX()
		{
			return x;
		}
		public void setX(double x)
		{
			this.x = x;
		}
		public double getY()
		{
			return y;
		}
		public void setY(double y)
		{
			this.y = y;
		}
		public double getW()
		{
			return width;
		}
		public void setW(int w)
		{
			this.width = w;
		}
		public double getH()
		{
			return height;
		}
		public void setH(int h)
		{
			this.height = h;
		}
	
	
}
