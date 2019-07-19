package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import model.Table;
import model.Zone;

public class TablesController implements Initializable {
	@FXML
	private ChoiceBox<Zone> zoneChoice;
	@FXML
	private HBox move;
	@FXML
	private TextField fromMove;
	@FXML
	private TextField toMove;
	@FXML
	private TextField fromMerge;
	@FXML
	private TextField toMerge;
	@FXML
	private HBox merge;
	@FXML
	private ToggleButton moveButton;
	@FXML
	private ToggleButton mergeButton;
	@FXML
	private Label message;
	@FXML
	private ScrollPane scrollPane;
	private final IntegerProperty columnCount = new SimpleIntegerProperty(0);
	private int rowCount = 0;
	private MainController main;
	private GridPane gridPane = new GridPane();
	private Zone currentZone;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		gridPane.setPadding(new Insets(50));
		gridPane.setVgap(100.0);
		gridPane.setHgap(100.0);
		gridPane.prefWidthProperty().bind(scrollPane.widthProperty());
		scrollPane.setContent(gridPane);
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		columnCount.bind(scrollPane.widthProperty().divide(250));
		zoneChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				currentZone = newValue;
				setTableMap();
			}
		});
		fromMove.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
		toMove.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
		fromMerge.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
		toMerge.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
	}

	@FXML
	private void handleMove() {
		if (moveButton.isSelected()) {
			move.setVisible(true);
			merge.setVisible(false);
			mergeButton.setSelected(false);
			fromMerge.setText("");
			toMerge.setText("");
			main.setMessage("Please enter table numbers to move. Table moving from must be an occupied table", message);
		} else {
			move.setVisible(false);
			fromMove.setText("");
			toMove.setText("");
		}
	}

	@FXML
	private void handleMerge() {
		if (mergeButton.isSelected()) {
			merge.setVisible(true);
			move.setVisible(false);
			moveButton.setSelected(false);
			fromMove.setText("");
			toMove.setText("");
			main.setMessage(
					"Please enter table numbers to merge. Tables merging from must be unoccupied tables, seperated by a single white space, and table merging to must be an occupied table",
					message);
		} else {
			merge.setVisible(false);
			fromMerge.setText("");
			toMerge.setText("");
		}
	}

	@FXML
	private void handleOkMove() {
		boolean foundTableFrom = false;
		boolean foundTableTo = false;
		int moveFromIndex = 0;
		int moveToIndex = 0;
		for (int i = 0; i < main.getTablesList().size(); i++) {
			if (fromMove.getText().compareTo(main.getTablesList().get(i).toString()) == 0) {
				foundTableFrom = true;
				moveFromIndex = i;
				if (!main.getTablesList().get(i).getOccupied()) {
					main.setMessage("The table to move from is unoccupied", message);
					return;
				} else if (main.getTablesList().get(i).getMergeBoolean()) {
					main.setMessage("Cannot move a merged table", message);
					return;
				} else if (foundTableTo) {
					break;
				}
			} else if (toMove.getText().compareTo(main.getTablesList().get(i).toString()) == 0) {
				foundTableTo = true;
				moveToIndex = i;
				if (main.getTablesList().get(i).getOccupied()) {
					main.setMessage("The table to move to is occupied", message);
					return;
				} else if (main.getTablesList().get(i).getMergeBoolean()) {
					main.setMessage("Cannot move to a merged table", message);
					return;
				} else if (foundTableFrom) {
					break;
				}
			}
		}
		if (!foundTableFrom) {
			main.setMessage("Cannot find the table to move from", message);
			return;
		} else if (!foundTableTo) {
			main.setMessage("Cannot find the table to move to", message);
			return;
		} else {
			main.getTablesList().get(moveToIndex).setTable(main.getTablesList().get(moveFromIndex));
			main.getTablesList().get(moveFromIndex).clearTable();
			main.setMessage("Successfully moved table " + fromMove.getText() + " to table " + toMove.getText(),
					message);
			moveButton.setSelected(false);
			move.setVisible(false);
			fromMove.setText("");
			toMove.setText("");
		}
	}

	@FXML
	private void handleOkMerge() {
		String[] tables = fromMerge.getText().split(" ");
		ObservableList<Table> mergeList = FXCollections.observableArrayList();
		for (int i = 0; i < tables.length; i++) {
			boolean foundTable = false;
			for (int j = 0; j < currentZone.getTablesList().size(); j++) {
				if (currentZone.getTablesList().get(j).toString().compareTo(tables[i]) == 0) {
					foundTable = true;
					if (!currentZone.getTablesList().get(j).getOccupied()) {
						if (!currentZone.getTablesList().get(j).getMergeBoolean()) {
							mergeList.add(currentZone.getTablesList().get(j));
							break;
						} else {
							main.setMessage("Table " + tables[i] + " is already merged", message);
							return;
						}
					} else {
						main.setMessage("Table " + tables[i] + " is already occupied", message);
						return;
					}
				}
			}
			if (!foundTable) {
				main.setMessage("Cannot find table " + tables[i] + " in this zone", message);
				return;
			}
		}
		boolean foundTable = false;
		for (int i = 0; i < currentZone.getTablesList().size(); i++) {
			if (currentZone.getTablesList().get(i).toString().compareTo(toMerge.getText()) == 0) {
				foundTable = true;
				if (currentZone.getTablesList().get(i).getOccupied()) {
					currentZone.getTablesList().get(i).setMergeProperty(true);
					break;
				} else {
					main.setMessage("Table " + toMerge.getText() + " must be occupied first", message);
					return;
				}
			}
		}
		if (!foundTable) {
			main.setMessage("Cannot find table " + toMerge.getText() + " in this zone", message);
			return;
		}
		for (int i = 0; i < mergeList.size(); i++) {
			mergeList.get(i).setMergeTable(toMerge.getText());
			mergeList.get(i).setMergeProperty(true);
			currentZone.getMergedTablesList().add(mergeList.get(i));
		}
		setTableMap();
		main.setMessage("Successfully merged tables " + fromMerge.getText() + " to tables " + toMerge.getText(),
				message);
		mergeButton.setSelected(false);
		merge.setVisible(false);
		fromMerge.setText("");
		toMerge.setText("");
	}

	private void setTableMap() {
		gridPane.getChildren().clear();
		ObservableList<Table> displayTables = FXCollections.observableArrayList();
		for (int i = 0; i < currentZone.getTablesList().size(); i++) {
			if (!currentZone.getMergedTablesList().contains(currentZone.getTablesList().get(i))) {
				displayTables.add(currentZone.getTablesList().get(i));
			}
		}
		rowCount = (int) displayTables.size() / columnCount.get();
		if (rowCount * columnCount.get() < displayTables.size()) {
			rowCount++;
		}
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount.get(); j++) {
				int nodeIndex = j + i * columnCount.get();
				if (nodeIndex < displayTables.size()) {
					Label newLabel = new Label("Table " + displayTables.get(nodeIndex).toString() + "\n"
							+ displayTables.get(nodeIndex).getCapacity() + " people");
					newLabel.setStyle("-fx-text-fill: white");
					newLabel.setAlignment(Pos.CENTER);
					StackPane stack = new StackPane();
					if (displayTables.get(nodeIndex).isCircle()) {
						Ellipse newEllipse = new Ellipse(75.0, 50.0);
						newEllipse.setFill(Paint.valueOf("gray"));
						stack.getChildren().add(newEllipse);
					} else {
						Rectangle newRectangle = new Rectangle(150.0, 100.0, Paint.valueOf("gray"));
						stack.getChildren().add(newRectangle);
					}
					stack.getChildren().add(newLabel);
					stack.setId(displayTables.get(nodeIndex).toString());
					stack.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							if (!displayTables.get(nodeIndex).getOccupied()) {
								if (main.confirmDialog("Do you want to open a bill at table "
										+ displayTables.get(nodeIndex).toString() + "?")) {
									displayTables.get(nodeIndex).setOccupied(true);
									main.openPOSWithTableNumber(displayTables.get(nodeIndex).toString());
								}
							} else {
								main.openPOSWithTableNumber(displayTables.get(nodeIndex).toString());
							}
						}
					});
					Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), c -> {
						if (displayTables.get(nodeIndex).getOccupied()) {
							if (displayTables.get(nodeIndex).getFoodReady()) {
								((Shape) stack.getChildren().get(0)).setFill(Paint.valueOf("green"));
							} else {
								((Shape) stack.getChildren().get(0)).setFill(Paint.valueOf("cyan"));
							}
						} else {
							((Shape) stack.getChildren().get(0)).setFill(Paint.valueOf("gray"));
						}
					}));
					timeline.setCycleCount(Animation.INDEFINITE);
					timeline.play();
					displayTables.get(nodeIndex).mergeProperty().addListener((observable, oldValue, newValue) -> {
						if (oldValue == true && newValue == false) {
							setTableMap();
						}
					});
					gridPane.add(stack, j, i);
				}
			}
		}
	}

	public void setMainController(MainController mc) {
		main = mc;
	}

	public void setZoneList() {
		zoneChoice.setItems(main.getZoneList());
	}

	public void clearZoneSelection() {
		zoneChoice.getSelectionModel().clearSelection();
		gridPane.getChildren().clear();
	}
}
