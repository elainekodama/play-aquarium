package model;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import view.UI;


//TODO: move plant, add aliens
public class Plant extends Item{
	
	private boolean coinsReady;
	private ImageView coinView;

	public Plant(String name, int cost) {
		super(name, cost, "algae-plant.png");
		
		picture.setFitWidth(100); picture.setFitHeight(200);

		Random r = new Random();
		picture.setX(r.nextDouble()*(UI.tankWidth - picture.getFitWidth()));
		double randDouble = 1.1 + (1.5 - 1.1) * r.nextDouble();
		picture.setY(UI.tankHeight - picture.getFitHeight() * randDouble);
		
		//coin icon above plant
		coinsReady = true;
		coinView = UI.returnImageView("coin.GIF");
		coinView.setFitWidth(20); coinView.setFitHeight(20);
		coinView.setX(this.picture.getX() + this.picture.getFitWidth()/2); coinView.setY(this.picture.getY()-15); //place on top of plant
		coinView.setVisible(true);
	}
	
	//using Timer and TimerTask to handle user action collecting coins
	public void collectCoins() {
		coinsReady = false;
		coinView.setVisible(coinsReady);
		Timer timer = new Timer();
    	TimerTask timerTask = new TimerTask(){
    		public void run() {
    			System.out.println("Coins are ready");
    			coinsReady = true;
    			coinView.setVisible(coinsReady);
    		}
    	};
		timer.schedule(timerTask, 30000); //30 seconds
	}
	
	//TODO: move plant
	
	public ImageView getCoinPicture() {
		return this.coinView;
	}
	
	public boolean getCoinsReady() {
		return this.coinsReady;
	}
	
}
