package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Zone {
	private final StringProperty zoneName = new SimpleStringProperty("");
	private final StringProperty zoneLetter = new SimpleStringProperty("");
	private ObservableList<Table> tablesList = FXCollections.observableArrayList();
	private ObservableList<Table> mergedTablesList = FXCollections.observableArrayList();

	public Zone(String zoneName, String zoneLetter) {
		setZoneName(zoneName);
		setZoneLetter(zoneLetter);
	}
	
	public void setZoneName(String s) {
		zoneName.set(s);
	}
	
	public void setZoneLetter(String s) {
		zoneLetter.set(s);
	}
	
	public StringProperty zoneNameProperty() {
		return zoneName;
	}
	
	public StringProperty zoneLetterProperty() {
		return zoneLetter;
	}
	
	@Override
	public String toString() {
		return zoneName.get();
	}
	
	public String getZoneLetter() {
		return zoneLetter.get();
	}
	
	public ObservableList<Table> getTablesList() {
		return tablesList;
	}
	
	public ObservableList<Table> getMergedTablesList() {
		return mergedTablesList;
	}
}
