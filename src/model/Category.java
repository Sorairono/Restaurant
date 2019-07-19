package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Category {
	private final StringProperty categoryName = new SimpleStringProperty("");
	private ObservableList<String> types = FXCollections.observableArrayList();

	public Category(String categoryName, ObservableList<String> listType) {
		setCategoryName(categoryName);
		setTypes(listType);
	}

	@Override
	public String toString() {
		return categoryName.get();
	}


	public ObservableList<String> getTypes() {
		return types;
	}

	public void setCategoryName(String s) {
		categoryName.set(s);
	}

	public void setTypes(ObservableList<String> listType) {
		types = FXCollections.observableArrayList(listType);
	}
}
