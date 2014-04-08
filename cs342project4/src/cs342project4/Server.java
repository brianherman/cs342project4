package cs342project4;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server implements ServerThreadCallback{
	private static ArrayList<ServerThread> threads = new ArrayList<ServerThread>();

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(25565);
			try{
				while(true){
					serverSocket.setSoTimeout(10000);
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
}
