package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import application.models.Books;
import database.DatabaseConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ListBookViewController implements Initializable {
	
	Connection con;
	
	ObservableList<Books>  list = FXCollections.observableArrayList();
	
    @FXML
    private TableView<Books> tableview;

    @FXML
    private TableColumn<Books, String> titleCol;

    @FXML
    private TableColumn<Books, String> authorCol;

    @FXML
    private TableColumn<Books, String> idCol;

    @FXML
    private TableColumn<Books, Boolean> availableCol;

    @FXML
	private TableColumn<Books, String> publisherCol;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
		con = DatabaseConnect.establishConnection(DatabaseConnect.getBaseurl() 
				+ "librarymanagementsystem" 
				+ DatabaseConnect.getTimezone(),
				"root", "");
		}catch (Exception e) {
			e.printStackTrace();
		}
		initColumns();
		loadData();
		
	}

	private void loadData() {
		try {
			Statement stmnt = con.createStatement();
			ResultSet rs = stmnt.executeQuery("select * from books");
			while(rs.next()) {
				String title = rs.getString("title");
				String id = rs.getString("id");
				String author = rs.getString("author");
				String publisher = rs.getString("publisher");
				Boolean available = rs.getBoolean("isAvailable");
				list.add(new Books(title,author,id,publisher,available));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		tableview.getItems().setAll(list);
		
	}

	private void initColumns() {
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
		publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
		
	}
    

}
