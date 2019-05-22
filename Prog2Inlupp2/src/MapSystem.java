
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MapSystem extends Application {

	// Image image = new Image("file:C:/Users/Ingela/Desktop/Programmering
	// 2/inlämingsuppgift 2/jarvafaltet.png");

	private Stage primaryStage;
	private TextField wordField = new TextField();
	// private TextArea display = new TextArea();
	private ImageView display = new ImageView();

	//Hashmap för sökning genom position(x & y)
	Map<Position, Place> positionList = new HashMap<>();
		
	List<Place> places = new ArrayList<>();
	// Hashmap för att söka genom namn
	HashMap<String, List<Place>> nameList = new HashMap<>();

	// private Image image = new Image("file:C:/Users/Ingela/Desktop/Programmering
	// 2/inlämingsuppgift 2/jarvafaltet.png");
	// private ImageView imageView = new ImageView(image);

	ObservableList<String> categories = FXCollections.observableArrayList("Underground", "Bus", "Train");
	ListView<String> cat = new ListView<>(categories);
	// categories.setPrefSize(double, double);


	private boolean changed = false;
	private ToggleGroup group = new ToggleGroup();
	private RadioButton namedButton = new RadioButton("Named");
	private RadioButton describedButton = new RadioButton("Described");
	private boolean undergroundSelected = false;
	private boolean busSelected = false;
	private boolean trainSelected = false;
	BorderPane root = new BorderPane();
	Button newButton = new Button("New");
	private ClickHandler clickHandler = new ClickHandler();



	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
	//	BorderPane root = new BorderPane();
		Label right = new Label("Right");

		VBox listan = new VBox();
		listan.setPadding(new Insets(5));
		listan.getChildren().add(new Label("Categories"));
		listan.getChildren().add(cat);
		cat.getSelectionModel().selectedItemProperty().addListener(new ListHandler());
		Button hideCategoryButton = new Button("Hide Category");
		hideCategoryButton.setAlignment(Pos.CENTER);
		listan.getChildren().add(hideCategoryButton);
		listan.setPrefSize(200, 200);
		hideCategoryButton.setOnAction(new HideCategoryButtonHandler());




		VBox vbox = new VBox();
		MenuBar menuBar = new MenuBar();
		vbox.getChildren().add(menuBar);

		Menu archiveMenu = new Menu("File");
		menuBar.getMenus().add(archiveMenu);

		MenuItem loadMapItem = new MenuItem("Load Map");
		archiveMenu.getItems().add(loadMapItem);
		loadMapItem.setOnAction(new OpenHandler());

		MenuItem loadPlacesItem = new MenuItem("Load Places");
		archiveMenu.getItems().add(loadPlacesItem);
		loadPlacesItem.setOnAction(new OpenHandler());

		MenuItem saveItem = new MenuItem("Save");
		archiveMenu.getItems().add(saveItem);
		saveItem.setOnAction(new SaveHandler());

		// categories.setItems();

		MenuItem ExitChoiceItem = new MenuItem("Exit");
		archiveMenu.getItems().add(ExitChoiceItem);

		// archiveMenu.getItems().add(exitItem);
		// exitItem.setOnAction(e -> primaryStage.fireEvent(
		// new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST)));

		HBox hboxTop = new HBox(15);
		vbox.getChildren().add(hboxTop);
		hboxTop.setPadding(new Insets(15));
		hboxTop.setAlignment(Pos.CENTER);
		// Button newButton = new Button("New");
		newButton.setOnAction(new newButtonHandler());

		VBox vbs = new VBox(10);
		vbs.getChildren().addAll(namedButton, describedButton);
		
		Button searchButton = new Button("Search");
		searchButton.setOnAction(new SearchHandler());
		Button hideButton = new Button("Hide");
		hideButton.setOnAction(new HideHandler());
		Button removeButton = new Button("Remove");
		removeButton.setOnAction(new RemoveHandler());
		Button coordinatesButton = new Button("Coordinates");
		coordinatesButton.setOnAction(new CoordinateSearch());

//        VBox root = new VBox();


		//Flytta ut radioknapperna utanför start metoden!
		// har fixat det
		namedButton.setToggleGroup(group);
		describedButton.setToggleGroup(group);


		//tror inte det nedan behövs längre

/*	    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	        public void changed(ObservableValue<? extends Toggle> ov,
	            Toggle old_toggle, Toggle new_toggle) {
	          if (group.getSelectedToggle() != null) {
	            System.out.println(group.getSelectedToggle().toString());
	          }
	        }
	      });*/
	    
	   
	    
	    
	    
	   

		hboxTop.getChildren().addAll(newButton, vbs, wordField, searchButton, hideButton,
				removeButton, coordinatesButton);
		root.setTop(vbox);
		// root.setCenter(imageView);

//        display.setWrapText(true);
		root.setCenter(display);

		root.setRight(listan);
		// listan.setPadding(new Insets(5));
		// listan.setPrefSize(100, 400);
		// listan.getSelectionModel().selectedItemProperty().addListener(
		// new ListHandler());

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Map System");
		primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new ExitHandler());
		primaryStage.show();
	}

	class ExitHandler implements EventHandler<WindowEvent> {
		public void handle(WindowEvent event) {
			if (changed) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setContentText("Osparat. Avsluta ändå?");
				Optional<ButtonType> res = alert.showAndWait();
				if (res.isPresent() && res.get() == ButtonType.CANCEL)
					event.consume();
			}
		}
	}
	
	
	


	/*
	 * class SearchHandler implements EventHandler<ActionEvent>{
	 * 
	 * @Override public void handle(ActionEvent event) { String word =
	 * wordField.getText(); if (word.isEmpty()) word =
	 * listan.getSelectionModel().getSelectedItem(); else
	 * listan.getSelectionModel().select(word); if (word == null || uppslag == null)
	 * return; String def = uppslag.get(word); display.setText(def); } }
	 */

	//Categories handler:
	class ListHandler implements ChangeListener<String>{
		@Override
		public void changed(ObservableValue obs, String old, String nev){
			switch (nev) {
				case "Underground":
					//gör alla undergroundplatser synliga
					System.out.println(nev);
					undergroundSelected = true;
					trainSelected = false;
					busSelected = false;
					break;
				case "Train":
					//gör alla trainplatser synliga
					System.out.println(nev);
					undergroundSelected = false;
					trainSelected = true;
					busSelected = false;
					break;
				case "Bus":
					//gör alla busplatser synliga
					System.out.println(nev);
					undergroundSelected = false;
					trainSelected = false;
					busSelected = true;
					break;
				default:
				//gör category null
			}
		
		}
	}



	class newButtonHandler implements EventHandler<ActionEvent>{
		@Override public void handle(ActionEvent event) {
			root.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
			root.setCursor(Cursor.CROSSHAIR);
			newButton.setDisable(true);
		}
	}

	class ClickHandler implements EventHandler<MouseEvent>{
		@Override public void handle(MouseEvent event) {
			double x = event.getX();
			double y = event.getY();
			PostItLapp lapp = new PostItLapp(x,y);
			root.getChildren().add(lapp);
			root.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
			root.setCursor(Cursor.DEFAULT);
			newButton.setDisable(false);
		}
	}

	/*
	public class newButtonHandler implements EventHandler<ActionEvent> {
		boolean new_is_pressed = false;
		public void handle(ActionEvent event) {
			display.setCursor(Cursor.CROSSHAIR);
			//Lyssnar var användare klickar i kartan och hämtar koordinaterna
			new_is_pressed = true;
			display.setOnMouseClicked(new NewPlace());
			
			
		
			
			*/
			
//			while (new_is_pressed) {
//				handleMouseClickAction(null);
//				
//			}
			//handleMouseClickAction(null);
			
			//Stäng av mus lyssnaren
			//Kolla kategoring och radiobutton
			//Startar Named eller Described placehandler beroende på radiobuttons
			//Objectet skapas i dialogen eller här.


	
	class NewPlace implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			double x = event.getX();
			double y = event.getY();
			//new NewNamePlace();
			//hejsan
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Text Input Dialog");

			dialog.setContentText("Please the name of the place");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				System.out.println("Your name: " + result.get());
			}
               
		//	System.out.println(x + " " + y);
/*			if (group.getSelectedToggle() == null)
			{
				System.out.println("Inget valt");
				return;
				//&& namedButton.isSelected()

				//System.out.println(group.getSelectedToggle());
			}
			if (group.getSelectedToggle().toString().contains("Named"))
			{
				System.out.println("Named funkar");
					//&& namedButton.isSelected()

				//System.out.println(group.getSelectedToggle());
			}
			if (group.getSelectedToggle().toString().contains("Described"))
			{
				System.out.println("Described funkar");
				//&& namedButton.isSelected()

				//System.out.println(group.getSelectedToggle());
			}*/
			if ((group.getSelectedToggle().toString().contains("Described")) && undergroundSelected) {
				System.out.println("Described funkar tillsammans med underground");
			}
			else if ((group.getSelectedToggle().toString().contains("Described")) && busSelected) {
				System.out.println("Described funkar tillsammans med buss");
			}
			else if ((group.getSelectedToggle().toString().contains("Described")) && trainSelected) {
				System.out.println("Described funkar tillsammans med tåg");
			}
			else if ((group.getSelectedToggle().toString().contains("Named")) && undergroundSelected) {
				System.out.println("Named funkar tillsammans med underground");
				
			}
			else if ((group.getSelectedToggle().toString().contains("Named")) && busSelected) {
				System.out.println("Named funkar tillsammans med buss");
			}
			else if ((group.getSelectedToggle().toString().contains("Named")) && trainSelected) {
				System.out.println("Named funkar tillsammans med tåg");

			}
			else {
				System.out.println("Inget");
			}

			//uppdaterings test
			//Kolla radiobutton 
			//kolla kategori
			//Skapa plats mha diologruta
			//Avsluta new place function
		}

	}

	
	
	//private void disableNewPlaceCursor() {
        //display.setCursor(Cursor.DefaultCursor);
        //display.removeMouseListener(mouseLyss);
        //newButton.setEnabled(true);
   // }
			
			
			
			//setOnMouseClicked(EventHandler<MouseEvent>);
			
		
	
//	private void handleMouseClickAction(MouseEvent event) {
//	    double x = event.getX();
//	    double y = event.getY();
//	    if (!isFirstLine && freehandButton.isSelected())
//	        gc.strokeLine(oldX, oldY, x, y);
//
//	    isFirstLine = false;
//	  //  oldX = x;
//	    //oldY = y;
//
//	    System.out.printf("(%f, %f)\n", x, y);
//	}
	

	@SuppressWarnings("unchecked")
	class OpenHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			try {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Map File");
				fileChooser.setInitialDirectory(new File("C:\\Prog2Inlupp2"));
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file == null)
					return;
				String filnamn = file.getAbsolutePath();

				FileInputStream fis = new FileInputStream(filnamn);
				Image image = new Image(new FileInputStream(filnamn), 600, 600, true, true);
				display.setImage(image);
				// ois.close();
				fis.close();

				//ObservableList<String> words = FXCollections.observableArrayList(uppslag.keySet());
				//FXCollections.sort(words);
				// listan.setItems(words);
			} catch (FileNotFoundException fnfe) {
				System.err.println("Ingen sån fil!");
				// }catch(ClassNotFoundException cnff){
				System.err.println("Fatalt fel har inträffat!");
			} catch (IOException ioe) {
				System.err.println("IO-fel har inträffat!");
				System.err.println(ioe.getMessage());
			}
		}
	}

	class SaveHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			try {
				FileChooser fileChooser = new FileChooser();
				File file = fileChooser.showSaveDialog(primaryStage);
				if (file == null)
					return;
				String filnamn = file.getAbsolutePath();

				FileOutputStream fos = new FileOutputStream(filnamn);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				//oos.writeObject(uppslag);
				oos.close();
				fos.close();
			} catch (FileNotFoundException fnfe) {
				System.err.println("Filen går ej att skriva!");
			} catch (IOException ioe) {
				System.err.println("Fel har inträffat!");
			}
		}
	}

	class CoordinateSearch implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			try {
				CoordinateHandler dialog = new CoordinateHandler();
				dialog.setTitle("Input coordinates:");
				Optional<ButtonType> result = dialog.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					//if (name.trim().isEmpty()) {
						//Alert msg = new Alert(AlertType.ERROR, "Empty name!");
						//msg.showAndWait();
					}
                double x = dialog.getXCoordinate();
				double y = dialog.getYCoordinate();
				// jämför de mottagna koordinaterna med de koordinater som finns i positionlist
				// om den inte hittar en plats ska det komma ett felmeddelande
				// gör alla (evenutella) markerade platser omarkerade
				// om dem mottagna koord hittar en plats ska den platsen bli markerad
				// och om den platsen är osynlig så ska den bli synlig


			} catch (NumberFormatException e)

			{
				Alert msg = new Alert(AlertType.ERROR);
				msg.setContentText("Error, incorrect input!");
				msg.showAndWait();
			} catch (NullPointerException e)

			{
				Alert msg = new Alert(AlertType.ERROR);
				msg.setContentText("Error, enter all fields please");
				msg.showAndWait();
			}
		}

	}
	
	class NewNamePlace implements EventHandler<ActionEvent> {
		String name;
		
		public void handle(ActionEvent event) {
			try {
				System.out.println("test");
				NamedPlaceHandler dialog = new NamedPlaceHandler();
				Optional<ButtonType> result = dialog.showAndWait();
				
				if (result.isPresent() && result.get() == ButtonType.OK) {
					if (name.trim().isEmpty()) {
						Alert msg = new Alert(AlertType.ERROR, "Empty name!");
						msg.showAndWait();
					}
					name = dialog.getName();
					//Objectet skapas:
					//Place nplace = new Place(name, Place.getCategory(), changed, null);
					//Objectet läggs i hashmap datastruktur:
				}
			} catch (NumberFormatException e) {
				Alert msg = new Alert(AlertType.ERROR);
				msg.setContentText("Error, incorrect input!");
				msg.showAndWait();
			} catch (NullPointerException e) {
				Alert msg = new Alert(AlertType.ERROR);
				msg.setContentText("Error, enter all fields please");
				msg.showAndWait();
			}

		}
	}

	class SearchHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			SearchButtonAction();
		}
	}

	public void SearchButtonAction() {
		System.out.println("Search button clicked");
		// avmarkera platser som evt är markerade
		// hämtar det som är i sökfältet
		// jämför den textsträngen med nyckeln i den datastrukturen som innehåller alla platser (kategorier spelar ingen roll här)
		// om den hittar en eller flera matches, ska de göras:
		// först synliga
		// sedan markerade
	}

	class HideHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			HideButtonAction();
		}
	}

	public void HideButtonAction() {
		System.out.println("Hide button clicked");
		// tar alla platser som är markerade i datastrukturen som innehåller markerade platser
		// gör dem osynliga

	}

	class RemoveHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			RemoveButtonAction();

		}
	}

	public void RemoveButtonAction() {
		System.out.println("Remove button clicked");
		// tar alla markerade platser
		// tar bort dem ur samtliga datastrukturer (som mest: den markerade datastrukturen plus positionList och nameList.)
	}

	class HideCategoryButtonHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			HideCategoryButtonAction();
		}
	}
	public void HideCategoryButtonAction() {
		System.out.println("Hide category button clicked");
		// kollar vilken kategori som är vald
		// gör alla av den kategoriNNNN markerade
		// göm alla markerade
	}

//	class ShowCategoryHandler implements EventHandler<MouseEvent> {
//		public void handle(ActionEvent event) {
//			ShowCategoryAction();
//		}
//	}
//	public void ShowCategoryAction() {
//		System.out.println("Hide category button clicked");
//		//
//
//		}






//	private void populate() {
//		placeList.put(K, V)(new Place("Helenelund", "Train", false, true, 40, 60));
//	}
	 
	public static void main(String[] args) {
		launch(args);
	}
}
