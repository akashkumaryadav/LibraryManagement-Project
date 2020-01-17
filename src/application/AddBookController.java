package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseConnect;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class AddBookController implements Initializable {

	Connection con;
	
	@FXML
	private AnchorPane rootpane;
	
    @FXML
    private JFXTextField id;

    @FXML
    private JFXTextField title;

    @FXML
    private JFXTextField author;

    @FXML
    private JFXTextField publisher;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    void cancel(ActionEvent event) {
    	Stage stage = (Stage) rootpane.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void save(ActionEvent event) {
    	String booktitle = title.getText();
    	String bookid = id.getText();
    	String bookauthor = author.getText();
    	String bookpublisher = publisher.getText();
    	try {
			Statement stmnt = con.createStatement();
			if(booktitle.isEmpty() || bookid.isEmpty() || bookauthor.isEmpty() || bookpublisher.isEmpty()) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setContentText("Please provide all details");
					alert.showAndWait();
					return;
			}else {
			stmnt.executeUpdate("insert into books values('" + booktitle +
								"','" + bookid + 
								"','" + bookauthor + 
								"','" + bookpublisher + "',"+ 1 +")");
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Added book successfully");
			alert.showAndWait();
			title.setText("");
	    	author.setText("");
	    	id.setText("");
	    	publisher.setText("");
			return;
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Book with this id already exist");
			alert.showAndWait();
			
			return;
		}	catch(SQLException e) {
			e.printStackTrace();
		}
    	
    	
    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try{
			con = DatabaseConnect.establishConnection(DatabaseConnect.getBaseurl()
											+"librarymanagementsystem"
											+DatabaseConnect.getTimezone(), "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkData() {
		try {
			Statement stmnt = con.createStatement();
			ResultSet rs  	= stmnt.executeQuery("select title,id from books");
			while(rs.next()) {
				String title = rs.getString("title");
				System.out.println(title);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
}
