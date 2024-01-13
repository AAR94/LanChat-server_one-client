package control;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import model.Server;

public class ControlServer {
	public static BufferedReader in;
	public static PrintWriter out;
	public static BooleanProperty changeState=new SimpleBooleanProperty(true);
	public static List<Integer> usedPorts=new ArrayList<Integer>();
	public volatile static AtomicBoolean connected=new AtomicBoolean(false);
	public volatile static AtomicBoolean connectedToClient=new AtomicBoolean(false);
	static int i=0;
	static Socket socket;

	
	public static boolean isNumber(String str) {
		if(str.equals(""))
			return false;
		try {
			int i=Integer.valueOf(str);
			
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean isServerOk(String str1,String str2) {
		if(str1.equals("") || !isNumber(str2))
			return false;
		return true;	
	}
	
	public static void openConnection(Server s,ServerSocket serverSocket,Socket socket)  {
		try {			
			serverSocket=new ServerSocket(s.getPort());

			System.out.println("Le Serveur à l'écoute du port :"+serverSocket.getLocalPort());
			do {
				
				System.out.println("I'm in serverSocket.accept() !");
				ControlServer.socket=serverSocket.accept();
				socket=ControlServer.socket;
				connected.set(true);
				System.out.println("\nUn Client s'est connecté\n");
				in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out=new PrintWriter(socket.getOutputStream());
				while(connected.get()) {
					if(i==0)
						System.out.println("inside boucle ----------:"+i++);
				}
				sendMessage(Control.server_STOP);
				ControlServer.socket.close();
				ControlServer.in.close();
				ControlServer.out.close();
				if(socket.isClosed())
					System.out.println("Socket is closed");
			}while(true);
						
		}catch(Exception e) {
			closeConnection(serverSocket,socket);
		}
		
	}
	
	public static void closeConnection(ServerSocket serverSocket,Socket socket) {
		try {
			connected.set(false);
			changeState.set(!changeState.get());
			System.out.println("inside close connection !");
			ControlServer.out.println(Control.server_STOP);
		    ControlServer.out.flush();
		    ControlServer.socket.close();
			serverSocket.close();
			ControlClient.in.close();
			ControlClient.out.close();
			
		} catch (Exception e) {
		}
	}
	
	public  static void sendMessage(String mess) {
		try {
			out.println(mess==null ? "" : mess);
		    out.flush();
			
		} catch (Exception e) {
			ControlServer.out.println(Control.server_STOP);
		    ControlServer.out.flush();
		}

	}
	
	public synchronized static String readMessage() {
		try {
			String message_distant=in.readLine();
			return message_distant!=null ? message_distant : "";
			
		} catch (Exception e) {
			return "";
		}
		
	}
}
