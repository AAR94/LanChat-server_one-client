package viewController;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import control.Control;
import control.ControlServer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Server;

public class ServerController implements Initializable{
	@FXML
	TextArea conversation_TextA;	
	@FXML
	TextField chat_TextF;
	@FXML
	TextField name_TextF;
	@FXML
	TextField port_TextF;
	@FXML
	Button connect_Button;
	@FXML
	Button send_Button;
	@FXML
	Button clear_Button;
	@FXML
	Button cancel_Button;
	@FXML
	RadioButton client_RadioB;
	@FXML
	MenuItem close_MenuItem;
	@FXML
	MenuItem about_MenuItem;
	@FXML
	MenuItem home_MenuItem;
	
	
	ServerSocket serverSocket;
	Socket socket;
	String message_distant="";
	Server s=new Server();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initComponents();
		initActions();
		name_TextF.setText("Mr robot");
		port_TextF.setText("2009");
	}
	public void initComponents() {
		connect_Button.setDisable(false);
		cancel_Button.setDisable(true);
		send_Button.setDisable(true);
		//clear_Button.setId("font-button");
		
	}
	public void initActions() {
		close_MenuItem.setOnAction(event->{
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
	    		 conversation_TextA.appendText(name_TextF.getText()+": "+chat_TextF.getText()+"\n");
				 ControlServer.sendMessage(name_TextF.getText()+": "+chat_TextF.getText().toString());
				
			 } catch (Exception e) {
				e.printStackTrace();
			 }
	    	 chat_TextF.clear();

	     });
	     
	     clear_Button.setOnAction(event->{
	    	 conversation_TextA.clear();
	    	 chat_TextF.clear();

	     });
	     
	     connect_Button.setOnAction(event->{
    		 Alert alert=new Alert(Alert.AlertType.INFORMATION);
	    	 if(ControlServer.isServerOk(name_TextF.getText(), port_TextF.getText())) {
				 Server s=new Server(name_TextF.getText(),Integer.valueOf(port_TextF.getText()));
	    		 if(!ControlServer.usedPorts.contains(s.getPort())) {
	    			 ControlServer.usedPorts.add(s.getPort());			
	    			 connect_Button.setDisable(true);
					 cancel_Button.setDisable(false);
					 chat_TextF.requestFocus();
					 send_Button.setDisable(false);
 			    	 name_TextF.setDisable(true);
 			    	 port_TextF.setDisable(true);
 			    	 
 					 conversation_TextA.appendText("Waiting for Clients..\n");		    	 
 					 new Thread() {
			 			public void run() {
			 				ControlServer.openConnection(s,serverSocket, socket);
			 			}
 					 }.start();	
 					 new Thread(){
			 			public void run(){
					 		listen();
			 			}
 					 }.start();
	    		 }else {
	    			 alert.setTitle("about Port");
	    			 alert.setContentText("You have already used that port, You need to change It or restart the application to use It again!");
		    		 alert.show();
	    		 }
	    		 
	    	 }else {
	    		 alert.setContentText("Something wrong!");
	    		 alert.show();
	    	 }

	     });
	     
	     chat_TextF.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent key) {
				if(key.getCode().equals(KeyCode.ENTER))
					send_Button.fire();
			}
	    	 
	     });
	     
	     cancel_Button.setOnAction(event->{
	    	 connect_Button.setDisable(false);
			 cancel_Button.setDisable(true);
			 send_Button.setDisable(true);
			 name_TextF.setDisable(false);
		     port_TextF.setDisable(false);
		     ControlServer.connected.set(false);
			 ControlServer.closeConnection(serverSocket,socket);
	     });
	     
	     ControlServer.changeState.addListener(new ChangeListener<Boolean>() {
	    	 public void changed(ObservableValue<? extends Boolean> Observable,Boolean oldValue,Boolean newValue) {
	    		 cancel_Button.fire();
				 System.out.println("in changeState");
	    	 }
			
	     });
	     
	     Main.globalStage.setOnCloseRequest(event->{
	    	 	cancel_Button.fire();
				System.exit(0);
		});
	}
	
	public void listen() {
		message_distant="";
		
		while(!ControlServer.connected.get());
		ControlServer.sendMessage("You are now connected to "+name_TextF.getText()+" .");
     	 
    	 new Thread() {
			public void run() {
			  synchronized(ServerController.class){
				  while(true) {
						if(!ControlServer.connected.get())
							break;
						
						try {
							message_distant = ControlServer.readMessage();
							
								if(message_distant.equals(Control.client_STOP)) {
									ControlServer.connected.set(false);
									System.out.println("out of listen");
									break;

								}
								if(message_distant!=null)
									if(!message_distant.equals("") )
										conversation_TextA.appendText(message_distant+"\n");
									message_distant="";
						} catch (Exception e) {
							cancel_Button.fire();
							System.out.println("your r out server");

							interrupt();
							break;
							//System.out.println("after inside interrupt in the server");
						}
					
					}
				  if(message_distant.equals(Control.client_STOP)) {
						message_distant="";
						listen();
					}
					interrupt();
			  }
				
				

				  
				}
			}.start();
			
			
		
	}
	
}
