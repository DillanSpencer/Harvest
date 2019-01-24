package com.quad.states;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.components.State;
import com.quad.entity.TestPlayer;

public class LevelState extends State{
	
	private TestPlayer player;
	private TileMap tm;

	@Override
	public void init(GameContainer gc) {
		tm = new TileMap(gc, 16);
		player = new TestPlayer(tm);
	}

	@Override
	public void update(GameContainer gc, float dt) {
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawFillRect(0, 0, Settings.WIDTH, Settings.HEIGHT, 0x000000);
	}

	@Override
	public void dipose() {
		
	}

}
