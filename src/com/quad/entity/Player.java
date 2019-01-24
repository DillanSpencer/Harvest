package com.quad.entity;

import java.util.ArrayList;

import com.quad.Tile.Tile;
import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.components.GameObject;
import com.quad.core.fx.Image;
import com.quad.inventory.Inventory;

public class Player extends GameObject{
	
	// animations
	private ArrayList<Image[]> sprites;
	private ArrayList<Enemy> enemies;
	
	//inventory
	Inventory inventory;
	private final int[] NUMFRAMES = {
		3, 3, 3, 4, 4,4,4, 2, 3, 3,3
	};
	private final int[] FRAMEWIDTHS = {
		17, 17, 18, 24 ,27,25,25,15,17,17,17
	};
	private final int[] FRAMEHEIGHTS = {
		24, 25, 25, 24, 23,23,23,25,25,25,25
	};
	private final int[] SPRITEDELAYS = {
		7, 8, 7, 6, 6,6,6, 8,6,6,6
	};
	
	// animation
	public final int DOWN = 0;
	public final int LEFT = 1;
	public final int RIGHT = 1;
	public final int UP = 2;
	public final int CUTDOWN = 3;
	public final int CUTUP = 4;
	public final int CUTRIGHT = 5;
	public final int CUTLEFT = 6;
	public final int SPRINTDOWN = 7;
	public final int SPRINTLEFT = 8;
	public final int SPRINTRIGHT = 9;
	public final int SPRINTUP = 10;
	
	//running
	private boolean sprinting;
	private boolean stairs;
	
	
	//event
	private boolean eventCut;
	private boolean canInput;

	public Player(TileMap tm, ArrayList<Enemy> en) {
		super(tm);
		
		this.enemies = en;
		
		width = 16;
		height = 16;
		cwidth = 16;
		cheight = 16;
		animCount = 0;
		
		moveSpeed = 1.2;
		canInput = true;
		sprinting = false;
		
		//inventory
		inventory = new Inventory(this);
		
		// load sprites
					try {
						
						Image spritesheet = new Image(
								"/player/player.gif"
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
					
	}
	
	public Inventory getInventory() {return inventory;}
	public void setSprinting(boolean s) {sprinting = s;}
	public boolean isSprinting() {return sprinting;}
	
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
	
	//move on spot
	public void down() {setAnimation(DOWN);}
	public void left() {
		facingRight = true;
		setAnimation(LEFT);
	}
	public void right() {
		setAnimation(RIGHT);
		facingRight = false;
	}
	public void up() {setAnimation(UP);}
	
	public boolean canInput() {return canInput;}
	
	private boolean validatePosition() {
		
		if(moving) return true;
		
		rowTile = (int) (y / tileSize);
		colTile = (int) (x / tileSize);
		for(Enemy e : enemies) {
			if(left) {
				if(colTile == 0 || tileMap.getType(rowTile, colTile - 1) == Tile.BLOCKED) {
					return false;
				}
				else if(intersects(e)) {
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
				else if(intersects(e)) {
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
				else if(intersects(e)) {
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
				else if(intersects(e)) {
					return false;
				}
				else {
					ydest = y + tileSize;
				}
			}
		}
		
		return true;
	}

	
	public void init() {
		
	}
	
	public void setAnimation(int i) {
		previousAction = currentAction;
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAYS[currentAction]);
		width = FRAMEWIDTHS[currentAction];
		height = FRAMEHEIGHTS[currentAction];
	}
	
	public void cutTree() {
		//up
		if(currentAction == UP && tileMap.getIndex(rowTile - 1, colTile) < 20) {
			tileMap.setTile(rowTile - 1, colTile, 20);
		}
		//down
		if(currentAction == DOWN && tileMap.getIndex(rowTile + 1, colTile) < 20) {
			tileMap.setTile(rowTile + 1, colTile, 20);
		}
		//left
		if(currentAction == LEFT && tileMap.getIndex(rowTile, colTile - 1) < 20) {
			tileMap.setTile(rowTile, colTile - 1, 20);
		}
		//right
		if(currentAction == RIGHT && tileMap.getIndex(rowTile, colTile + 1) < 20) {
			tileMap.setTile(rowTile, colTile + 1, 20);
		}
	}
	
	public void setAction() {
		canInput = false;
		down = up = right = left = false;
		eventCut = true;
		///////CUT DOWN TREE///////////////
		if(currentAction == UP && tileMap.getIndex(rowTile - 1, colTile) == 20) {
			tileMap.setTile(rowTile - 1, colTile, 5);
		}
		//down
		if(currentAction == DOWN && tileMap.getIndex(rowTile + 1, colTile) == 20) {
			tileMap.setTile(rowTile + 1, colTile, 5);
		}
		//left
		if(currentAction == LEFT && tileMap.getIndex(rowTile, colTile - 1) == 20) {
			tileMap.setTile(rowTile, colTile - 1, 5);
		}
		//right
		if(currentAction == RIGHT && tileMap.getIndex(rowTile, colTile + 1) == 20) {
			tileMap.setTile(rowTile, colTile + 1, 5);
		}
	}
	
	public void update(GameContainer gc, float dt) {
		
		//inventory
		inventory.update(gc, dt);
		if(inventory.isActive()) return;
		
		if(!moving) sprinting = false;
		if(sprinting) {
			moveSpeed = 1.5;
		}else if(stairs){
			moveSpeed = 0.5;
		}
		else {
			moveSpeed = 1.2;
		}
		//Check to see if player is on stair tile
		if(tileMap.getIndex(rowTile, colTile) == 8 || tileMap.getIndex(rowTile, colTile) == 9 ||
				tileMap.getIndex(rowTile, colTile) == 10 || tileMap.getIndex(rowTile, colTile) == 11) {
			stairs = true;
		}else {
			stairs = false;
		}
		
		//animation
		
		if(sprinting) {
			if(down) {
				animation.setDelay(SPRITEDELAYS[currentAction]);
				if(currentAction != SPRINTDOWN)
				setAnimation(SPRINTDOWN);
			}
			else if(right) {
				animation.setDelay(SPRITEDELAYS[currentAction]);
				if(currentAction != SPRINTRIGHT)
				setAnimation(SPRINTRIGHT);
				facingRight = false;
			}
			else if(up) {
				animation.setDelay(SPRITEDELAYS[currentAction]);
				if(currentAction != SPRINTUP)
				setAnimation(SPRINTUP);
			}
			else if(left) {
				animation.setDelay(SPRITEDELAYS[currentAction]);
				if(currentAction != SPRINTLEFT)
				setAnimation(SPRINTLEFT);
				facingRight = true;
			}
			else if(!down && !up && !right && !left) {
				animation.setDelay(-1);
				animation.setFrame(1);
			}
		}
		else {
			if(down) {
				animation.setDelay(SPRITEDELAYS[currentAction]);
				if(currentAction != DOWN)
				setAnimation(DOWN);
			}
			else if(right) {
				animation.setDelay(SPRITEDELAYS[currentAction]);
				if(currentAction != RIGHT)
				setAnimation(RIGHT);
				facingRight = false;
			}
			else if(up) {
				animation.setDelay(SPRITEDELAYS[currentAction]);
				if(currentAction != UP)
				setAnimation(UP);
			}
			else if(left) {
				animation.setDelay(SPRITEDELAYS[currentAction]);
				if(currentAction != LEFT)
				setAnimation(LEFT);
				facingRight = true;
			}else if(eventCut) {
				if(currentAction != CUTDOWN && currentAction == DOWN)
					setAnimation(CUTDOWN);
				else if(currentAction != CUTLEFT && currentAction == LEFT) {
					setAnimation(CUTLEFT);
				}
				else if(currentAction != CUTUP && currentAction == UP)
					setAnimation(CUTUP);
				else if(currentAction != CUTRIGHT && currentAction == RIGHT) {
					setAnimation(CUTRIGHT);
				}
			}
			else if(!down && !up && !right && !left) {
				animation.setDelay(-1);
				animation.setFrame(1);
			}
		}
		
		if(eventCut && animation.hasPlayedOnce()){
			setAnimation(previousAction);
			eventCut = false;
			canInput = true;
		}
		//check enemy
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// collision with enemy
			if(!e.isDead() && intersects(e)) {
				
			}
		}
		
		animation.update();
		super.updateComponents(gc, dt);
	}
	
	public void render(GameContainer gc, Renderer r) {
		
		super.renderComponents(gc, r);
	}
	
	public void postRender(GameContainer gc, Renderer r){
		//inventory
		inventory.render(gc, r);
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