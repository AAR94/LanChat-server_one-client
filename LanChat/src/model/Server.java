package model;

public class Server {
	String name;
	int port;
	public Server() {
		
	}
	public Server(String name, int port) {
		super();
		this.name = name;
		this.port = port;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
