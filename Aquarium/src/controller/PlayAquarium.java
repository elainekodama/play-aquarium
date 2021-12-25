package controller;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Fish;
import model.FishSpecies;
import model.Item;
import model.Plant;
import view.ItemList;
import view.UI;

/*
 * Controls user's actions
 * Displays the controls
 */
public class PlayAquarium extends Application {
	//Function variables
	//test aquarium
	private ArrayList<Item> myFish, myPlants;
	private int myCoins;
	
	//UI variables
	private ItemList fishList, plantList;
	private StackPane coinsSP;
	private Text coins;
	private Pane tank;
	private HBox controls;
	private ComboBox<String> fishCB;
	private Button buyF;
	private VBox buyFVB;
	private VBox fishListVBox;
		
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		HBox root = new HBox(10);
		root.setBackground(new Background(new BackgroundFill(Color.LIGHTGOLDENRODYELLOW, new CornerRadii(0), null)));
		Scene gameScene = new Scene(root, UI.width, UI.height);
		
		stage.setTitle("Aquarium");
		stage.show();
		
		//instruction/control view
		Pane p = UI.controlMenu();
		Scene controlMenuScene = new Scene(p, 600, 400);
		stage.setScene(controlMenuScene);
		
		Button playGame = UI.playGameButton(p);
		playGame.setOnAction(e -> {
			stage.setScene(gameScene);
		});
		
		//add accessbility
		gameScene.setOnKeyPressed(ke -> { keyboardClicked(ke.getCode()); });
		
		initializeGame(root);
		
		stage.setOnCloseRequest(e->{ Platform.exit(); System.exit(0); });
	}

	//allow user to play game using only keyboard
	private void keyboardClicked(KeyCode key) {
		switch(key) {
		case F:
			feedFish(); break;
		case S:
			if(fishList.getSelectionModel().getSelectedItem() != null) {
				sellItem(fishList.getSelectionModel().getSelectedItem());
				fishList.getSelectionModel().clearSelection();
			};
			break;
		case P:
			buyPlant(); break;
		case C:
			for(Item p : myPlants) {
				Plant plant = (Plant)p;
				getCoins(plant);
			}
			break;
		case B:
			controls.getChildren().set(1, buyFVB);
			fishCB.show();
			break;
			
		case DIGIT1:
			numberPressed(KeyCode.DIGIT1.getName()); break;
		case DIGIT2:
			numberPressed(KeyCode.DIGIT2.getName()); break;
		case DIGIT3:
			numberPressed(KeyCode.DIGIT3.getName()); break;
		
		case SHIFT:
			if(fishListVBox.getChildren().get(1) == plantList) { //plant list is currently displayed
				fishListVBox.getChildren().set(1, fishList); //show fish list
			}
			else { fishListVBox.getChildren().set(1, plantList); }
			break;
		default:
			//clear all selections
			controls.getChildren().set(1, buyF);
			fishList.getSelectionModel().clearSelection();
			fishCB.getSelectionModel().clearSelection();
			break;
		}
	}
	//distinguises number press for buying fish vs. selling fish
	private void numberPressed(String number) {
		int index = Integer.valueOf(number) - 1; //get the number clicked
		if(fishCB.isShowing()) { //if user is currently picking a fish to buy
			buyFish(index);
			controls.getChildren().set(1, buyF);
		}
		else { //user just presses number
			if(index < myFish.size()) { //select and focus chosen fish to sell
				fishList.getSelectionModel().select(index);
				fishList.getFocusModel().focus(index);
			}
		}
	}

	private void initializeGame(HBox root) {
		//initialize data
		myFish = new ArrayList<Item>();
		myPlants = new ArrayList<Item>();
		myCoins = 20; //start with 20 coins
		
		VBox tankVBox = new VBox(10);
		tank = UI.makeAquarium();
		runControlBar(tankVBox);
		tankVBox.getChildren().add(tank); //add tank after the controls
		
		fishList = new ItemList(myFish);
		plantList = new ItemList(myPlants);
		
		//List views
		Button fishBtn = UI.ctrlButtonDesigner("Fish");
		fishBtn.setAccessibleText("View my fish");
		Button plantBtn = UI.ctrlButtonDesigner("Plants");
		plantBtn.setAccessibleText("View my plants");
		//switch the list view to show fish or plants
		fishBtn.setOnAction(e -> { fishListVBox.getChildren().set(1, fishList); });
		plantBtn.setOnAction(e -> { fishListVBox.getChildren().set(1, plantList); });

		HBox fishPlantsBtns = new HBox();
		fishPlantsBtns.getChildren().addAll(fishBtn, plantBtn);
		
		fishListVBox = new VBox();
		fishListVBox.getChildren().addAll(fishPlantsBtns, fishList);
		
		root.getChildren().addAll(tankVBox, fishListVBox);
		
		//refresh list views every second
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				fishList.refresh();
				plantList.refresh();
			}
		};
		timer.scheduleAtFixedRate(task, 0, 1000);
	}
	
	//buttons that control game
	private void runControlBar(VBox tankVBox) {		
		Button feed = UI.ctrlButtonDesigner("Feed Fish\n(1 coin)");
			feed.setOnAction(e -> { feedFish();	});
			
		buyF = UI.ctrlButtonDesigner("Buy Fish");
			buyF.setOnAction(e -> { controls.getChildren().set(1, buyFVB); }); //switch back and forth between buy and buying
			
		Button buyP = UI.ctrlButtonDesigner("Buy Plant\n(10 coins)");
			buyP.setOnAction(e -> { buyPlant(); });
			
		Button sell = UI.ctrlButtonDesigner("Sell Fish");
			sell.setOnAction(e -> {
				if(fishList.getSelectionModel().getSelectedItem() != null) {
					sellItem(fishList.getSelectionModel().getSelectedItem()); //select fish before clicking sell
				}
			});
		
		coinsSP = UI.makeCoinsStackPane(myCoins);
		
		controls = new HBox();
		controls.getChildren().addAll(feed, buyF, buyP, sell, coinsSP);
		controlComboBoxes(controls);
		tankVBox.getChildren().add(controls);
	}

	//displays list of fish to buy
	private void controlComboBoxes(HBox controls) {
		fishCB = UI.makeFishCB();
		fishCB.setOnAction( (e) -> {
			controls.getChildren().set(1, buyF); //buyF has no combo box
			int index = fishCB.getSelectionModel().getSelectedIndex();
			if(index >= 0 && index < FishSpecies.values().length) {
				buyFish(index); //buy chosen fish
			}
			Platform.runLater(() -> fishCB.getSelectionModel().clearSelection()); //clear selection
		});
		
		Button fishGoBack = new Button("Go Back");
		fishGoBack.setOnAction(e -> {
			controls.getChildren().set(1, buyF); //switch back and forth between buy and buying
		});
		buyFVB = UI.makeBuyFishVBox(fishGoBack, fishCB); //buyFVB includes combo box
		//TODO: Combo Box for Plants!
	}
	
	//randomly drop food into tank, fish get full
	private void feedFish() {
		if(myCoins>0) {
			UI.dropFoodPellets(tank); //can give food even if no fish are hungry
			myCoins--; //costs one coin
			UI.updateCoins(coinsSP, myCoins);
			for(Item f : myFish) {
				if(((Fish)f).getHunger()) {
					((Fish)f).getsFed();
					break; //only feed one fish (first fish that was found in arraylist)
				}
			}
		}
		else {
			UI.showNoCoins(tank);
		}
	}
	
	//choose species of fish to buy, update coins
	private void buyFish(int index) {
		FishSpecies fs = FishSpecies.getFishSpecies(index);
		if(myCoins - fs.getCost() >= 0) { //can afford
			myCoins -= fs.getCost();
			UI.updateCoins(coinsSP, myCoins);
			Fish f = new Fish(fs, fs.getCost());
			myFish.add(f);
			tank.getChildren().add(f.getImage());
			fishList.getItems().add(f);
		}
		else {
			UI.showNoCoins(tank);
		}
	}
	
	//TODO: different types of plants
	//choose plant to buy and update coins
	private void buyPlant() {
		Plant p = new Plant("Algae Plant", 5);
		if(myCoins - p.getSellPrice() >= 0) { //can afford
			myPlants.add(p);
			myCoins -= p.getSellPrice(); //sell price = initial cost
			UI.updateCoins(coinsSP, myCoins);
			tank.getChildren().addAll(p.getImage(), p.getCoinPicture());
			for(Item plant : myPlants) {
				if(p.getImage().getY() < plant.getImage().getY()) { //UI: if plant is higher up, put it behind
					plant.getImage().toFront();
				}
			}
			plantList.getItems().add(p);
			p.getImage().setOnMouseClicked(e -> { getCoins(p); });
		}
		else {
			UI.showNoCoins(tank);
		}
	}
	//user can collect 3 coins from the plants
	private void getCoins(Plant p) {
		if(p.getCoinsReady()) {
			myCoins += 3;
			UI.updateCoins(coinsSP, myCoins);
			p.collectCoins();
			plantList.refresh();
		}
	}
	
	//TODO: be able to move plants
	
	//TODO: be able to sell plant
	private void sellItem(Object object) {
		if(object instanceof Fish) {
			Fish f = (Fish)object;
			myCoins = myCoins + f.getSellPrice();
			UI.updateCoins(coinsSP, myCoins);
			myFish.remove(f);
			tank.getChildren().remove(f.getImage());
			fishList.getItems().remove(f);
		}
	}
}
