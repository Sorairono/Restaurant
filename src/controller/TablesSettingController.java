package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import model.*;

public class TablesSettingController implements Initializable {
	@FXML
	private HBox hBox;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private ToggleButton addTableButton;
	@FXML
	private ToggleButton moveTableButton;
	@FXML
	private ToggleButton changeCapacityButton;
	@FXML
	private ToggleButton deleteTableButton;
	@FXML
	private HBox addTableBar;
	@FXML
	private HBox moveTableBar;
	@FXML
	private HBox changeCapacityBar;
	@FXML
	private HBox deleteTableBar;
	@FXML
	private ChoiceBox<Zone> addTableZoneBox;
	@FXML
	private ChoiceBox<Integer> addTableCapacityBox;
	@FXML
	private ChoiceBox<String> addTablePropertyBox;
	@FXML
	private TextField moveTableFromField;
	@FXML
	private ChoiceBox<Zone> moveTableToZoneBox;
	@FXML
	private TextField changeCapacityTableField;
	@FXML
	private ChoiceBox<Integer> changeCapacityToBox;
	@FXML
	private TextField deleteTableField;
	@FXML
	private Label message;

	private MainController main;

	@FXML
	private void handleAddTable() {
		int newTableNumber = addTableZoneBox.getSelectionModel().getSelectedItem().getTablesList().size() + 1;
		boolean isCircle = false;
		if (addTablePropertyBox.getSelectionModel().getSelectedItem().compareTo("Circle") == 0) {
			isCircle = true;
		}
		Table newTable = new Table(addTableZoneBox.getSelectionModel().getSelectedItem().getZoneLetter(),
				newTableNumber, addTableCapacityBox.getSelectionModel().getSelectedItem(), isCircle);
		addTableZoneBox.getSelectionModel().getSelectedItem().getTablesList().add(newTable);
		main.getTablesList().add(newTable);
		main.setMessage("Successfully added table " + newTable.toString() + " to "
				+ addTableZoneBox.getSelectionModel().getSelectedItem().toString(), message);
		addTableButton.setSelected(false);
		addTableBar.setVisible(false);
		addTableZoneBox.getSelectionModel().selectFirst();
		addTableCapacityBox.getSelectionModel().selectFirst();
		addTablePropertyBox.getSelectionModel().selectFirst();
	}

	@FXML
	private void handleMoveTable() {
		for (int i = 0; i < main.getTablesList().size(); i++) {
			if (moveTableFromField.getText().compareTo(main.getTablesList().get(i).toString()) == 0) {
				if (!main.getTablesList().get(i).getOccupied() && !main.getTablesList().get(i).getMergeBoolean()) {
					if (main.getTablesList().get(i).getZoneLetter()
							.compareTo(moveTableToZoneBox.getSelectionModel().getSelectedItem().getZoneLetter()) != 0) {
						for (int j = 0; j < main.getZoneList().size(); j++) {
							if (main.getTablesList().get(i).getZoneLetter()
									.compareTo(main.getZoneList().get(j).getZoneLetter()) == 0) {
								for (int k = 0; k < main.getZoneList().get(j).getTablesList().size(); k++) {
									if (main.getTablesList().get(i).toString().compareTo(
											main.getZoneList().get(j).getTablesList().get(k).toString()) == 0) {
										main.getZoneList().get(j).getTablesList().remove(k);
										for (int l = 0; l < main.getZoneList().get(j).getTablesList().size(); l++) {
											main.getZoneList().get(j).getTablesList().get(l).setTableNumber(l + 1);
										}
										break;
									}
								}
								break;
							}
						}
						int newTableNumber = moveTableToZoneBox.getSelectionModel().getSelectedItem().getTablesList()
								.size() + 1;
						main.getTablesList().get(i).setTableNumber(newTableNumber);
						main.getTablesList().get(i).setZoneLetter(
								moveTableToZoneBox.getSelectionModel().getSelectedItem().getZoneLetter());
						moveTableToZoneBox.getSelectionModel().getSelectedItem().getTablesList()
								.add(main.getTablesList().get(i));
						main.setMessage("Successfully moved table " + moveTableFromField.getText() + " to "
								+ moveTableToZoneBox.getSelectionModel().getSelectedItem().toString(), message);
						moveTableButton.setSelected(false);
						moveTableBar.setVisible(false);
						moveTableFromField.setText("");
						moveTableToZoneBox.getSelectionModel().selectFirst();
					} else {
						main.setMessage("The table is already in this zone", message);
					}
				} else {
					main.setMessage("The table is in use", message);
				}
				return;
			}
		}
		main.setMessage("Couldn't find this table", message);
	}

	@FXML
	private void handleChangeCapacity() {
		for (int i = 0; i < main.getTablesList().size(); i++) {
			if (changeCapacityTableField.getText().compareTo(main.getTablesList().get(i).toString()) == 0) {
				if (main.getTablesList().get(i).getCapacity() != changeCapacityToBox.getSelectionModel()
						.getSelectedItem()) {
					main.getTablesList().get(i).setCapacity(changeCapacityToBox.getSelectionModel().getSelectedItem());
					main.setMessage("Successfully changed table " + changeCapacityTableField.getText()
							+ "'s capacity to " + changeCapacityToBox.getSelectionModel().getSelectedItem(), message);
					changeCapacityButton.setSelected(false);
					changeCapacityBar.setVisible(false);
					changeCapacityTableField.setText("");
					changeCapacityToBox.getSelectionModel().selectFirst();
				} else {
					main.setMessage("The table's capacity is already " + main.getTablesList().get(i).getCapacity(),
							message);
				}
				return;
			}
		}
		main.setMessage("Couldn't find this table", message);
	}

	@FXML
	private void handleDeleteTable() {
		for (int i = 0; i < main.getTablesList().size(); i++) {
			if (deleteTableField.getText().compareTo(main.getTablesList().get(i).toString()) == 0) {
				if (main.getTablesList().get(i).getStatus().compareTo("Empty") == 0) {
					if (main.confirmDialog(
							"Are you sure you want to delete table " + main.getTablesList().get(i).toString())) {
						for (int j = 0; j < main.getZoneList().size(); j++) {
							if (main.getTablesList().get(i).getZoneLetter()
									.compareTo(main.getZoneList().get(j).getZoneLetter()) == 0) {
								main.getZoneList().get(j).getTablesList()
										.remove(main.getTablesList().get(i).getTableNumber() - 1);
								for (int k = 0; k < main.getZoneList().get(j).getTablesList().size(); k++) {
									main.getZoneList().get(j).getTablesList().get(k).setTableNumber(k + 1);
								}
								break;
							}
						}
						main.getTablesList().remove(i);
						deleteTableButton.setSelected(false);
						deleteTableBar.setVisible(false);
						deleteTableField.setText("");
					}
				} else {
					main.setMessage("The table is in use", message);
				}
				return;
			}
		}
		main.setMessage("Couldn't find this table", message);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		hBox.prefHeightProperty().bind(scrollPane.heightProperty());
		hBox.setSpacing(100.0);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		addTableCapacityBox.setItems(FXCollections.observableArrayList(4, 6, 8, 12));
		addTableCapacityBox.getSelectionModel().selectFirst();
		addTablePropertyBox.setItems(FXCollections.observableArrayList("Rectangle", "Circle"));
		addTablePropertyBox.getSelectionModel().selectFirst();
		changeCapacityToBox.setItems(FXCollections.observableArrayList(4, 6, 8, 12));
		changeCapacityToBox.getSelectionModel().selectFirst();
		addTableButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (addTableButton.isSelected()) {
					addTableBar.setVisible(true);
					moveTableButton.setSelected(false);
					moveTableBar.setVisible(false);
					changeCapacityButton.setSelected(false);
					changeCapacityBar.setVisible(false);
					deleteTableButton.setSelected(false);
					deleteTableBar.setVisible(false);
				} else {
					addTableBar.setVisible(false);
					addTableZoneBox.getSelectionModel().selectFirst();
					addTableCapacityBox.getSelectionModel().selectFirst();
					addTablePropertyBox.getSelectionModel().selectFirst();
				}
			}
		});
		moveTableButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (moveTableButton.isSelected()) {
					moveTableBar.setVisible(true);
					addTableButton.setSelected(false);
					addTableBar.setVisible(false);
					changeCapacityButton.setSelected(false);
					changeCapacityBar.setVisible(false);
					deleteTableButton.setSelected(false);
					deleteTableBar.setVisible(false);
				} else {
					moveTableBar.setVisible(false);
					moveTableFromField.setText("");
					moveTableToZoneBox.getSelectionModel().selectFirst();
				}
			}
		});
		changeCapacityButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (changeCapacityButton.isSelected()) {
					changeCapacityBar.setVisible(true);
					addTableButton.setSelected(false);
					addTableBar.setVisible(false);
					moveTableButton.setSelected(false);
					moveTableBar.setVisible(false);
					deleteTableButton.setSelected(false);
					deleteTableBar.setVisible(false);
				} else {
					changeCapacityBar.setVisible(false);
					changeCapacityTableField.setText("");
					changeCapacityToBox.getSelectionModel().selectFirst();
				}
			}
		});
		deleteTableButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (deleteTableButton.isSelected()) {
					deleteTableBar.setVisible(true);
					addTableButton.setSelected(false);
					addTableBar.setVisible(false);
					moveTableButton.setSelected(false);
					moveTableBar.setVisible(false);
					changeCapacityButton.setSelected(false);
					changeCapacityBar.setVisible(false);
				} else {
					deleteTableBar.setVisible(false);
					deleteTableField.setText("");
				}
			}
		});
		moveTableFromField.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
		moveTableFromField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > 3) {
					moveTableFromField.setText(oldValue);
				}
			}
		});
		changeCapacityTableField.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
		changeCapacityTableField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > 3) {
					changeCapacityTableField.setText(oldValue);
				}
			}
		});
		deleteTableField.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
		deleteTableField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > 3) {
					deleteTableField.setText(oldValue);
				}
			}
		});
	}

	public void setMainController(MainController mc) {
		main = mc;
	}

	@SuppressWarnings("unchecked")
	public void setContent() {
		addTableZoneBox.setItems(main.getZoneList());
		addTableZoneBox.getSelectionModel().selectFirst();
		moveTableToZoneBox.setItems(main.getZoneList());
		moveTableToZoneBox.getSelectionModel().selectFirst();
		for (int i = 0; i < main.getZoneList().size(); i++) {
			AnchorPane newAnchorPane = new AnchorPane();
			Label zoneNameLabel = new Label("Zone Name: " + main.getZoneList().get(i).toString());
			zoneNameLabel.setPrefHeight(50.0);
			zoneNameLabel.setAlignment(Pos.CENTER);
			AnchorPane.setTopAnchor(zoneNameLabel, 0.0);
			AnchorPane.setLeftAnchor(zoneNameLabel, 0.0);
			AnchorPane.setRightAnchor(zoneNameLabel, 0.0);
			Label zoneLetterLabel = new Label("Zone Letter: " + main.getZoneList().get(i).getZoneLetter());
			zoneLetterLabel.setPrefHeight(50.0);
			zoneLetterLabel.setAlignment(Pos.CENTER);
			AnchorPane.setTopAnchor(zoneLetterLabel, 50.0);
			AnchorPane.setLeftAnchor(zoneLetterLabel, 0.0);
			AnchorPane.setRightAnchor(zoneLetterLabel, 0.0);
			TableView<Table> zoneTableList = new TableView<Table>();
			TableColumn<Table, Number> tablesNumberList = new TableColumn<Table, Number>("No.");
			TableColumn<Table, Number> capacityList = new TableColumn<Table, Number>("Capacity");
			TableColumn<Table, String> propertiesList = new TableColumn<Table, String>("Properties");
			TableColumn<Table, String> statusList = new TableColumn<Table, String>("Status");
			tablesNumberList.prefWidthProperty().bind(zoneTableList.widthProperty().multiply(0.15));
			tablesNumberList.setCellValueFactory(c -> c.getValue().tableNumberProperty());
			capacityList.prefWidthProperty().bind(zoneTableList.widthProperty().multiply(0.275));
			capacityList.setCellValueFactory(c -> c.getValue().capacityProperty());
			propertiesList.prefWidthProperty().bind(zoneTableList.widthProperty().multiply(0.3));
			propertiesList.setCellValueFactory(c -> c.getValue().propertyProperty());
			statusList.prefWidthProperty().bind(zoneTableList.widthProperty().multiply(0.275));
			statusList.setCellValueFactory(c -> c.getValue().statusProperty());
			zoneTableList.getStylesheets().add("/css/noScrollBarTableView.css");
			zoneTableList.getColumns().addAll(tablesNumberList, capacityList, propertiesList, statusList);
			zoneTableList.setItems(main.getZoneList().get(i).getTablesList());
			AnchorPane.setTopAnchor(zoneTableList, 100.0);
			AnchorPane.setLeftAnchor(zoneTableList, 0.0);
			AnchorPane.setRightAnchor(zoneTableList, 0.0);
			AnchorPane.setBottomAnchor(zoneTableList, 0.0);
			newAnchorPane.getChildren().addAll(zoneNameLabel, zoneLetterLabel, zoneTableList);
			newAnchorPane.setPrefWidth(300.0);
			hBox.getChildren().add(newAnchorPane);
		}
	}
}
