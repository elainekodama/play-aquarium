package model;

import javafx.scene.image.ImageView;
import view.UI;

public abstract class Item {
	String name;
	int sellPrice;
	ImageView picture;
	
	public Item(String name, int cost, String fileName) {
		this.name = name;
		sellPrice = cost;
		picture = UI.returnImageView(fileName);
	}
	
	public Item(FishSpecies species, int cost) {
		name = species.getSpecies();
		sellPrice = cost;
		picture = UI.returnImageView(species.getFileName());
	}
	
	public int getSellPrice() {
		return sellPrice;
	}
	
	public String getName() {
		return name;
	}
	
	public ImageView getImage() {
		 return this.picture;
	}
}
