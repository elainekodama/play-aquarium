package model;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public enum FishSpecies {
	GOLDFISH("Gold Fish", 5, "goldfish.png"),
	BETTA_FISH("Beta Fish", 10, "betta-fish.png"),
	ANGEL_FISH("Angel Fish", 15, "angel-fish.png");
	
	String species;
	int cost;
	String url;
	
	private FishSpecies(String species, int cost, String url) {
		this.species = species;
		this.cost = cost;
		this.url = url;
	}
	
	public int getCost() {
		return this.cost;
	}
	
	public String getSpecies() {
		return this.species;
	}
	
	public Image getImage() {
		File file = new File(this.url);
	    Image image = new Image(file.toURI().toString());
		return image;
	}
	
	public String getFileName() {
		return this.url;
	}
	
	public static FishSpecies getFishSpecies(int index) {
		return FishSpecies.values()[index];
	}
	
	
}
