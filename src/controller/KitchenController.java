package controller;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CartProduct;

public class KitchenController implements Initializable {
	@FXML
	private TableView<CartProduct> tableView;
	private TableColumn<CartProduct, String> productNameList = new TableColumn<CartProduct, String>("Product Name");
	private TableColumn<CartProduct, Number> quantityList = new TableColumn<CartProduct, Number>("Quantity");
	private TableColumn<CartProduct, LocalTime> timeList = new TableColumn<CartProduct, LocalTime>("Time");

	@FXML
	private void handleReady() {
		if (tableView.getSelectionModel().getSelectedIndex() != -1) {
			for (int i = 0; i < main.getTablesList().size(); i++) {
				if (main.getTablesList().get(i).toString()
						.compareTo(tableView.getSelectionModel().getSelectedItem().getTableNumber()) == 0) {
					for (int j = 0; j < main.getTablesList().get(i).getCustomerProduct().size(); j++) {
						if (main.getTablesList().get(i).getCustomerProduct().get(j)
								.equals(tableView.getSelectionModel().getSelectedItem())) {
							main.getTablesList().get(i).getCustomerProduct().get(j).productReady();
							main.getTablesList().get(i).setFoodReady(true);
							main.getWaitingList().remove(tableView.getSelectionModel().getSelectedIndex());
							tableView.getSelectionModel().clearSelection();
							return;
						}
					}
				}
			}
		}
	}

	private MainController main;

	@SuppressWarnings({ "unchecked" })
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		productNameList.prefWidthProperty().bind(tableView.widthProperty().multiply(0.6));
		productNameList.setCellValueFactory(c -> c.getValue().productNameProperty());
		quantityList.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
		quantityList.setCellValueFactory(c -> c.getValue().amountProperty());
		timeList.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
		timeList.setCellValueFactory(c -> c.getValue().timeOrderedProperty());
		tableView.getColumns().addAll(productNameList, quantityList, timeList);
	}

	public void setMainController(MainController mc) {
		main = mc;
		tableView.setItems(main.getWaitingList());
	}
}
