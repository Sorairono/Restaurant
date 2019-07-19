package controller;

import java.net.URL;
import java.util.*;

import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Table;

public class GuestPaymentController implements Initializable {
	@FXML
	private TextField customerName;
	@FXML
	private Label totalLabel;
	@FXML
	private Label tableNumber;
	@FXML
	private Label message;
	@FXML
	private Button cancelButton;
	@FXML
	private Button completeButton;
	
	private boolean completeClicked = false;
	private Table currentTable;
	private boolean waitingPayment = false;
	private MainController main;

	@FXML
	private void handlePrint() {
		try {
			FXMLLoader fourthLoader = new FXMLLoader(getClass().getResource("/fxmlDocument/BillView.fxml"));
			Parent fourthUI = fourthLoader.load();
			BillController billController = fourthLoader.getController();
			billController.setContent(currentTable);
			billController.setCustomerName(customerName.getText());
			Scene scene = new Scene(fourthUI);
			Stage dialogStage = new Stage();
			main.lockedSizeStage(dialogStage);
			dialogStage.setScene(scene);
			dialogStage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleCash() {
		main.setMessage("Press Complete button when you receive enough money from customer", message);
		waitingPayment = true;
	}

	@FXML
	private void handleCreditCard() {
		main.setMessage("Press Complete button when you receive enough money from customer", message);
		waitingPayment = true;
	}

	@FXML
	private void handleCancel() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void handleConfirmPayment() {
		if (waitingPayment) {
			completeClicked = true;
			if (currentTable.getMergeBoolean()) {
				for (int i = 0; i < main.getZoneList().size(); i++) {
					if (currentTable.getZoneLetter().compareTo(main.getZoneList().get(i).getZoneLetter()) == 0) {
						for (int j = 0; j < main.getZoneList().get(i).getMergedTablesList().size(); j++) {
							if (main.getZoneList().get(i).getMergedTablesList().get(j).getMergeTable().compareTo(currentTable.toString()) == 0) {
								main.getZoneList().get(i).getMergedTablesList().get(j).setMergeProperty(false);
								main.getZoneList().get(i).getMergedTablesList().get(j).setMergeTable("");
								main.getZoneList().get(i).getMergedTablesList().remove(j);
								j--;
							}
						}
						break;
					}
				}
			}
			currentTable.clearTable();
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.close();
		} else {
			main.setMessage("Please choose a method of payment first", message);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	public void setMainController(MainController mc) {
		main = mc;
	}
	
	public void setTable(Table table) {
		currentTable = table;
		tableNumber.setText(currentTable.toString());
		totalLabel.setText(String.valueOf(currentTable.getBalance()));
	}
	
	public boolean isCompleteClicked() {
		return completeClicked;
	}

}
