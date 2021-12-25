package view;

import java.io.File;
import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import model.FishSpecies;

public class UI {
	public static double width = 1110, height = 635;
	public static double tankWidth = 800, tankHeight = 550;
	
	//fish tank
	public static Pane makeAquarium() {
		Pane tank = new Pane();
		tank.setPrefSize(tankWidth, tankHeight);
		ImageView iv = UI.returnImageView("fish-tank.png");
	    iv.setFitHeight(UI.tankHeight); iv.setFitWidth(UI.tankWidth);
	    tank.getChildren().add(iv);
	    return tank;
	}
	
	//row of buttons on the top
	public static Button ctrlButtonDesigner(String label) {
		Button b = new Button(label);
		b.setTextAlignment(TextAlignment.CENTER);
		b.setPrefSize(width/7, 75);
		b.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 15));
		return b;
	}
	
	//text that displays amount of coins user has
	public static StackPane makeCoinsStackPane(int myCoins) {
		StackPane sp = new StackPane();
		sp.setPrefSize(width/7, 100);
		Text coins = new Text("My Coins:\n" + myCoins);	
		coins.setAccessibleText("You have " + myCoins + " coins");
		coins.setTextAlignment(TextAlignment.CENTER);
		coins.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 15));
		sp.getChildren().add(coins);
		return sp;
	}
	
	//updates the amount of coins user has
	public static StackPane updateCoins(StackPane sp, int myCoins) {
		Text t = (Text)sp.getChildren().get(0);
		t.setText("My Coins:\n" + myCoins);
		t.setAccessibleText("You have " + myCoins + " coins");
		return sp;
	}
	
	//combo box list for choosing a fish species to buy
	public static ComboBox<String> makeFishCB() {
		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.setPromptText("Pick a fish");
		comboBox.setAccessibleText("Choose a species of fish");
		comboBox.setPrefWidth(width/7);
		for(int i=0; i<FishSpecies.values().length; i++) {
			FishSpecies fs = FishSpecies.values()[i];
			comboBox.getItems().add((i + 1) + ".  " + fs.getSpecies() + "- " + fs.getCost() + " coins");
		}        
        return comboBox;
	}
	
	//include a button to exit the fish combo box when buying a fish
	public static VBox makeBuyFishVBox(Button b, ComboBox<String> comboBox) {
		VBox vb = new VBox();
		b.setPrefWidth(width/7);
		b.setPrefHeight(75);
		b.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 15));
		vb.getChildren().addAll(b, comboBox);
		return vb;
	}

	//visible for a little bit then pellet fades into tank
	public static void dropFoodPellets(Pane tank) {
		Circle food = new Circle(3);
		food.setFill(Color.BROWN);
		Random r = new Random();
		double x = r.nextDouble()*(tank.getWidth()-10); //randomly place within x range
		food.setCenterX(x);
		food.setCenterY(tank.getLayoutX() + 50);
		tank.getChildren().add(food);
		
		double howDeep = (r.nextDouble()*(height-height/2)) + height/2; //value between the bottom of tank and middle of tank
		FadeTransition ft = new FadeTransition(); //fade into water at the end
		ft.setNode(food);
		ft.setDelay(new Duration((howDeep*15)/2));
		ft.setToValue(0);
		ft.playFromStart();
		
		TranslateTransition tt = new TranslateTransition(); //drop down into tank
	    tt.setNode(food);
	    tt.setToX(0); //no horizontal movement
	    tt.setToY(howDeep);
	    tt.setDuration( new Duration(howDeep*15));
	    tt.setCycleCount(1);
	    tt.playFromStart();
	    tt.setOnFinished(e -> {
	    	tank.getChildren().remove(food);
	    });
	}

	//when user does not have enough coins to buy an item
	public static void showNoCoins(Pane tank) {
		Text text = new Text("NO COINS");
		text.setAccessibleText("You do not have enough coins");
		text.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 25));
		text.setFill(Color.RED);
		text.setLayoutX(tankWidth/2-60); text.setLayoutY(tankHeight/2);
		tank.getChildren().add(text);
		
		FadeTransition ft = new FadeTransition(); //fade away text
		ft.setNode(text);
		ft.setDuration(new Duration(2000));
		ft.setToValue(0);
		ft.playFromStart();
		ft.setOnFinished(e -> {
			tank.getChildren().remove(text);
		});
	}
	
	//start up display of controls
	public static Pane controlMenu() {
		Pane p = new Pane();
		p.setPrefSize(600,  300);
		p.setBackground(new Background(new BackgroundFill(Color.LIGHTGOLDENRODYELLOW, new CornerRadii(0), null)));
		String title = "Welcome to Aquarium!\n"
				+ "Fish gain value after they age. Sell more fish to grow your aquarium!";
		String controls = "Controls:\n"
				+ "F = Feed fish\n"
				+ "B -> Item Number = Buy Fish\n"
				+ "P = Buy Plant\n"
				+ "Item Number -> S = Sell Item\n"
				+ "SHIFT = Switch between Fish and Plant menu\n"
				+ "C = Collect Coins\n"
				+ "Can use arrow keys and space bar for all controls";
		Text text1 = new Text(title);
		Text text2 = new Text(controls);
		
		text1.setTextAlignment(TextAlignment.CENTER);
		text2.setTextAlignment(TextAlignment.LEFT);
		p.getChildren().addAll(text1, text2);
		
		text1.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 18));
		text2.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 14));
		text1.setLayoutX(10);
		text2.setLayoutX(10);
		text1.setLayoutY(20);
		text2.setLayoutY(80);
		return p;
	}
	
	//the button to open up the game display
	public static Button playGameButton(Pane p) {
		Button b = new Button("Play Game!");
		b.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 14));
		b.setPrefSize(100, 50);
		p.getChildren().add(b);
		b.setLayoutX(p.getPrefWidth()/2 - b.getPrefWidth()/2);
		b.setLayoutY(250);
		return b;
	}
	
	//get the images for the fish, plants, and tank
	public static ImageView returnImageView(String fileName) {
		File file = new File(fileName);
	    Image image = new Image(file.toURI().toString());
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		imageView.setPreserveRatio(true);
		return imageView;
	}
	
}
