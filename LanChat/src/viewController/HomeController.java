package viewController;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class HomeController implements Initializable{
	@FXML
	ToggleGroup typeConnection_ToggleG;
	@FXML
	RadioButton server_RadioB;
	@FXML
	RadioButton client_RadioB;
	@FXML
	MenuItem close_MenuItem;
	@FXML
	MenuItem about_MenuItem;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initComponents();
		initActions();
	}
	public void initComponents() {
		server_RadioB.setUserData("Server");
		client_RadioB.setUserData("Client");
	}
	public void initActions() {
		 typeConnection_ToggleG.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				try {
					Scene scene;
					if(newValue.getUserData().equals("Server")) {
						scene=new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("view/Server.fxml")));
						scene.getStylesheets().add("view/Nouveau.css");
						Main.globalStage.setScene(scene);
						Main.globalStage.show();
					}else if(newValue.getUserData().equals("Client")) {
						scene=new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("view/Client.fxml")));
						scene.getStylesheets().add("view/Nouveau.css");
						Main.globalStage.setScene(scene);
						Main.globalStage.show();
					}
					else {
						server_RadioB.setSelected(false);
						client_RadioB.setSelected(false);
					}
					
				}catch(Exception e) {
					
				}
			}
		 });	
		 
		 close_MenuItem.setOnAction(event->{
			 System.exit(0);
		 });
		
	     about_MenuItem.setOnAction(event->{
			 
		 });
	}
	
}