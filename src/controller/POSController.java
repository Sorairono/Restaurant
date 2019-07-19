package controller;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.CartProduct;
import model.Coupon;
import model.Product;
import model.Table;

public class POSController implements Initializable {
	@FXML
	private Label tableMessage;
	@FXML
	private HBox mainPane;
	@FXML
	private BorderPane cartPane;
	@FXML
	private VBox choicePane;
	@FXML
	private AnchorPane cartBottom;
	@FXML
	private ScrollPane categoriesPane;
	@FXML
	private ScrollPane typesPane;
	@FXML
	private ScrollPane menuPane;
	@FXML
	private TableView<CartProduct> cartView;
	@FXML
	private HBox changeTableBox;
	@FXML
	private JFXTextField tableInput;
	@FXML
	private Label tableNumber;
	@FXML
	private JFXToggleButton changeTableButton;
	@FXML
	private JFXButton enterButton;
	@FXML
	private Label message;
	@FXML
	private Label subTotalLabel;
	@FXML
	private Label taxLabel;
	@FXML
	private Label totalLabel;
	@FXML
	private Label discountLabel;
	@FXML
	private Label balanceLabel;
	@FXML
	private JFXToggleButton discountButton;
	@FXML
	private HBox discountInput;
	@FXML
	private JFXTextField discountField;
	@FXML
	private JFXTextField filterField;
	private TableColumn<CartProduct, String> productNameList = new TableColumn<CartProduct, String>("Name");
	private TableColumn<CartProduct, CartProduct> quantityList = new TableColumn<CartProduct, CartProduct>("Quantity");
	private TableColumn<CartProduct, Number> eachList = new TableColumn<CartProduct, Number>("Each");
	private TableColumn<CartProduct, Number> totalList = new TableColumn<CartProduct, Number>("Total");
	private TableColumn<CartProduct, LocalTime> timeOrderedList = new TableColumn<CartProduct, LocalTime>(
			"Time Ordered");
	private TableColumn<CartProduct, LocalTime> timeCompletedList = new TableColumn<CartProduct, LocalTime>(
			"Time Completed");
	private TableColumn<CartProduct, String> statusList = new TableColumn<CartProduct, String>("Status");
	private MainController main;
	private IntegerProperty columnCount = new SimpleIntegerProperty(0);
	private GridPane gridPane1 = new GridPane();
	private GridPane gridPane2 = new GridPane();
	private GridPane gridPane3 = new GridPane();
	private ObservableList<Product> displayMenu = FXCollections.observableArrayList();
	private ObservableList<CartProduct> tableProductList = FXCollections.observableArrayList();
	private ObjectProperty<TableRow<CartProduct>> lastSelectedRow = new SimpleObjectProperty<>();
	@SuppressWarnings("serial")
	private static List<Coupon> couponList = new ArrayList<Coupon>() {
		{
			add(new Coupon("MAGIAMGIA1", false, 200000));
			add(new Coupon("MAGIAMGIA2", false, 300000));
			add(new Coupon("MAGIAMGIA3", false, 500000));
			add(new Coupon("MAGIAMGIA4", true, 0.3));
			add(new Coupon("MAGIAMGIA5", true, 0.5));
		}
	};
	private Table currentTable;

	@FXML
	private void handleEnter() {
		boolean foundTable = false;
		if (tableInput.getText().isEmpty() || tableInput.getText().length() == 0) {
			main.setMessage("Please fill in the field with table number", message);
			return;
		}
		for (int i = 0; i < main.getTablesList().size(); i++) {
			if (tableInput.getText().compareTo(main.getTablesList().get(i).toString()) == 0) {
				foundTable = true;
				if (!main.getTablesList().get(i).getOccupied()) {
					if (!main.getTablesList().get(i).getMergeBoolean()) {
						if (main.confirmDialog("Do you want to open a bill at table "
								+ main.getTablesList().get(i).toString() + "?")) {
							main.getTablesList().get(i).setOccupied(true);
							tableNumber.setText(tableInput.getText());
							changeTableButton.setSelected(false);
							tableInput.setText("");
							changeTableBox.setVisible(false);
						}
					} else {
						main.setMessage("Change to merged table " + main.getTablesList().get(i).getMergeTable(),
								message);
						tableNumber.setText(main.getTablesList().get(i).getMergeTable());
						changeTableButton.setSelected(false);
						tableInput.setText("");
						changeTableBox.setVisible(false);
					}
				} else {
					tableNumber.setText(tableInput.getText());
					changeTableButton.setSelected(false);
					tableInput.setText("");
					changeTableBox.setVisible(false);
				}
			}
		}
		if (!foundTable) {
			main.setMessage("Table not found", message);
		}
	}

	@FXML
	private void handleEnterCoupon() {
		for (int i = 0; i < couponList.size(); i++) {
			if (discountField.getText().compareTo(couponList.get(i).getCouponCode()) == 0) {
				currentTable.setCoupon(couponList.get(i));
				setPriceLabel();
				discountField.setText("");
				discountInput.setVisible(false);
				main.setMessage("Discount successfully applied", message);
				return;
			}
		}
		main.setMessage("Invalid coupon code", message);
	}

	@FXML
	private void handleCancel() {
		if (currentTable != null) {
			if (!tableProductList.removeIf(c -> !c.getOrderSentBoolean())) {
				main.setMessage("You didn't choose any product", message);
			}
		} else {
			main.setMessage("Please choose a table first", message);
		}
	}

	@FXML
	private void handleSend() {
		if (currentTable != null) {
			if (tableProductList.size() > 0) {
				for (int i = 0; i < tableProductList.size(); i++) {
					if (!tableProductList.get(i).getOrderSentBoolean()) {
						ObservableList<CartProduct> copyOrder = FXCollections.observableArrayList(tableProductList);
						for (int j = 0; j < copyOrder.size(); j++) {
							if (!copyOrder.get(j).getOrderSentBoolean()) {
								copyOrder.get(j).sendOrder();
								main.getWaitingList().add(copyOrder.get(j));
							}
						}
						tableProductList.setAll(copyOrder);
						main.setMessage("Order sent to the kitchen", message);
						return;
					}
				}
				main.setMessage("You didn't choose any product", message);
			} else {
				main.setMessage("Your cart is empty at the moment", message);
			}
		} else {
			main.setMessage("Please choose a table first", message);
		}
	}

	@FXML
	private void handleDiscount() {
		if (currentTable != null) {
			if (discountButton.isSelected()) {
				discountInput.setVisible(true);
			} else {
				currentTable.setCoupon(new Coupon());
				setPriceLabel();
				discountInput.setVisible(false);
			}
		} else {
			main.setMessage("Please choose a table first", message);
		}
	}

	@FXML
	private void handlePrint() {
		if (currentTable != null) {
			if (tableProductList.size() > 0) {
				try {
					FXMLLoader thirdLoader = new FXMLLoader(getClass().getResource("/fxmlDocument/BillView.fxml"));
					Parent thirdUI = thirdLoader.load();
					BillController billController = thirdLoader.getController();
					billController.setContent(currentTable);
					Scene scene = new Scene(thirdUI);
					Stage dialogStage = new Stage();
					main.lockedSizeStage(dialogStage);
					dialogStage.setScene(scene);
					dialogStage.showAndWait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				main.setMessage("You haven't bought anything", message);
			}
		} else {
			main.setMessage("Please choose a table first", message);
		}
	}

	@FXML
	private void handlePay() {
		if (currentTable != null) {
			if (tableProductList.size() > 0) {
				if (currentTable.getBalance() > 0) {
					for (int i = 0; i < tableProductList.size(); i++) {
						if (!tableProductList.get(i).getProductDeliveredBoolean()) {
							main.setMessage("You still have unfinished product in your cart", message);
							return;
						}
					}
					boolean isPaymentComplete = false;
					try {
						FXMLLoader thirdLoader = new FXMLLoader(
								getClass().getResource("/fxmlDocument/GuestPaymentView.fxml"));
						Parent thirdUI = thirdLoader.load();
						GuestPaymentController guestPaymentController = thirdLoader.getController();
						guestPaymentController.setMainController(main);
						guestPaymentController.setTable(currentTable);
						Scene scene = new Scene(thirdUI);
						Stage dialogStage = new Stage();
						main.lockedSizeStage(dialogStage);
						dialogStage.setScene(scene);
						dialogStage.showAndWait();
						isPaymentComplete = guestPaymentController.isCompleteClicked();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (isPaymentComplete) {
						tableNumber.setText("");
						main.setMessage("Payment complete", message);
					}
				} else {
					main.setMessage(
							"You are having a negative balance. Please disable your discount or buy more product",
							message);
				}
			} else {
				main.setMessage("You haven't bought anything", message);
			}
		} else {
			main.setMessage("Please choose a table first", message);
		}
	}

	private void setPriceLabel() {
		currentTable.setBill();
		subTotalLabel.setText(String.valueOf(currentTable.getSubtotal()));
		taxLabel.setText(String.valueOf(currentTable.getTax()));
		totalLabel.setText(String.valueOf(currentTable.getTotal()));
		discountLabel.setText(String.valueOf(currentTable.getDiscount()));
		balanceLabel.setText(String.valueOf(currentTable.getBalance()));
	}

	private void setMenu() {
		gridPane2.getChildren().clear();
		gridPane3.getChildren().clear();
		for (int i = 0; i < main.getTablesList().size(); i++) {
			if (tableNumber.getText().compareTo(main.getTablesList().get(i).toString()) == 0) {
				currentTable = main.getTablesList().get(i);
				tableProductList = currentTable.getCustomerProduct();
			}
		}
		cartView.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (lastSelectedRow.get() != null) {
					Bounds boundsOfSelectedRow = lastSelectedRow.get()
							.localToScene(lastSelectedRow.get().getLayoutBounds());
					if (boundsOfSelectedRow.contains(event.getSceneX(), event.getSceneY()) == false) {
						cartView.getSelectionModel().clearSelection();
					}
				}
			}
		});
		setPriceLabel();
		if (currentTable.getCoupon().getDiscountValue() > 0) {
			discountButton.setSelected(true);
		} else {
			discountButton.setSelected(false);
		}
		cartView.setItems(tableProductList);
		for (int i = 0; i <= main.getCategories().size() / columnCount.get(); i++) {
			for (int j = 0; j < columnCount.get(); j++) {
				int categoryIndex = i * columnCount.get() + j;
				if (categoryIndex < main.getCategories().size()) {
					Label newCategory = new Label(main.getCategories().get(categoryIndex).toString());
					newCategory.setAlignment(Pos.CENTER);
					StackPane stack1 = new StackPane();
					Rectangle newRec1 = new Rectangle(150.0, 50.0, Paint.valueOf("cyan"));
					stack1.getChildren().addAll(newRec1, newCategory);
					gridPane1.add(stack1, j, i);
					stack1.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							gridPane2.getChildren().clear();
							for (int i = 0; i <= main.getCategories().get(categoryIndex).getTypes().size()
									/ columnCount.get(); i++) {
								for (int j = 0; j < columnCount.get(); j++) {
									int typeIndex = i * columnCount.get() + j;
									if (typeIndex < main.getCategories().get(categoryIndex).getTypes().size()) {
										Label newType = new Label(
												main.getCategories().get(categoryIndex).getTypes().get(typeIndex));
										newType.setAlignment(Pos.CENTER);
										StackPane stack2 = new StackPane();
										Rectangle newRec2 = new Rectangle(150.0, 50.0, Paint.valueOf("cyan"));
										stack2.getChildren().addAll(newRec2, newType);
										gridPane2.add(stack2, j, i);
										stack2.setOnMouseClicked(new EventHandler<MouseEvent>() {
											@Override
											public void handle(MouseEvent event) {
												gridPane3.getChildren().clear();
												displayMenu.clear();
												for (int i = 0; i < main.getProductData().size(); i++) {
													if (main.getProductData().get(i).getCategory() == categoryIndex
															&& main.getProductData().get(i).getType() == typeIndex) {
														displayMenu.add(main.getProductData().get(i));
													}
												}
												for (int i = 0; i <= displayMenu.size() / columnCount.get(); i++) {
													for (int j = 0; j < columnCount.get(); j++) {
														int menuIndex = i * columnCount.get() + j;
														if (menuIndex < displayMenu.size()) {
															Label menuProduct = new Label(
																	displayMenu.get(menuIndex).toString());
															menuProduct.setAlignment(Pos.CENTER);
															StackPane stack3 = new StackPane();
															Rectangle newRec3 = new Rectangle(150.0, 50.0,
																	Paint.valueOf("cyan"));
															stack3.getChildren().addAll(newRec3, menuProduct);
															gridPane3.add(stack3, j, i);
															stack3.setOnMouseClicked(new EventHandler<MouseEvent>() {
																@Override
																public void handle(MouseEvent event) {
																	if (event.getClickCount() == 2) {
																		for (int i = 0; i < tableProductList
																				.size(); i++) {
																			if (displayMenu.get(menuIndex).toString()
																					.compareTo(tableProductList.get(i)
																							.toString()) == 0
																					&& !tableProductList.get(i)
																							.getOrderSentBoolean()) {
																				main.setMessage(
																						"This product is already in your cart",
																						message);
																				return;
																			}
																		}
																		tableProductList.add(new CartProduct(
																				displayMenu.get(menuIndex),
																				tableNumber.getText()));
																		setPriceLabel();
																	}
																}
															});
														}
													}
												}
											}
										});
									}
								}
							}
						}
					});
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cartPane.prefWidthProperty().bind((mainPane.widthProperty().subtract(50)).divide(2));
		choicePane.prefWidthProperty().bind((mainPane.widthProperty().subtract(50)).divide(2));
		menuPane.prefHeightProperty().bind(choicePane.heightProperty().subtract(325));
		columnCount.bind(choicePane.widthProperty().divide(150));
		gridPane1.setPadding(new Insets(5, 50, 5, 50));
		gridPane1.setVgap(15);
		gridPane1.setHgap(15);
		gridPane2.setPadding(new Insets(5, 50, 5, 50));
		gridPane2.setVgap(15);
		gridPane2.setHgap(15);
		gridPane3.setPadding(new Insets(25, 50, 25, 50));
		gridPane3.setVgap(25);
		gridPane3.setHgap(15);
		categoriesPane.setContent(gridPane1);
		typesPane.setContent(gridPane2);
		menuPane.setContent(gridPane3);
		productNameList.prefWidthProperty().bind(cartView.widthProperty().multiply(0.25));
		productNameList.setCellValueFactory(c -> c.getValue().productNameProperty());
		eachList.prefWidthProperty().bind(cartView.widthProperty().multiply(0.11));
		eachList.setCellValueFactory(c -> c.getValue().priceProperty());
		totalList.prefWidthProperty().bind(cartView.widthProperty().multiply(0.11));
		totalList.setCellValueFactory(c -> c.getValue().totalPriceProperty());
		quantityList.prefWidthProperty().bind(cartView.widthProperty().multiply(0.2));
		quantityList.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue()));
		quantityList.setCellFactory(c -> new TableCell<CartProduct, CartProduct>() {
			@Override
			protected void updateItem(CartProduct cartProduct, boolean empty) {
				super.updateItem(cartProduct, empty);
				if (cartProduct == null || empty) {
					setGraphic(null);
					return;
				} else if (cartProduct.getOrderSentBoolean()) {
					setGraphic(new Label(String.valueOf(cartProduct.getAmount())));
					return;
				}
				Label quantity = new Label("");
				quantity.setPrefSize(25.0, 50.0);
				quantity.setAlignment(Pos.CENTER);
				quantity.setText(String.valueOf(cartProduct.getAmount()));
				StackPane button1 = new StackPane(new Circle(15, Paint.valueOf("gray")), new Label("-"));
				StackPane button2 = new StackPane(new Circle(15, Paint.valueOf("gray")), new Label("+"));
				button1.setOnMouseClicked(e -> {
					if (cartProduct.getAmount() == 1) {
						tableProductList.remove(cartProduct);
					} else {
						cartProduct.setAmountAndPrice(cartProduct.getAmount() - 1);
						quantity.setText(String.valueOf(cartProduct.getAmount()));
					}
					setPriceLabel();
				});
				button2.setOnMouseClicked(e -> {
					cartProduct.setAmountAndPrice(cartProduct.getAmount() + 1);
					quantity.setText(String.valueOf(cartProduct.getAmount()));
					setPriceLabel();
				});
				HBox pane = new HBox(button1, quantity, button2);
				pane.setAlignment(Pos.CENTER);
				setGraphic(pane);
			}
		});
		timeOrderedList.prefWidthProperty().bind(cartView.widthProperty().multiply(0.11));
		timeOrderedList.setCellValueFactory(c -> c.getValue().timeOrderedProperty());
		timeOrderedList
				.setCellFactory(new Callback<TableColumn<CartProduct, LocalTime>, TableCell<CartProduct, LocalTime>>() {
					@Override
					public TableCell<CartProduct, LocalTime> call(TableColumn<CartProduct, LocalTime> param) {
						return new TableCell<CartProduct, LocalTime>() {
							@Override
							protected void updateItem(LocalTime item, boolean empty) {
								super.updateItem(item, empty);
								if (item != null && !empty) {
									DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
									setText(item.format(dtf));
								} else {
									setText(null);
								}
							}
						};
					}
				});
		timeCompletedList.prefWidthProperty().bind(cartView.widthProperty().multiply(0.11));
		timeCompletedList.setCellValueFactory(c -> c.getValue().timeCompletedProperty());
		timeCompletedList
				.setCellFactory(new Callback<TableColumn<CartProduct, LocalTime>, TableCell<CartProduct, LocalTime>>() {
					@Override
					public TableCell<CartProduct, LocalTime> call(TableColumn<CartProduct, LocalTime> param) {
						return new TableCell<CartProduct, LocalTime>() {
							@Override
							protected void updateItem(LocalTime item, boolean empty) {
								super.updateItem(item, empty);
								if (item != null && !empty) {
									DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
									setText(item.format(dtf));
								} else {
									setText(null);
								}
							}
						};
					}
				});
		statusList.prefWidthProperty().bind(cartView.widthProperty().multiply(0.125));
		statusList.setCellValueFactory(c -> c.getValue().statusProperty());
		cartView.getColumns().addAll(productNameList, quantityList, eachList, totalList, timeOrderedList,
				timeCompletedList, statusList);
		cartView.setItems(tableProductList);
		changeTableButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				changeTableBox.setVisible(changeTableButton.isSelected());
			}
		});
		tableInput.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
		discountField.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
		tableNumber.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				if (newValue == "") {
					tableMessage.setVisible(true);
					currentTable = null;
					tableProductList = null;
					gridPane1.getChildren().clear();
					gridPane2.getChildren().clear();
					gridPane3.getChildren().clear();
				} else {
					tableMessage.setVisible(false);
					setMenu();
				}
			}
		});
		cartView.setRowFactory(tableView -> {
			TableRow<CartProduct> row = new TableRow<CartProduct>();
			row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
				if (isNowSelected) {
					lastSelectedRow.set(row);
				}
			});
			return row;
		});
		cartView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if (event.getClickCount() == 2 && cartView.getSelectionModel().getSelectedIndex() != -1
						&& cartView.getSelectionModel().getSelectedItem().getProductReadyBoolean()) {
					cartView.getSelectionModel().getSelectedItem().productDelivered();
					for (int i = 0; i < tableProductList.size(); i++) {
						if (tableProductList.get(i).getProductReadyBoolean()
								&& !tableProductList.get(i).getProductDeliveredBoolean()) {
							return;
						}
					}
					currentTable.setFoodReady(false);
				}
			}
		});
		filterField.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toLowerCase());
			return change;
		}));
		filterField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null && tableNumber.getText() != "") {
					displayMenu.clear();
					gridPane2.getChildren().clear();
					gridPane3.getChildren().clear();
					for (int i = 0; i < main.getProductData().size(); i++) {
						if (main.getProductData().get(i).toString().toLowerCase().contains(newValue)) {
							displayMenu.add(main.getProductData().get(i));
						}
					}
					for (int i = 0; i <= displayMenu.size() / columnCount.get(); i++) {
						for (int j = 0; j < columnCount.get(); j++) {
							int menuIndex = i * columnCount.get() + j;
							if (menuIndex < displayMenu.size()) {
								Label menuProduct = new Label(displayMenu.get(menuIndex).toString());
								menuProduct.setAlignment(Pos.CENTER);
								StackPane stack3 = new StackPane();
								Rectangle newRec3 = new Rectangle(150.0, 50.0, Paint.valueOf("cyan"));
								stack3.getChildren().addAll(newRec3, menuProduct);
								gridPane3.add(stack3, j, i);
								stack3.setOnMouseClicked(new EventHandler<MouseEvent>() {
									@Override
									public void handle(MouseEvent event) {
										if (event.getClickCount() == 2) {
											for (int i = 0; i < tableProductList.size(); i++) {
												if (displayMenu.get(menuIndex).toString()
														.compareTo(tableProductList.get(i).toString()) == 0
														&& !tableProductList.get(i).getOrderSentBoolean()) {
													main.setMessage("This product is already in your cart", message);
													return;
												}
											}
											tableProductList.add(
													new CartProduct(displayMenu.get(menuIndex), tableNumber.getText()));
											setPriceLabel();
										}
									}
								});
							}
						}
					}
				}
			}
		});
	}

	public void setMainController(MainController mc) {
		main = mc;
	}

	public void setTableNumber(String tableNumber) {
		this.tableNumber.setText(tableNumber);
	}
}
