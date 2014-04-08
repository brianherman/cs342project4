package cs342project4;
import java.net.*;
import java.util.ArrayList;
import java.util.Vector;
import java.io.*;

import javax.swing.*;

import java.awt.*;

public class Server extends JFrame implements ServerThreadIterface{
	private static Vector<ServerThread> threads = new Vector<ServerThread>();
	private JList log;
	private DefaultListModel logModel;
	
	public Server(){
		logModel = new DefaultListModel();
		log = new JList(logModel);
		log.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		log.setLayoutOrientation(JList.VERTICAL);
		log.setVisibleRowCount(-1);

		add(log);
		setSize(800,600);
		setVisible(true);
		
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(25565);
			log ("Server started open on Port: " +
					serverSocket.getLocalPort());
			InetAddress addr = InetAddress.getLocalHost();
			log("Local Host Name: " + addr.getHostName());
			log("Local Host Address: " + addr.getHostAddress());
			log("Listening...");
			try{
				while(true){
					try{
						ServerThread st = new ServerThread(serverSocket.accept(),new Server());
						new Thread(st).start();
						threads.add(st);
					}catch(SocketTimeoutException ste){
						System.err.println("Timeout Occured");
					}
				}
			}catch(IOException e){
				System.err.println("Accept failed.");
				System.exit(-1);
			}
		}catch(IOException e){
			System.err.println("Couldn't listen on port.");
			System.exit(-1);
		}finally{
			try{
				serverSocket.close();
			}catch(IOException e){
				System.err.println("Couldn't close port. 25565");
				System.exit(-1);
			}
		}

	}
	
	public ArrayList<String> getUsers()
	{
		ArrayList<String> users = new ArrayList<String>();
		for(ServerThread st : threads)
			users.add(st.name());
		return users;
	}
	@Override
	public void send(Evenlope m) {
		System.out.println(m);
		for(ServerThread s: threads)
		{
			for(String r : m.recipiants())
			{
				if(s.name().equals(r))
					s.send(m);
			}
		}
	}
	@Override
	public void remove(String user)
	{
		log("User "+user+" Left.");
		for(int i=0; i<threads.size(); i++){
			if(threads.get(i).name().equals(user))
			{
				threads.remove(i);
			}
		}
	}
	public void log(String l)
	{
		logModel.addElement(l);
	}
	
}
