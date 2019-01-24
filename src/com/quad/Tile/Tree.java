package com.quad.Tile;

public class Tree extends GameTile{
	
	public Tree(TileMap tm) {
		super(tm);
		
		width = 16;
		height = 16;
	}
	
	
	public void getTrees() {
		for(int r = 0; r < tm.getNumRows(); r++) {
			for(int c = 0; c < tm.getNumCols();c++) {
				if(tm.getIndex(r, c) == 20) System.out.println("tree");
			}
		}
	}

}
