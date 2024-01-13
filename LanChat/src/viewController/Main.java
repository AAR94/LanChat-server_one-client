package viewController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	public static Stage globalStage;
	public static void main(String[] args) throws Exception{
		launch(args);	
		Socket s;
		do{
			System.out.println("at new Socket-->");
			s=new Socket("192.168.1.100",2009);

		}while(!s.isConnected());
		System.out.println("after accept");
		/*BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out=new PrintWriter(s.getOutputStream());
		Thread.sleep(1000);
		System.out.println(in.readLine()); 
		System.out.println(in.readLine());*/ 
		
		DataOutputStream out=new DataOutputStream(s.getOutputStream());
		DataInputStream in=new DataInputStream(s.getInputStream());
		String m;
		while((m=in.readUTF())!=null)
			System.out.println(m);
		//System.out.println(in.readUTF());
		System.out.println("The end");
	}

	@Override
	public void start(Stage arg0) throws Exception {
		Scene scene=new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("view/Home.fxml")));
		scene.getStylesheets().add("view/Nouveau.css");
		arg0.setScene(scene);
		arg0.setTitle("LanChat by Abderrahmane Aloui");
		arg0.setResizable(false);
		arg0.show();
		arg0.setOnCloseRequest(event->{
			System.exit(0);
		});
		globalStage=arg0;
		
	}
}