package view;

import java.util.HashMap;
import java.util.Map;

import automata.Automaton.Action;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.Environment;
import main.Environment.Machine;
import main.Experiment;

public class Interface extends Application {

	private static final int MARGIN = 20;
	
	private Scene scene;
	private Stage primaryStage;
	private BorderPane root;
	private Bounds bounds;
	
	private Environment environment;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		environment = new Environment(this);
		draw();
	}
	
	private void draw() {
		root = new BorderPane();
		root.setPadding(new Insets(10, 20, 10, 20));
		drawButtons();
		drawTable();
		setScene();
		setStage();
	}
	
	private void drawButtons() {
		HBox buttonPane = new HBox(50);
		
		Button tsetlin = new Button("Testlin Automaton");
		tsetlin.setOnAction(e -> {
			environment.setAutomaton(Machine.TSETLIN);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Automaton Changed");
			alert.setHeaderText(null);
			alert.setContentText("Tsetlin Automaton will now be used.");
			alert.showAndWait();
		});
		
		Button krinsky = new Button("Krinsky Automaton");
		krinsky.setOnAction(e -> {
			environment.setAutomaton(Machine.KRINSKY);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Automaton Changed");
			alert.setHeaderText(null);
			alert.setContentText("Krinsky Automaton will now be used.");
			alert.showAndWait();
		});
		
		Button krylov = new Button("Krylov Automaton");
		krylov.setOnAction(e -> {
			environment.setAutomaton(Machine.KRYLOV);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Automaton Changed");
			alert.setHeaderText(null);
			alert.setContentText("Krylov Automaton will now be used.");
			alert.showAndWait();
		});
		
		Button lri = new Button("LRI Scheme");
		lri.setOnAction(e -> {
			environment.setAutomaton(Machine.LRI);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Automaton Changed");
			alert.setHeaderText(null);
			alert.setContentText("LRI Scheme will now be used.");
			alert.showAndWait();
		});
		
		Button start = new Button("Start");
		start.setOnAction(e -> {
			environment.start();
		});
		
		Button reset = new Button("Reset");
		reset.setOnAction(e -> {
			environment.reset();
		});
		
		buttonPane.getChildren().addAll(tsetlin, krinsky, krylov, lri, start, reset);
		buttonPane.setAlignment(Pos.CENTER);
		root.setBottom(buttonPane);
		BorderPane.setAlignment(buttonPane, Pos.CENTER);
	}
	
	@SuppressWarnings("unchecked")
	private void drawTable() {
		TableView<Experiment> table = new TableView<Experiment>();
		ObservableList<Experiment> experiments = FXCollections.observableArrayList(
				environment.getExperiments());
		table.setItems(experiments);
		
		TableColumn<Experiment, String> numCol = new TableColumn<Experiment, String>("Experiment Number");
		numCol.setCellValueFactory(new PropertyValueFactory<>("experimentNum"));
		
		TableColumn<Experiment, String> action1Col = new TableColumn<Experiment, String>("Action 1 %");
		action1Col.setCellValueFactory(new PropertyValueFactory<>("action1Percentage"));
		
		TableColumn<Experiment, String> action2Col = new TableColumn<Experiment, String>("Action 2 %");
		action2Col.setCellValueFactory(new PropertyValueFactory<>("action2Percentage"));
		
		TableColumn<Experiment, String> action3Col = new TableColumn<Experiment, String>("Action 3 %");
		action3Col.setCellValueFactory(new PropertyValueFactory<>("action3Percentage"));
		
		TableColumn<Experiment, String> action4Col = new TableColumn<Experiment, String>("Action 4 %");
		action4Col.setCellValueFactory(new PropertyValueFactory<>("action4Percentage"));
		
		TableColumn<Experiment, String> action5Col = new TableColumn<Experiment, String>("Action 5 %");
		action5Col.setCellValueFactory(new PropertyValueFactory<>("action5Percentage"));
		
		TableColumn<Experiment, String> action6Col = new TableColumn<Experiment, String>("Action 6 %");
		action6Col.setCellValueFactory(new PropertyValueFactory<>("action6Percentage"));
		
		TableColumn<Experiment, String> initAction = new TableColumn<Experiment, String>("Initial Action");
		initAction.setCellValueFactory(new PropertyValueFactory<>("initialAction"));
		
		table.getColumns().addAll(numCol, action1Col, action2Col, action3Col, action4Col, action5Col, 
				action6Col, initAction);
		
		root.setCenter(table);
	}
	
	private void setScene() {
		bounds = root.getLayoutBounds();
		scene = new Scene(root);
	}
	
	private void setStage() {
		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	    double factor = Math.min(visualBounds.getWidth() / (bounds.getWidth() + MARGIN),
	            visualBounds.getHeight() / (bounds.getHeight() + MARGIN));
	    primaryStage.setScene(scene);
	    primaryStage.setWidth((bounds.getWidth() + MARGIN) * factor);
	    primaryStage.setHeight((bounds.getHeight() + MARGIN) * factor);
	    primaryStage.setTitle("Learning Automata");
	    primaryStage.show();
	}
	
	public void displayAverages(HashMap<Action, Double> averageWaitTimes) {
		Dialog<?> dialog = new Dialog<>();
		dialog.setTitle("Average Wait Times");
		dialog.setHeaderText("Average wait times for each floor");
		
		ButtonType confirm = new ButtonType("Confirm", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(confirm);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		int rowIndex = 0;
		for(Map.Entry<Action, Double> averageWaitTime: averageWaitTimes.entrySet()) {
			grid.add(new Label(averageWaitTime.getKey() + ": "), 0, rowIndex);
			grid.add(new Label(averageWaitTime.getValue().toString()), 1, rowIndex);
			rowIndex++;
		}
		
		dialog.getDialogPane().setContent(grid);
		
		dialog.setResultConverter(button -> {
			if(button == confirm) {
				return null;
			}
			return null;
		});
		
		dialog.showAndWait();
	}
	
	public static void main(String args[]) {
		launch(args);
	}
}
