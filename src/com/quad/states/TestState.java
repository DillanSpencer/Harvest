package com.quad.states;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.quad.Tile.TileMap;
import com.quad.Tile.Tree;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.components.State;
import com.quad.core.fx.Light;
import com.quad.entity.Enemy;
import com.quad.entity.Fly;
import com.quad.entity.Horse;
import com.quad.entity.Item;
import com.quad.entity.Player;
import com.quad.hud.DayCycle;
import com.quad.items.Wood;
import com.quad.items.craftable.Craft;
import com.sun.glass.events.KeyEvent;

public class TestState extends State{
	
	//lists
	private ArrayList<Enemy> gameObjects;
	private ArrayList<Fly> flies;
	private ArrayList<Item> items;
	// transition box
	private ArrayList<Rectangle> boxes;
	
	//player
	private Player player;
	
	private Rectangle r = new Rectangle();
	
	
	//horse
	private Horse horse;
	
	//tilemap
	private TileMap tm;
	
	//Day and night cycle
	private DayCycle cycle;
	
	//Craft
	private Craft craft;
	
	//tree
	private Tree tree;
	
	
	//event
	private boolean eventStart;
	private boolean eventMove;
	boolean isDown;
	private int eventTick;
	
	@Override
	public void init(GameContainer gc) {
		// TODO Auto-generated method stub
		
		Settings.setLight(gc, true);
		
		tm = new TileMap(gc, 16);
		tm.loadTiles("/tiles/harvesttiles.gif");
		tm.loadMap("/maps/test.map");
		tm.loadGameTiles();
		
		
		gameObjects = new ArrayList<Enemy>();
		items = new ArrayList<Item>();
		flies = new ArrayList<Fly>();
		
		//cycle
		cycle = new DayCycle();
		cycle.setTime(DayCycle.NIGHT);
		cycle.start();
		
		player = new Player(tm, gameObjects);
		player.setPosition(9*tm.getTileSize()+8, (8*tm.getTileSize())+4);
		
		//craft
		craft = new Craft(player.getInventory());
		
		initObjects();
		initFlies();
		initItems();
		
		tree = new Tree(tm);
		
		boxes = new ArrayList<Rectangle>();
		eventStart = true;
		eventMove = false;
	}
	
	private void initObjects() {
		Horse h;
		
		h = new Horse(tm, player);
		h.init(tm);
		h.setPosition(8*4, 8*16);
		gameObjects.add(h);
		
	}
	
	private void initFlies() {
		Fly f;
		
		f = new Fly(tm);
		f.init();
		f.setPosition(8*9, 8*25);
		flies.add(f);
		
		
		f = new Fly(tm);
		f.init();
		f.setPosition(8*7, 8*25);
		flies.add(f);
	}
	
	private void initItems() {
		Wood w;
		
		w = new Wood(tm, player);
		w.setPosition(100, 200);
		items.add(w);
	}

	@Override
	public void update(GameContainer gc, float dt) {
		// TODO Auto-generated method stub
		
		//event
		//if(eventStart) eventStart();
		if(eventMove) eventMove();
		
		tm.setPosition(
				Settings.WIDTH / 2 - player.getx(),
				Settings.HEIGHT / 2 - player.gety()
				);
		tm.update(dt);
		cycle.update(gc, dt);
		
		player.update(gc, dt);

		for(Enemy g : gameObjects) {
			g.update(gc, dt);
		}
		
		for(Fly f : flies) {
			f.update(gc, dt);
		}
		for(int i = 0; i < items.size(); i++) {
			Item it = items.get(i);
			
			it.update(gc, dt);
			
			if(it.shouldRemove()) {
				items.remove(i);
				i--;
			}
		}
		
		handleInput(gc);
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		tm.draw(r);
		
		//handle cycle colors
		cycle.renderCycle(gc, r);
		
		//render items befroe player
		for(Item i : items) {
			i.render(r, gc);
		}
		
		player.render(gc, r);
		
		for(Enemy g : gameObjects) {
			g.render(gc, r);
		}
		
		for(Fly f : flies) {
			f.render(gc, r);
		}
		r.drawLight(new Light(0xffcc33, 200), (int)(tm.getx() + 80), (int)((tm.gety() + 80)));
		r.drawLight(new Light(0xffcc33, 75), (int)(tm.getx() + 80), (int)((tm.gety() + 200)));
		//r.drawLight(new Light(0xffcc33, 75), (int)(tm.getx() + player.getx()), (int)((tm.gety() + player.gety())));
		
		player.postRender(gc, r);
		
		for(int i = 0; i < boxes.size(); i++) {
			r.drawFillRect(boxes.get(i).x, boxes.get(i).y,
					boxes.get(i).width, boxes.get(i).height, 0x000000);
		}
	}
	
	private void handleInput(GameContainer gc) {		
		if(!player.canInput()) return;
		//handle if down
		if(gc.getInput().isKey(KeyEvent.VK_SHIFT))player.setSprinting(true);
		if(gc.getInput().isKeyReleased(KeyEvent.VK_SHIFT))player.setSprinting(false);
		
		if(gc.getInput().isKeyPressed(KeyEvent.VK_UP)) {
			player.up();
			eventMove = true;
		}
		else if(gc.getInput().isKeyPressed(KeyEvent.VK_DOWN)) {
			eventMove = true;
			player.down();
		}
		else if(gc.getInput().isKeyPressed(KeyEvent.VK_LEFT)) {
			eventMove = true;
			player.left();
		}
		else if(gc.getInput().isKeyPressed(KeyEvent.VK_RIGHT)) {
			eventMove = true;
			player.right();
		}
		
		if(isDown) {
			if(gc.getInput().isKey(KeyEvent.VK_UP)) player.moveUp();
			else if(gc.getInput().isKey(KeyEvent.VK_DOWN)) player.moveDown();
			else if(gc.getInput().isKey(KeyEvent.VK_LEFT)) player.moveLeft();
			else if(gc.getInput().isKey(KeyEvent.VK_RIGHT)) player.moveRight();
			else {
				isDown = false;
			}
		}
		
		if(gc.getInput().isKey(KeyEvent.VK_L)) craft.woodBlock();
		
		//set action
		if(!player.getMoving()) {
			if(gc.getInput().isKeyPressed(KeyEvent.VK_Z)) player.setAction();
			if(gc.getInput().isKeyPressed(KeyEvent.VK_X)) player.cutTree();
		}
	}
	
	private void eventStart() {
		eventTick++;
		if(eventTick == 1) {
			boxes.clear();
			for(int i = 0; i < 4; i++) {
				if(i % 2 == 0) boxes.add(new Rectangle(-128, i * 16, Settings.WIDTH, 16));
				else boxes.add(new Rectangle(128, i * 16, Settings.WIDTH, 16));
			}
		}
		if(eventTick > 1) {
			for(int i = 0; i < boxes.size(); i++) {
				Rectangle r = boxes.get(i);
				if(i % 2 == 0) {
					if(r.x < 0) r.x += 4;
				}
				else {
					if(r.x > 0) r.x -= 4;
				}
			}
		}
	}
	
	private void eventMove() {
		eventTick++;
		if(eventTick == 5) {
			isDown = true;
			eventTick = 0;
			eventMove = false;
		}
	}
	

	@Override
	public void dipose() {
		// TODO Auto-generated method stub
		
	}

}
