package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import com.mysql.cj.protocol.Resultset;

import database.DatabaseConnect;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainViewController implements Initializable{
	
	Connection con;
	
	@FXML
    private VBox renewIcon;
	
	@FXML
	private VBox submitIcon;
	
	@FXML
    private VBox addBookIcon;

    @FXML
    private VBox addMemberIcon;

    @FXML
    private VBox viewBookIcon;

    @FXML
    private VBox ViewMemberIcon;

    @FXML
    private VBox settingsIcon;
	
	@FXML
	private VBox check;

	@FXML
	private HBox bookSearchBox;

	@FXML
	private HBox memberSearchBox;
	
    @FXML
    private TextField bookID;

    @FXML
    private JFXTextField returnbookID;
    
    @FXML
    private Label bookName;

    @FXML
    private Label author;

    @FXML
    private Label available;

    @FXML
    private TextField memberID;

    @FXML
    private Label memberName;
    
    @FXML
    private Label memberPhone;
    
    @FXML ListView<String> issuedBookInfo;

    @FXML
    void loadAddBook(ActionEvent event) throws IOException {
    	loadWindow("views/AddBookView.fxml", "Add Book");
    }

    @FXML
    void loadAddMember(ActionEvent event) throws IOException {
    	loadWindow("views/AddMemberView.fxml", "Add Member");
    }

    @FXML
    void loadViewBook(ActionEvent event) throws IOException {
    	loadWindow("views/ListBookView.fxml", "Books");
    }

    @FXML
    void loadViewMember(ActionEvent event) throws IOException {
    	loadWindow("views/ListMemberView.fxml", "Members");
    }
    
    public void loadWindow(String loc,String title) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource(loc));
    	Stage stage = new Stage(StageStyle.DECORATED);
    	stage.setScene(new Scene(root));
    	stage.setTitle(title);
    	stage.show();
    }
    
    @FXML
    void loadBookInfo(ActionEvent event) {
    	clearCache();
    	resetStyleBookInfo();
    	try {
    		String bookid = bookID.getText();
    		Statement stmnt = con.createStatement();
    		ResultSet rs = stmnt.executeQuery("select title,author,isAvailable from books where id=" + bookid);
    		
    		if(rs.next()) {
    			
    			
    			bookName.setText(rs.getString("title"));
    			author.setText(rs.getString("author"));
    			bookName.getStyleClass().add("found");
    			author.getStyleClass().add("found");
    			if(rs.getBoolean("isAvailable")) {   				
    				available.setText("Available");
    				available.getStyleClass().add("available");
    				bookID.getStyleClass().addAll("queryExist");
    			}else {
    				bookID.getStyleClass().addAll("queryNotExist");
    				available.setText("Not Available");
    				available.getStyleClass().add("notfound");
    			}
    		}else {
    			bookID.getStyleClass().add("queryNotExist");
    			bookName.setText("No Such Book Available");
    			bookName.getStyleClass().add("notfound");
    			
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void loadMemberInfo(ActionEvent event) {
    	resetStyleMemberInfo();
    	memberPhone.setText("");
    	memberName.setText("");
    	try {
    		String memberid = memberID.getText();
    		Statement stmnt = con.createStatement();
    		ResultSet rs = stmnt.executeQuery("select name,phone from members where ID=" + memberid);
    		
    		if(rs.next()) {
    			memberID.getStyleClass().add("queryExist");
    			memberName.setText(rs.getString(1));
    			memberPhone.setText(rs.getString(2));
    			memberName.getStyleClass().add("found");
    			memberPhone.getStyleClass().add("found");
    		}else {
    			memberID.getStyleClass().add("queryNotExist");
    			memberName.setText("No Such Member");
    			memberName.getStyleClass().add("notfound");
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    @FXML
    void issueBook(ActionEvent event) {
    	boolean avail = true;
    	try {
    		String mid = memberID.getText();
    		String bid = bookID.getText();
    		Statement stmnt = con.createStatement();
    		ResultSet rs = stmnt.executeQuery("select isAvailable from books where id = "+bid);
    		if(rs.next()) {
    			avail = rs.getBoolean("isAvailable");
    		}else {
    			avail = false;
    		}
    		if(avail) {
    		if(!(mid.isEmpty() || bid.isEmpty())) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setHeaderText("");
    		alert.setContentText("Are you sure you want to issue " + bookName.getText() + "to the member name " + memberName.getText());
    		Optional<ButtonType> respone = alert.showAndWait();
    		
    		if(respone.get() == ButtonType.OK) {
    		if(stmnt.executeUpdate("insert into issue  (bookId,memberId) values (" + bid +"," + mid +")") == 1 &&
    		stmnt.executeUpdate("Update books set isAvailable=0 where id='"+bid+"'") == 1){
    			Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        		alert1.setHeaderText("");
        		alert1.setContentText("book issued");
        		alert1.showAndWait();
    		}else {
    			Alert alert2 = new Alert(Alert.AlertType.ERROR);
        		alert2.setHeaderText("");
        		alert2.setContentText("Issue Failed");
        		alert2.showAndWait();
    		}
    		}else {
    			Alert alert1 = new Alert(Alert.AlertType.ERROR);
        		alert1.setHeaderText("");
        		alert1.setContentText("Issue Operation Canceled");
        		alert1.showAndWait();
    		}
    		}else {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
        		alert.setHeaderText("");
        		alert.setContentText("Issue not possible either member id is not provided or book id ");
        		alert.showAndWait();
    		}
    	}else{ 
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setHeaderText("");
    		alert.setContentText("Book is not Available ");
    		alert.showAndWait();
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    public void loadReturnBookInfo() throws SQLException {
    	if(returnbookID.getText().isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setHeaderText(null);
    		alert.setHeaderText("Field should not be empty");
    		alert.showAndWait();
    	}else {
    		String bid = returnbookID.getText();
    		
    		String mid = null;
    		String rc = null;
    		String timestamp = null;
    		Statement stmnt = con.createStatement();
    		ResultSet rs =stmnt.executeQuery("select * from issue where bookId="+bid);
    		if(rs.next()) {
    			mid = rs.getString("memberId");
    			rc = rs.getString(4);
    			timestamp = rs.getString(3).toString();
    			System.out.println(rc);
    		}else {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
        		alert.setHeaderText(null);
        		alert.setContentText("There is no pending submisson of the book id"+bid);
        		alert.showAndWait();
    		}
    		loadIssuedBookInfo(bid, stmnt);
    		loadMemberInfo(mid, stmnt);
    		if(rc != null) {
    		issuedBookInfo.getItems().add("Renew Count \t\t\t::>"+rc);
    		}
    		if(timestamp != null) {
    			issuedBookInfo.getItems().add("DateAndTime of issue \t::>"+timestamp);
    		}
    	}
    }
   
    
    private void loadIssuedBookInfo(String bid,Statement stmnt) throws SQLException {
    	ObservableList<String> list = FXCollections.observableArrayList();
    	ResultSet rs = stmnt.executeQuery("select * from books where id="+bid);
    	if(rs.next()) {
    		list.add("Book Name is 	\t\t::> "+rs.getString("title"));
    		//list.add("Book ID is  	\t\t::> "+rs.getString("id"));
    		list.add("Author is    	\t\t::> "+rs.getString("author"));
    		list.add("Publisher is 	\t\t::> "+rs.getString("publisher"));
    	}
    	issuedBookInfo.getItems().setAll(list);
    }
    
    private void loadMemberInfo(String mid,Statement stmnt) throws SQLException {
    	ObservableList<String> list = FXCollections.observableArrayList();
    	ResultSet rs = stmnt.executeQuery("select * from members where id="+mid);
    	if(rs.next()) {
    		list.add("Member Name is 	 \t::> "+rs.getString("name"));
    		list.add("Member ID is  	 \t\t::> "+rs.getString("id"));
    		list.add("Member Phone     	\t::> "+rs.getString("phone"));
    		list.add("Member Email 		::> "+rs.getString("email"));
    	}
    	issuedBookInfo.getItems().addAll(list);
    }
    
    
    @FXML
    public void renewBook() throws SQLException {
    	String bid = returnbookID.getText();
    	int rc = 0;
    	if(bid.isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setHeaderText(null);
    		alert.setContentText("Please provide book id");
    		alert.showAndWait();
    	}else {
    		Statement stmnt = con.createStatement();
    		ResultSet rs = stmnt.executeQuery("select renewCount from issue where bookId="+bid);
    		if(rs.next()) {
    			 rc = rs.getInt(1);
    		}
    	   try{
    		   rc++;
    		   stmnt.executeUpdate("update issue set issueTime=CURRENT_TIMESTAMP,renewCount="+rc+" where bookID="+bid);
    		   Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		   alert.setContentText("Book has been renewed");
    		   alert.setHeaderText(null);
    		   alert.showAndWait();
    	   }catch(Exception e) {
    		   e.printStackTrace();
    		   Alert alert = new Alert(Alert.AlertType.ERROR);
    		   alert.setContentText("Book cannot be renewed");
    		   alert.setHeaderText(null);
    		   alert.showAndWait();
    	   }
    	}
    	
    }
    
    @FXML
    public void submitBook() throws SQLException{
    	String bid = returnbookID.getText();
    	if(bid.isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setHeaderText(null);
    		alert.setContentText("Please provide book id");
    		alert.showAndWait();
    	}else {
    		Statement stmnt = con.createStatement();
    		stmnt.executeUpdate("DELETE FROM issue where bookId="+bid);
    		stmnt.executeUpdate("UPDATE books SET isAvailable=1 where id='"+bid+"'");
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setHeaderText(null);
    		alert.setContentText("Submited successfully Have a Good Day");
    		alert.showAndWait();
    	}
    	
    }
    
    @Override
    public void initialize(URL arg0,ResourceBundle arg1) {
    	initIcons();
    	try {
    		con = DatabaseConnect.establishConnection(DatabaseConnect.getBaseurl()+"librarymanagementsystem"+DatabaseConnect.getTimezone(), "root", "");
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    private void initIcons() {
    	
    	addBookIcon.getChildren().addAll(GlyphsDude.createIcon(FontAwesomeIcons.BOOK, "15px"));
//    	check.getChildren().addAll(GlyphsDude.createIcon(FontAwesomeIcons.CHECK, "15px"));
    	addMemberIcon.getChildren().addAll(GlyphsDude.createIcon(FontAwesomeIcons.USER, "15px"));
    	viewBookIcon.getChildren().addAll(GlyphsDude.createIcon(FontAwesomeIcons.TABLE, "15px"));
    	ViewMemberIcon.getChildren().addAll(GlyphsDude.createIcon(FontAwesomeIcons.TABLE, "15px"));
    	settingsIcon.getChildren().addAll(GlyphsDude.createIcon(FontAwesomeIcons.GEAR, "15px"));
 //   	renewIcon.getChildren().addAll(GlyphsDude.createIcon(FontAwesomeIcons.REPEAT, "15px"));
 //   	submitIcon.getChildren().addAll(GlyphsDude.createIcon(FontAwesomeIcons.CHECK_CIRCLE, "15px"));
    	
    	
    }
    
    public void clearCache() {
    	bookName.setText("");
    	author.setText("");
    	available.setText("");
    	
    }
    
    public void resetStyleBookInfo() {
    	bookName.getStyleClass().removeAll("found");
    	bookName.getStyleClass().removeAll("notfound");
    	bookID.getStyleClass().removeAll("queryExist");
    	bookID.getStyleClass().removeAll("queryNotExist");
    	available.getStyleClass().removeAll("available");
    	available.getStyleClass().removeAll("notfound");
    	author.getStyleClass().removeAll("found");
    }
    
    public void resetStyleMemberInfo() {
    	memberID.getStyleClass().remove("queryExist");
    	memberID.getStyleClass().remove("queryNotExist");
    	memberName.getStyleClass().remove("found");
    	memberName.getStyleClass().remove("notfound");
    }


}
