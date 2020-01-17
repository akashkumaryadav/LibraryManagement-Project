package application;
	
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		try{
		Parent root = FXMLLoader.load(getClass().getResource("views/MainViewMaterialStyle.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("LMS");
		primaryStage.setScene(scene);
		primaryStage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
}
	
	public static void main(String[] args) {
		launch(args);
	}
}
