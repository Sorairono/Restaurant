package controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTabPane;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import model.*;

public class MainController implements Initializable {
	@FXML
	private JFXTabPane tabPaneMain;
	private Tab tabFrontOffice = new Tab("Front Office");
	private Tab tabBackOffice = new Tab("Back Office");
	private Tab tabKitchen = new Tab("Kitchen");
	private Tab tabReservation = new Tab("Reservation");
	private Tab tabSettings = new Tab("Settings");
	private Tab tabTablesFront = new Tab("Tables");
	private Tab tabPOS = new Tab("POS");
	private Tab tabTablesSetting = new Tab("Tables");
	private JFXTabPane tabPaneFront = new JFXTabPane();
	private JFXTabPane tabPaneSettings = new JFXTabPane();

	private ObservableList<Zone> zoneList = FXCollections.observableArrayList();
	private ObservableList<Table> tablesList = FXCollections.observableArrayList();
	private ObservableList<Category> categories = FXCollections.observableArrayList();
	private ObservableList<Product> productData = FXCollections.observableArrayList();
	private ObservableList<CartProduct> waitingList = FXCollections.observableArrayList();

	private POSController posController;
	private TablesController tablesController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tabPaneMain.getTabs().addAll(tabFrontOffice, tabBackOffice, tabKitchen, tabReservation, tabSettings);
		tabPaneFront.getTabs().addAll(tabTablesFront, tabPOS);
		tabPaneFront.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		tabFrontOffice.setContent(tabPaneFront);
		tabSettings.setContent(tabPaneSettings);
		tabPaneSettings.getTabs().add(tabTablesSetting);
		tabPaneSettings.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		zoneList.add(new Zone("Outside", "A"));
		zoneList.add(new Zone("Inside", "B"));
		zoneList.add(new Zone("Floor 2", "C"));
		zoneList.add(new Zone("Rooftop", "Z"));
		for (int i = 0; i < 10; i++) {
			Table newTable = new Table("A", i + 1, 4, false);
			tablesList.add(newTable);
			zoneList.get(0).getTablesList().add(newTable);
		}
		for (int i = 0; i < 20; i++) {
			Table newTable = new Table("B", i + 1, 6, false);
			tablesList.add(newTable);
			zoneList.get(1).getTablesList().add(newTable);
		}
		for (int i = 0; i < 30; i++) {
			Table newTable = new Table("C", i + 1, 8, true);
			tablesList.add(newTable);
			zoneList.get(2).getTablesList().add(newTable);
		}
		for (int i = 0; i < 15; i++) {
			Table newTable = new Table("Z", i + 1, 4, false);
			tablesList.add(newTable);
			zoneList.get(3).getTablesList().add(newTable);
		}
		categories.add(new Category("Khai vi", FXCollections.observableArrayList("Sup", "Chao", "Khac")));
		categories.add(new Category("Mon chinh",
				FXCollections.observableArrayList("Bo", "Ga", "Lon", "Hai San", "Lau", "Rau", "Com")));
		categories.add(new Category("Trang mieng", FXCollections.observableArrayList("Hoa Qua", "Kem", "Banh Ngot")));
		categories.add(new Category("Do uong", FXCollections.observableArrayList("Nuoc Ngot", "Bia", "Ruou")));
		productData.add(new Product("Sup 1", 20000, 0, 0));
		productData.add(new Product("Sup 2", 20000, 0, 0));
		productData.add(new Product("Sup 3", 20000, 0, 0));
		productData.add(new Product("Sup 4", 20000, 0, 0));
		productData.add(new Product("Sup 5", 20000, 0, 0));
		productData.add(new Product("Sup 6", 20000, 0, 0));
		productData.add(new Product("Sup 7", 20000, 0, 0));
		productData.add(new Product("Sup 8", 20000, 0, 0));
		productData.add(new Product("Sup 9", 20000, 0, 0));
		productData.add(new Product("Sup 10", 20000, 0, 0));
		productData.add(new Product("Sup 11", 20000, 0, 0));
		productData.add(new Product("Sup 12", 20000, 0, 0));
		productData.add(new Product("Sup 13", 20000, 0, 0));
		productData.add(new Product("Chao 1", 30000, 0, 1));
		productData.add(new Product("Chao 2", 30000, 0, 1));
		productData.add(new Product("Chao 3", 30000, 0, 1));
		productData.add(new Product("Chao 4", 30000, 0, 1));
		productData.add(new Product("Chao 5", 30000, 0, 1));
		productData.add(new Product("Chao 6", 30000, 0, 1));
		productData.add(new Product("Chao 7", 30000, 0, 1));
		try {
			FXMLLoader secondLoader = new FXMLLoader(getClass().getResource("/fxmlDocument/TablesView.fxml"));
			Parent secondUI = secondLoader.load();
			tabTablesFront.setContent(secondUI);
			tablesController = secondLoader.getController();
			tablesController.setMainController(this);
			tablesController.setZoneList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FXMLLoader secondLoader = new FXMLLoader(getClass().getResource("/fxmlDocument/POSView.fxml"));
			Parent secondUI = secondLoader.load();
			tabPOS.setContent(secondUI);
			posController = secondLoader.getController();
			posController.setMainController(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FXMLLoader secondLoader = new FXMLLoader(getClass().getResource("/fxmlDocument/KitchenView.fxml"));
			Parent secondUI = secondLoader.load();
			tabKitchen.setContent(secondUI);
			KitchenController kitchenController = secondLoader.getController();
			kitchenController.setMainController(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FXMLLoader secondLoader = new FXMLLoader(getClass().getResource("/fxmlDocument/TablesSettingView.fxml"));
			Parent secondUI = secondLoader.load();
			tabTablesSetting.setContent(secondUI);
			TablesSettingController tablesSettingController = secondLoader.getController();
			tablesSettingController.setMainController(this);
			tablesSettingController.setContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		tabPaneMain.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			if (!tabFrontOffice.isSelected()) {
				tablesController.clearZoneSelection();
			}
		});
	}

	public ObservableList<Zone> getZoneList() {
		return zoneList;
	}

	public ObservableList<Table> getTablesList() {
		return tablesList;
	}

	public ObservableList<Category> getCategories() {
		return categories;
	}

	public ObservableList<Product> getProductData() {
		return productData;
	}

	public ObservableList<CartProduct> getWaitingList() {
		return waitingList;
	}

	public boolean confirmDialog(String contentText) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setContentText(contentText);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	public void openPOSWithTableNumber(String tableNumber) {
		posController.setTableNumber(tableNumber);
		tabPOS.getTabPane().getSelectionModel().select(tabPOS);
	}

	public void setMessage(String s, Label message) {
		message.setText(s);
		FadeTransition fader = createFader(message);
		SequentialTransition fade = new SequentialTransition(message, fader);
		fade.play();
	}

	private FadeTransition createFader(Node node) {
		FadeTransition fade = new FadeTransition(Duration.seconds(5), node);
		fade.setFromValue(1);
		fade.setToValue(0);
		return fade;
	}

	public void lockedSizeStage(Stage stage) {
		stage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				stage.setMaximized(false);
			}
		});
		stage.setResizable(false);
	}
}
