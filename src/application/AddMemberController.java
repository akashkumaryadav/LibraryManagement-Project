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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddMemberController implements Initializable{
	
	Connection con;

    @FXML
    private AnchorPane rootpane;

    @FXML
    private JFXTextField member_name;

    @FXML
    private JFXTextField member_id;

    @FXML
    private JFXTextField member_phone;

    @FXML
    private JFXTextField member_email;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    void cancel(ActionEvent event) {
    	Stage stage  = (Stage) rootpane.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void save(ActionEvent event) {
    	try {
    		String name = member_name.getText();
    		String id = member_id.getText();
    		String phone = member_phone.getText();
    		String email = member_email.getText();
    		Statement stmnt = con.createStatement();
    		if(name.isEmpty() || id.isEmpty() || member_phone.getText().isEmpty() || email.isEmpty()) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setHeaderText(null);
    			alert.setContentText("No fileds should be blank");
    			alert.showAndWait();
    			return;
    		}else {
    		stmnt.executeUpdate("Insert into members values ('" + name + "','"
    															+ id+ "',"
    															+Integer.parseInt(phone) + ",'"
    															+ email + "')");
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setHeaderText(null);
    		alert.setContentText("Successfuly added the member");
    		alert.showAndWait();
    		member_email.setText("");
    		member_id.setText("");
    		member_name.setText("");
    		member_phone.setText("");
    		}
    		
    	}catch(SQLIntegrityConstraintViolationException e) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setHeaderText(null);
    		alert.setContentText("member with the same id already exist");
    		alert.showAndWait();
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}

    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try{
			con = DatabaseConnect.establishConnection(DatabaseConnect.getBaseurl()
												 +"librarymanagementsystem"+DatabaseConnect.getTimezone(), "root", "");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

    
    public void checkData() {
    	
		try {
			Statement stmnt = con.createStatement();
			ResultSet rs = stmnt.executeQuery("select * from members");
			while(rs.next()) {
				System.out.println(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }

	
}
