package com.quad.inventory;

import java.util.ArrayList;

import com.quad.entity.InventoryItem;

public abstract class ItemRecipe {
	
	protected Inventory inventory;
	private ArrayList<InventoryItem> recipe;
	
	
	public ItemRecipe(Inventory inventory) {
		this.inventory = inventory;
		this.setRecipe(new ArrayList<InventoryItem>());
	}
	
	public abstract void setRecipe();
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
	
	//add to recipe
	public void add(InventoryItem item) {
		getRecipe().add(item);
	}

	public ArrayList<InventoryItem> getRecipe() {
		return recipe;
	}

	public void setRecipe(ArrayList<InventoryItem> recipe) {
		this.recipe = recipe;
	}
	
	

}
