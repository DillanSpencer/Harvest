package com.quad.items.craftable;

import com.quad.inventory.Inventory;

public class Craft {
	
	private Inventory inventory;
	private WoodBlock woodBlock;
	
	public Craft(Inventory inventory){
		this.inventory = inventory;
		woodBlock = new WoodBlock(inventory);
		woodBlock.setRecipe();
	}
	
	//woodblock
	public void woodBlock() {
		if(woodBlock.canCraft()) {
			System.out.println("Can craft");
		}
		else {
			System.out.println("Not Craft");
		}
	}

}
