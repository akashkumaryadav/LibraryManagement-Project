package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import application.models.Member;
import database.DatabaseConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ListMemberController implements Initializable{
	
	Connection con;

	ObservableList<Member> memberlist = FXCollections.observableArrayList();
	
    @FXML
    private TableView<Member> tableview;

    @FXML
    private TableColumn<Member, String> nameCol;

    @FXML
    private TableColumn<Member, String> phoneCol;

    @FXML
    private TableColumn<Member, String> idCol;

    @FXML
    private TableColumn<Member, String> emailCol;
    
    @Override
    public void initialize(URL arg0,ResourceBundle arg1) {
    	try{
    		con = DatabaseConnect.establishConnection(DatabaseConnect.getBaseurl()+"librarymanagementsystem"+DatabaseConnect.getTimezone(), "root", "");
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	initcol();
    	loadData();
    }
    
    public void loadData() {
    	try {
    		Statement stmnt = con.createStatement();
    		ResultSet rs    = stmnt.executeQuery("select * from members");
    		while(rs.next()) {
    			memberlist.add(new Member(rs.getString("name"),rs.getString("id"),rs.getString("phone"),rs.getString("email")));
    		}
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	tableview.getItems().addAll(memberlist);
    }
    
    public void initcol() {
    	nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    	idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    	phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
    	emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }


}
