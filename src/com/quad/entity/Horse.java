package com.quad.entity;

import java.util.ArrayList;
import java.util.Random;

import com.quad.Tile.Tile;
import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.components.GameObject;
import com.quad.core.fx.Image;
import com.sun.glass.events.KeyEvent;

public class Horse extends Enemy{

	private Player player;
	private boolean active;
	private int eventCount;
	
	private Enemy col;
	
	private static int DOWN = 0;	
	private static int UP = 1;
	private static int RIGHT = 2;
	private static int LEFT = 2;
	
	private ArrayList<Image[]> sprites;
	private final int[] NUMFRAMES = {
		8, 9, 6, 6
	};
	private final int[] FRAMEWIDTHS = {
		16, 16, 30, 29
	};
	private final int[] FRAMEHEIGHTS = {
		26, 29, 24, 24
	};
	private final int[] SPRITEDELAYS = {
		10, 10, 10, 10
	};
	
	public Horse(TileMap tm, Player p) {
		
		super(tm);
		
		player = p;
		
		health = maxHealth = 3;
		
		width = 30;
		height = 29;
		cwidth = 32;
		cheight = 35;
		eventCount = 0;
		moveSpeed = 0.1;
		
		
		try {
			
			Image spritesheet = new Image(
					"/enemies/horse2.gif"
				);
			
			int count = 0;
			sprites = new ArrayList<Image[]>();
			for(int i = 0; i < NUMFRAMES.length; i++) {
				Image[] bi = new Image[NUMFRAMES[i]];
				for(int j = 0; j < NUMFRAMES[i]; j++) {
					bi[j] = spritesheet.getSubimage(
						j * FRAMEWIDTHS[i],
						count,
						FRAMEWIDTHS[i],
						FRAMEHEIGHTS[i]
					);
				}
				sprites.add(bi);
				count += FRAMEHEIGHTS[i];
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		setAnimation(DOWN);
				
		left = true;
		facingRight = false;
		
	}
	
	public void init(TileMap tm) {
		col = new Enemy(tm);
		col.setW(10);
		col.setH(10);
	}
	
	public void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAYS[currentAction]);
		width = FRAMEWIDTHS[currentAction];
		height = FRAMEHEIGHTS[currentAction];
	}
	
	public void moveDown() {
		if(moving) return;
		down = true;
		moving = validatePosition();
	}
	public void moveLeft() {
		if(moving) return;
		left = true;
		moving = validatePosition();
	}
	public void moveRight() {
		super.setRight();
		right = true;
		moving = validatePosition();
	}
	public void moveUp() {
		if(moving) return;
		up = true;
		moving = validatePosition();
	}
	
	private boolean validatePosition() {
		
		if(moving) return true;
		
		rowTile = (int) (y / tileSize);
		colTile = (int) (x / tileSize);
		
		if(left) {
			if(colTile == 0 || tileMap.getType(rowTile, colTile - 1) == Tile.BLOCKED) {
				return false;
			}
			else if(intersects(player)) {
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
			else if(intersects(player)) {
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
			else if(intersects(player)) {
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
			else if(intersects(player)) {
				return false;
			}
			else {
				ydest = y + tileSize;
			}
		}
		
		return true;
	}
	
	
	public void update(GameContainer gc, float dt) {
		
		super.updateComponents(gc, dt);
		
		active = true;
		
		eventMove();
		
		if(down) {
			animation.setDelay(SPRITEDELAYS[currentAction]);
			col.setPosition(this.getX(), this.getY()-tileSize);
			if(currentAction != DOWN)
			setAnimation(DOWN);
		}
		else if(right) {
			animation.setDelay(SPRITEDELAYS[currentAction]);
			col.setPosition(this.getX()-tileSize, this.getY());
			if(currentAction != RIGHT)
			setAnimation(RIGHT);
			facingRight = false;
		}
		else if(up) {
			animation.setDelay(SPRITEDELAYS[currentAction]);
			col.setPosition(this.getX(), this.getY()+tileSize);
			if(currentAction != UP)
			setAnimation(UP);
		}
		else if(left) {
			animation.setDelay(SPRITEDELAYS[currentAction]);
			col.setPosition(this.getX()+ tileSize, this.getY());
			if(currentAction != LEFT)
			setAnimation(LEFT);
			facingRight = true;
		}
		else if(!down && !up && !right && !left) {
			animation.setDelay(-1);
			animation.setFrame(0);
		}
		
		getNextPosition();
		
		// update animation
		if(gc.getInput().isKey(KeyEvent.VK_S)) super.setDown();
		if(gc.getInput().isKey(KeyEvent.VK_W)) super.setUp();
		if(gc.getInput().isKey(KeyEvent.VK_A)) super.setLeft();
		if(gc.getInput().isKey(KeyEvent.VK_D)) super.setRight();
		
		
		animation.update();
		
	}
	
	public void eventMove() {
		eventCount++;
		
		if(eventCount == 75) {
			int rand = new Random().nextInt(4);
			
			if(rand == 0) moveLeft();
			if(rand == 1) moveRight();
			if(rand == 2) moveUp();
			if(rand == 3) moveDown();
			eventCount = 0;
		}
	}
	
	public void render(GameContainer gc, Renderer r) {
		
		super.renderComponents(gc, r);
		
	}
	
	public Enemy getObject() {
		return col;
	}

	@Override
	public void componentEvent(String name, GameObject object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
