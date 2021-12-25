package model;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import view.UI;

//TODO: possibly implement a lifespan
//TODO: possibly implement reproduction/mating
public class Fish extends Item{

	int age;
	boolean hungry;
	FishSpecies species;
	public double x, y, lastX, lastY; //location of fish
	public double xSpeed, ySpeed, minSpeed = 50, maxSpeed = 80; //control speed of fish
	boolean firstTime; long lastTime; long startTime; //to initialize driver
	Driver driver;
	public double notHungryTimer, isHungryTimer, ageTimer; //animation timers for hunger penalty and age
	
	public Fish(FishSpecies species, int cost){
		super(species, cost);
		this.species = species;
		age = 0;
		hungry = true;
		
		Random r = new Random();
		
		picture.setFitWidth(80); picture.setFitHeight(80);
		x = r.nextDouble()*(UI.tankWidth - picture.getFitWidth() - 20); //random x
		y = 65; //top of tank
		picture.setX(x); picture.setY(y);
		picture.setRotationAxis(Rotate.Y_AXIS);
		
		//random speed
		xSpeed = minSpeed + r.nextDouble()*maxSpeed; //random speed
		ySpeed = xSpeed; //same y speed as x speed
		firstTime = true;
    	driver = new Driver();
    	driver.start();
    }
	
	public void getsFed() {
		hungry = false;
	}
	
	public FishSpecies getSpecies() {
		return this.species;
	}
	public boolean getHunger() {
		return this.hungry;
	}
	
	public void swims(double deltaT) {
		//set x and y accoring to change in time
		x += xSpeed * deltaT;
		picture.setX(x);
		y += ySpeed * deltaT;
		picture.setY(y);
				
		//tank bounds, location of image starts at top left
		//flip picture when changing x direction
		if(x<0) { xSpeed = Math.abs(xSpeed); picture.setRotate(0); } //left
		if (x>(UI.tankWidth - picture.getFitWidth() - 20)) { xSpeed = -Math.abs(xSpeed); picture.setRotate(180);} //right
		if (y>(UI.tankHeight - picture.getFitHeight() - 20)) { ySpeed = -Math.abs(ySpeed); } //bottom
    	if (y<65) { ySpeed = Math.abs(ySpeed); } //top
		
	}
	
	public void step(long now) {
		if ( firstTime ) { lastTime = now; startTime = now; notHungryTimer = 0; isHungryTimer = 0; ageTimer = now * 1.0e-9; firstTime = false; } //initialize time "now"
		else {
			double deltaT = (now-lastTime) * 1.0e-9;
			this.swims(deltaT);
			
			//fish just becomes hungry
			if(hungry && isHungryTimer == 0.000) {
				isHungryTimer = now * 1.0e-9;
			}
			//fish is hungry, sell price is decreasing
			if(Math.abs((now* 1.0e-9) - (isHungryTimer+10)) < 0.01500) { //after ~10 seconds decrease sell price
				sellPrice--;
				isHungryTimer = now * 1.0e-9; //repeat timer
			}
			//TODO: possibly fish dies? :( or slows down, etc.
			
			//fish just gets fed
			if(!hungry && notHungryTimer == 0.00) {
				notHungryTimer = now * 1.0e-9;
				isHungryTimer = 0;
			}
			//fish not hungry, waiting to get hungry
			if(Math.abs((now* 1.0e-9) - (notHungryTimer+30)) < 0.01500) { //~30 seconds
				hungry = true;
				notHungryTimer = 0.00;
			}
			
			if(Math.abs((now* 1.0e-9) - (ageTimer+120)) < 0.01500) { //increase age after ~120 seconds
				age++;
    			if(age <= 5) {
    				sellPrice+=2;
    				//fish gets bigger
    				picture.setFitWidth(picture.getFitWidth()*1.15); picture.setFitHeight(picture.getFitHeight()*1.15);
    			}
    			else { //after age 5, stop growing, but price still increases
    				sellPrice+=1;
    			}
    			ageTimer = now* 1.0e-9;
			}
			lastTime = now;
		}
	}
	
	public class Driver extends AnimationTimer {
		@Override
		public void handle(long now) {
			step(now);
		}
	}	
}
