package com.quad.items.craftable;

import java.util.ArrayList;

import com.quad.entity.InventoryItem;
import com.quad.inventory.Inventory;
import com.quad.inventory.ItemRecipe;

public class WoodBlock extends ItemRecipe{
	

	public WoodBlock(Inventory inventory) {
		super(inventory);
	}

	@Override
	public void setRecipe() {
		add(InventoryItem.woodItem.createNew(1));
	}
	
	public boolean canCraft() {

		for(int j = 0; j < getRecipe().size(); j++) {
			if(inventory.hasItem(getRecipe().get(j).getName())) {
				if(getRecipe().get(j).getCount() <= inventory.getInventoryItems().get(j).getCount()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}
