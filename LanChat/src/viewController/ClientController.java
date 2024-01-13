package viewController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import control.Control;
import control.ControlClient;
import control.ControlServer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Client;

public class ClientController implements Initializable{
	@FXML
	MenuItem close_MenuItem;
	@FXML
	MenuItem about_MenuItem;
	@FXML
	MenuItem home_MenuItem;
	@FXML
	TextArea conversation_TextA;	
	@FXML
	TextField chat_TextF;
	@FXML
	TextField hostName_TextF;
	@FXML
	TextField serverLocalIP_TextF;
	@FXML
	TextField serverPort_TextF;
	@FXML
	Button send_Button;
	@FXML
	Button clear_Button;
	@FXML
	Button connect_Button;
	@FXML
	Button cancel_Button;
	
	Client c=new Client();
	Socket socket;
	//volatile String message_distant="";
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initComponents();
		initActions();
		
	}
	public void initComponents() {
		hostName_TextF.setText("User");
		serverLocalIP_TextF.setText("127.0.0.1");
		serverPort_TextF.setText("2009");
		connect_Button.setDisable(false);
		cancel_Button.setDisable(true);
		send_Button.setDisable(true);

	}
	public void initActions() {
		close_MenuItem.setOnAction(event->{
			if(!cancel_Button.isDisabled())
				cancel_Button.fire();
			 System.exit(0);
		 });
		
	     home_MenuItem.setOnAction(event->{
	    	    
				try {
					Scene scene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("view/Home.fxml")));
					scene.getStylesheets().add("view/Nouveau.css");
					Main.globalStage.setScene(scene);
					Main.globalStage.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		 });
	     
	     about_MenuItem.setOnAction(event->{

	     });
	     
	     send_Button.setOnAction(event->{
	    	 try {
				conversation_TextA.appendText(c.getName()+": "+chat_TextF.getText().toString()+"\n");
				ControlClient.sendMessage(c.getName()+": "+chat_TextF.getText().toString());
				System.out.println("after send");
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	 chat_TextF.clear();

	     });
	     
	     chat_TextF.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent key) {
					if(!send_Button.isDisabled() && key.getCode().equals(KeyCode.ENTER))
						send_Button.fire();
				}
		    	 
		   });
	     
	     clear_Button.setOnAction(event->{
	    	 conversation_TextA.clear();
	    	 chat_TextF.clear();

	     });
	     
	     connect_Button.setOnAction(event->{
	    	 if( ControlClient.isClientOk(hostName_TextF.getText(),serverLocalIP_TextF.getText(),serverPort_TextF.getText())){
	    		 c=new Client(hostName_TextF.getText(),serverLocalIP_TextF.getText(),Integer.valueOf(serverPort_TextF.getText()));
	    		 ControlClient.wait.set(true);
 				 connect_Button.setDisable(true);
 				 cancel_Button.setDisable(false);
 				 chat_TextF.requestFocus();
				 hostName_TextF.setDisable(true);
			     serverLocalIP_TextF.setDisable(true);
			     serverPort_TextF.setDisable(true);

	    				 new Thread() {
	    						public void run() {
	    							String message_distant="";
				 					conversation_TextA.appendText("Waiting to connect..\n");
				 					try {
				 						ControlClient.waitForServer(socket, c);
		    				    		ControlClient.sendMessage(c.getName()+" is connected !");
		    			 				send_Button.setDisable(false);
					 			    	ControlClient.connected.set(true);
					 			    	synchronized(ClientController.class) {
					 			    		while(true) {
												message_distant=ControlClient.readMessage();
												if(Control.server_STOP.equals(message_distant))
													break;
												if(!message_distant.equals(""))
		    										conversation_TextA.appendText(message_distant+"\n");
												message_distant="";
										
					 			    		}
					 			    	}
				    					System.out.println(message_distant==null?"null" : message_distant+"  "+Control.server_STOP);
				    					//if(message_distant.equals(Control.server_STOP)) {
						 			    	cancel_Button.fire();
						 			    	message_distant="";
						 			    	interrupt();
				    					//}
				 					}catch(Exception e) {
				 						
				 					}
	    				    		
		    									
	    							
			    				}
			    							
			    						
			    			}.start();
	    					
	    	 }else {
	    		 Alert alert=new Alert(Alert.AlertType.INFORMATION);
	    		 alert.setContentText("Something wrong!");
	    		 alert.show();
	    	 }
	     });
	     
	     cancel_Button.setOnAction(event->{
			 conversation_TextA.appendText("You disconnected .\n");
	    	 ControlClient.sendMessage(hostName_TextF.getText()+" is disconnected");
    		 ControlClient.wait.set(false);
		     ControlClient.connected.set(false);
	    	 connect_Button.setDisable(false);
			 cancel_Button.setDisable(true);
			 send_Button.setDisable(true);
			 hostName_TextF.setDisable(false);
		     serverLocalIP_TextF.setDisable(false);
		     serverPort_TextF.setDisable(false);
			 ControlClient.closeConnection(socket);
	     });
	     
	     Main.globalStage.setOnCloseRequest(event->{
	    	 	cancel_Button.fire();
				System.exit(0);
		});
	     
	     
	}
	
}