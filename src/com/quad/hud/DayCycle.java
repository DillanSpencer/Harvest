package com.quad.hud;

import com.quad.core.GameContainer;
import com.quad.core.Renderer;

public class DayCycle {
	
	public static final int MORNING = 0;
	public static final int MIDMORNING = 3000;
	public static final int DAY = 6000;
	public static final int MIDDAY = 20000;
	public static final int NIGHT = 24000;
	public static final int MIDNIGHT = 27000;
	private int currentTime;
	private int tick;
	
	//start
	private boolean start;
	
	public DayCycle() {
		currentTime = DAY;
		tick = 0;
	}
	
	public void start() {start = true;}
	public void stop() {start = false;}
	
	public int getTime() {return currentTime;}
	public void setTime(int time) {
			currentTime = time;
			tick = time;
		}
	
	public void update(GameContainer gc, float dt) {
		tick++;
		
		if(!start) return;
		switch(tick) {
		case 1:
			currentTime = MORNING;
			break;
		case 3000:
			currentTime = MIDMORNING;
			break;
		case 6000:
			currentTime = DAY;
			break;
		case 20000:
			currentTime = MIDDAY;
			break;
		case 24000:
			currentTime = NIGHT;
			break;
		case 27000:
			currentTime = MIDNIGHT;
			break;
		case 30000:
			tick = 0;
			currentTime = MORNING;
		
		}
	}
	
	public void renderCycle(GameContainer gc, Renderer r) {
		switch(currentTime) {
		case MORNING:
			r.setAmbientLight(0xe2a94d);
			break;
		case MIDMORNING:
			r.setAmbientLight(0xe8c792);
			break;
		case DAY:
			r.setAmbientLight(0xffffff);
			break;
		case MIDDAY:
			r.setAmbientLight(0x568ce2);
			break;
		case NIGHT:
			r.setAmbientLight(0x304f82);
			break;
		case MIDNIGHT:
			r.setAmbientLight(0x343435);
		}
	}

}
