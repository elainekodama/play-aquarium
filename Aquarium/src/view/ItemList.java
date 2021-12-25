package view;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.Fish;
import model.Item;
import model.Plant;

/*
 * ListView with CustomListCell
 * Picture with name, sell price, and hunger/coins status
 */
public class ItemList extends ListView /*<CustomThing> */ {
	ArrayList<Item> items;
	ObservableList observableList;

	public ItemList(ArrayList<Item> items) {
		this.items = items;
		this.setPrefSize(UI.width - UI.tankWidth, UI.height-75);
		observableList = FXCollections.observableArrayList();
		observableList.setAll(items);
//		this.setBackground(new Background(new BackgroundFill(Color.LIGHTGOLDENRODYELLOW, new CornerRadii(0), null)));
		this.setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		Text placeholder = new Text("No items yet!");
		placeholder.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 18));
		this.setPlaceholder(placeholder);
		
		this.setItems(observableList);
		this.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
		    @Override
		    public CustomCell call(ListView<Item> listView) {
		        return new CustomCell();
		    }
		});
	}
	
	private class CustomCell extends ListCell<Item> {
		private HBox hBox;
		private VBox vBox;
		private Text name;
		private Text price;
		private Text alert;
		private ImageView image;

		public CustomCell() {
			super();
			name = new Text();
			price = new Text();
			alert = new Text();
			alert.setFill(Color.RED);
			makeText(new Text[]{name, price, alert});
			
			hBox = new HBox();
			hBox.setSpacing(10);
			hBox.setAlignment(Pos.CENTER_LEFT);
			
			image = new ImageView();
			image.setPreserveRatio(true);
			image.setFitHeight(80); image.setFitWidth(80);
			
			vBox = new VBox(name, price);
			vBox.setAlignment(Pos.CENTER_LEFT);
			hBox.getChildren().addAll(image, vBox, alert);
		}
		
		private void makeText(Text[] texts) {
			for(Text text : texts) {
				text.setFont(Font.font("Comic Sans MS"));
			}
		}

		@Override
		protected void updateItem(Item item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null && !empty) { //test for null and make sure not empty
				name.setText(item.getName());
				price.setText("Sell Price: " + item.getSellPrice() + " coins");
				price.setAccessibleText("This item sells for " + item.getSellPrice() + " coins");
				if(item instanceof Fish) { //if for fishList, then use image from FishSpecies
					Fish fish = (Fish)item;
					File file = new File(fish.getSpecies().getFileName());
					Image i = new Image(file.toURI().toString());
					image.setImage(i);
					if(fish.getHunger()) { alert.setText("Hungry!"); }
					else { alert.setText(""); }
				} else if(item instanceof Plant) { //if plantList, then use plant image
					File file = new File("algae-plant.png");
					Image i = new Image(file.toURI().toString());
					image.setImage(i);
					if(((Plant)item).getCoinsReady()) {
						{ alert.setText("Coins Ready!"); }
					}
					else { alert.setText(""); }
				}
				setGraphic(hBox);
			} else {
				setGraphic(null);
			}
		}
	}

}
