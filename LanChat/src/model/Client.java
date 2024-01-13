package model;

public class Client {
	String name="";
	String localIP="";
	int port;
	public Client() {
		
	}
	public Client(String name, String localIP, int port) {
		super();
		this.name = name;
		this.localIP = localIP;
		this.port = port;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocalIP() {
		return localIP;
	}
	public void setLocalIP(String localIP) {
		this.localIP = localIP;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
