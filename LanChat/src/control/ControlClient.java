package control;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import model.Client;

public class ControlClient {
	public static BufferedReader in;
	public static PrintWriter out;
	public volatile static AtomicBoolean wait=new AtomicBoolean(false);
	public volatile static AtomicBoolean connectedToServer=new AtomicBoolean(false);
	public volatile static AtomicBoolean connected=new AtomicBoolean(false);


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
	
	public static boolean isClientOk(String str1,String str2,String str3) {
		if(str1.equals("") || str2.equals("") || !isNumber(str3))
			return false;
		return true;	
	}
	
	public static void waitForServer(Socket socket,Client c)  {
		try {
			socket=new Socket(c.getLocalIP(),c.getPort());
			in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream());
			//connectedToServer.set(true);

		}catch(Exception e) {
			if(wait.get()) {
				System.out.println("I'm waiting for the server");
				waitForServer(socket,c);
			}
			else {
				System.out.println("what's going on ?!");
			}
		}
	}
	
	public static void closeConnection(Socket socket) {
		try {
			sendMessage(Control.client_STOP);
			socket.close();
			ControlClient.in.close();
			ControlClient.out.close();
			

		} catch (Exception e) {
		}
	}
	
	public  static boolean sendMessage(String mess) {
		try {
			out.println(mess);
		    out.flush();
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

		
	}
	
	public synchronized static String readMessage() {
		try {
			String message_distant=ControlClient.in.readLine();
			return message_distant!=null ? message_distant : "";
			
		} catch (Exception e) {
			return "";
		}
		
	}
}
